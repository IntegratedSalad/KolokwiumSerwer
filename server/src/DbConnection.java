import java.sql.*;

public class DbConnection {

    static final String url = "jdbc:mysql://172.20.10.2:3306/java";
    static final String user = "root";
    static final String password = "haslo";
    static Connection connection;

    public static Connection connect() {
        try {
            connection = DriverManager.getConnection(url, user, password);
            return connection;
        } catch (SQLException e) {
            System.out.print("Błąd łączenia z bazą!");
            return null;
        }
    }
}
