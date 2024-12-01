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

    public static void executeStatement(String query, Statement statement) throws SQLException {
        try {
            statement.executeQuery(query);
        } catch (SQLException e) {
            System.out.print("Błąd w readStatement!");
        }
    }
    public static void updateStatement(String query, Statement statement) throws SQLException {
        try {
            statement.executeUpdate(query);
        } catch (SQLException e) {
            System.out.print("Błąd w updateStatement!");
        }
    }
    public static void checkSetupDB(Statement statement) throws SQLException {
        try {
            String questionTable = """
        CREATE TABLE IF NOT EXISTS questions (
            id INT AUTO_INCREMENT PRIMARY KEY,
            question TEXT NOT NULL,
            answers TEXT NOT NULL,
            correct_answer TEXT NOT NULL);""";
            String answersTable = """
        CREATE TABLE IF NOT EXISTS user_answers (
            id INT AUTO_INCREMENT PRIMARY KEY,
            username VARCHAR(255) NOT NULL,
            question_id INT NOT NULL,
            answer TEXT NOT NULL,
            FOREIGN KEY (question_id) REFERENCES questions(id)); """;
            statement.executeUpdate(questionTable);
            statement.executeUpdate(answersTable);
        } catch (SQLException e) {
            System.out.print("Błąd przygotowania tabel!");
        }
    }
}
