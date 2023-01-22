package savkin.ilya.db.connection;

import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Slf4j
public class PostgreSQLConnectionFactory {
    private final static String DB_DRIVER = "org.postgresql.Driver";
    private final static String CONNECTION_URI = "jdbc:postgresql://127.0.0.1:5432/cb-test";
    private final static String LOGIN = "postgres";
    private final static String PASSWORD = "1234";
    private static Connection connection;

    public static Connection getConnection() {
        if (connection == null) {
            try {
                Class.forName(DB_DRIVER);
                connection = DriverManager.getConnection(CONNECTION_URI, LOGIN, PASSWORD);
            }
            catch (ClassNotFoundException ex) {
                System.out.println("Не удалось найти драйвер для базы данных");
            } catch (SQLException ex) {
                System.out.println("Не удалось подключиться к базе данных (" + ex.getErrorCode() + ": " + ex.getMessage() + ").");
            }
        }
        return connection;
    }
}
