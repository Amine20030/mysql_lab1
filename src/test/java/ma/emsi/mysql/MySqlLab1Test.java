package ma.emsi.mysql;

import ma.emsi.mysql.database.DatabaseManager;
import ma.emsi.mysql.runner.SqlScriptRunner;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import static org.assertj.core.api.Assertions.assertThat;

public class MySqlLab1Test {

    private static Connection conn;

    @BeforeAll
    static void setUpDatabase() throws Exception {
        conn = DatabaseManager.getConnection();
        // Initialiser le schéma et les données
        SqlScriptRunner.executeScript(conn, "sql/01_schema.sql", false);
        SqlScriptRunner.executeScript(conn, "sql/02_seed_data.sql", false);
    }

    @Test
    @DisplayName("1. Vérification de la création du schéma et intégrité des tables")
    void testSchemaCreationAndTableIntegrity() throws Exception {
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM departements");
            assertThat(rs.next()).isTrue();
            assertThat(rs.getInt(1)).isGreaterThanOrEqualTo(4);

            rs = stmt.executeQuery("SELECT COUNT(*) FROM employes");
            assertThat(rs.next()).isTrue();
            assertThat(rs.getInt(1)).isGreaterThanOrEqualTo(6);

            rs = stmt.executeQuery("SELECT COUNT(*) FROM projets");
            assertThat(rs.next()).isTrue();
            assertThat(rs.getInt(1)).isGreaterThanOrEqualTo(3);

            rs = stmt.executeQuery("SELECT COUNT(*) FROM affectations");
            assertThat(rs.next()).isTrue();
            assertThat(rs.getInt(1)).isGreaterThanOrEqualTo(7);
        }
    }

    @Test
    @DisplayName("2. Filtrage des employés par salaire élevé (WHERE salaire >= 15000)")
    void testFilteringHighSalaries() throws Exception {
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT COUNT(*) FROM employes WHERE salaire >= ?")) {
            ps.setDouble(1, 15000.00);
            ResultSet rs = ps.executeQuery();
            assertThat(rs.next()).isTrue();
            // EMP-001 (18500), EMP-004 (16000), EMP-006 (19500)
            assertThat(rs.getInt(1)).isEqualTo(3);
        }
    }

    @Test
    @DisplayName("3. Requête INNER JOIN entre Employés et Départements")
    void testInnerJoinEmployeeDepartment() throws Exception {
        String sql = "SELECT e.matricule, d.nom AS dept_nom " +
                     "FROM employes e " +
                     "INNER JOIN departements d ON e.departement_id = d.id " +
                     "WHERE e.matricule = 'EMP-001'";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            assertThat(rs.next()).isTrue();
            assertThat(rs.getString("dept_nom")).isEqualTo("Technologies de l'Information");
        }
    }

    @Test
    @DisplayName("4. Agrégation avec GROUP BY et filtre HAVING (SUM(heures) >= 200)")
    void testAggregationAndHaving() throws Exception {
        String sql = "SELECT p.code, SUM(a.heures) AS total_heures " +
                     "FROM projets p " +
                     "JOIN affectations a ON p.id = a.projet_id " +
                     "GROUP BY p.id, p.code " +
                     "HAVING SUM(a.heures) >= 200";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            assertThat(rs.next()).isTrue();
            // PRJ-CLD : 120 + 160 + 100 = 380 heures
            assertThat(rs.getString("code")).isEqualTo("PRJ-CLD");
            assertThat(rs.getInt("total_heures")).isEqualTo(380);
        }
    }

    @Test
    @DisplayName("5. Mises à jour DML et vérification des transactions (COMMIT / ROLLBACK)")
    void testTransactionsAndUpdates() throws Exception {
        conn.setAutoCommit(false);
        try {
            try (Statement stmt = conn.createStatement()) {
                stmt.executeUpdate("UPDATE employes SET salaire = salaire + 1000 WHERE matricule = 'EMP-002'");
            }

            try (PreparedStatement ps = conn.prepareStatement("SELECT salaire FROM employes WHERE matricule = ?")) {
                ps.setString(1, "EMP-002");
                ResultSet rs = ps.executeQuery();
                assertThat(rs.next()).isTrue();
                assertThat(rs.getDouble("salaire")).isEqualTo(15500.00); // 14500 + 1000
            }

            conn.rollback(); // Annulation de la transaction

            try (PreparedStatement ps = conn.prepareStatement("SELECT salaire FROM employes WHERE matricule = ?")) {
                ps.setString(1, "EMP-002");
                ResultSet rs = ps.executeQuery();
                assertThat(rs.next()).isTrue();
                assertThat(rs.getDouble("salaire")).isEqualTo(14500.00); // Rétabli à 14500
            }
        } finally {
            conn.setAutoCommit(true);
        }
    }
}
