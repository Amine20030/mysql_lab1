package ma.emsi.mysql;

import ma.emsi.mysql.database.DatabaseManager;
import ma.emsi.mysql.runner.SqlScriptRunner;

import java.sql.Connection;

public class Main {

    public static void main(String[] args) {
        System.out.println("================================================================================");
        System.out.println("  LAB 1 : MISE EN PLACE ET DECOUVERTE DE MYSQL - EMSI CASABLANCA");
        System.out.println("================================================================================");
        System.out.println("-> Connexion au serveur de base de donnees MySQL...");
        
        try (Connection conn = DatabaseManager.getConnection()) {
            System.out.println("-> Connexion etablie avec succes. Version Driver : " + conn.getMetaData().getDriverVersion());
            System.out.println();

            System.out.println("--------------------------------------------------------------------------------");
            System.out.println(" 1. EXECUTION DU SCRIPT DDL (CREATION DU SCHEMA & TABLES)");
            System.out.println("--------------------------------------------------------------------------------");
            SqlScriptRunner.executeScript(conn, "sql/01_schema.sql", true);

            System.out.println("--------------------------------------------------------------------------------");
            System.out.println(" 2. EXECUTION DU SCRIPT DML (PEUPLEMENT DES DONNEES INITIALES)");
            System.out.println("--------------------------------------------------------------------------------");
            SqlScriptRunner.executeScript(conn, "sql/02_seed_data.sql", true);

            System.out.println("--------------------------------------------------------------------------------");
            System.out.println(" 3. EXECUTION DES REQUETES DQL (FILTRES, JOINTURES & AGREGATIONS)");
            System.out.println("--------------------------------------------------------------------------------");
            SqlScriptRunner.executeScript(conn, "sql/03_queries.sql", true);

            System.out.println("--------------------------------------------------------------------------------");
            System.out.println(" 4. MISES A JOUR, SUPPRESSIONS ET TRANSACTIONS (DML & TCL)");
            System.out.println("--------------------------------------------------------------------------------");
            SqlScriptRunner.executeScript(conn, "sql/04_dml_transactions.sql", true);

            System.out.println("================================================================================");
            System.out.println("  LAB 1 EXECUTE AVEC SUCCES (TOUTES LES OPERATIONS SQL VALIDEES)");
            System.out.println("================================================================================");
        } catch (Exception e) {
            System.err.println("Erreur lors de l'execution du Lab 1 : " + e.getMessage());
            e.printStackTrace();
        }
    }
}
