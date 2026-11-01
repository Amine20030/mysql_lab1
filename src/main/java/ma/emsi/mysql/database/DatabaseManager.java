package ma.emsi.mysql.database;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseManager {

    private static String url;
    private static String user;
    private static String password;

    static {
        try (InputStream in = DatabaseManager.class.getClassLoader().getResourceAsStream("application.properties")) {
            Properties props = new Properties();
            if (in != null) {
                props.load(in);
            }
            url = props.getProperty("db.url", "jdbc:h2:mem:entreprise_db;MODE=MySQL;DATABASE_TO_LOWER=TRUE;DB_CLOSE_DELAY=-1");
            user = props.getProperty("db.user", "sa");
            password = props.getProperty("db.password", "");
            String driver = props.getProperty("db.driver", "org.h2.Driver");
            Class.forName(driver);
        } catch (Exception e) {
            throw new RuntimeException("Erreur de chargement de la configuration BDD", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }
}
