import java.sql.*;

public class DbConnection {

    static final String url = "jdbc:mysql://172.20.10.2:3306/java";
    static final String user = "root";
    static final String password = "haslo";
    static Connection connection;

    public static Connection connect () throws SQLException {
        try {
            connection = DriverManager.getConnection(url, user, password);
            return connection;
        } catch (SQLException e) {
            System.out.print("Błąd łączenia z bazą!");
            return null;
        }
    }

    public static Statement createStatement(Connection connection) throws SQLException {
        try {
            return connection.createStatement();
        } catch (SQLException e) {
            System.out.print("Błąd w tworzeniu statement!");
            return null;
        }
    }

    public static void closeConnection(Connection connection, Statement statement) throws SQLException {
        try {
            statement.close();
            connection.close();
        } catch (SQLException e) {
            System.out.print("Błąd w zamykaniu statement!");
        }
    }
}
