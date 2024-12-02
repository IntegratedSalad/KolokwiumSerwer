import com.mysql.cj.protocol.Resultset;

import java.sql.*;
import java.util.concurrent.locks.ReentrantLock;

public class DbConnection {

    static final String URL = "jdbc:mysql://192.168.1.29:3306";
    static final String DB_NAME_QUERY = "USE javalab";
    static final String USER = "root";
    static final String PASSWORD = "haslo";
    static Connection connection;
    private static final ReentrantLock lock = new ReentrantLock();

    public static Connection connect() throws SQLException {
        lock.lock();
        try {
            if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            }
            return connection;
        } catch (SQLException e) {
            System.out.println("Błąd łączenia z bazą!");
            throw e;
        } finally {
            lock.unlock();
        }
    }

    public static Statement createStatement(Connection connection) throws SQLException {
        lock.lock();
        try {
            return connection.createStatement();
        } catch (SQLException e) {
            System.out.println("Błąd w tworzeniu statement!");
            throw e;
        } finally {
            lock.unlock();
        }
    }

    public static void closeConnection(Connection connection, Statement statement) throws SQLException {
        lock.lock();
        try {
            if (statement != null) {
                statement.close();
            }
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.out.println("Błąd w zamykaniu statement!");
            throw e;
        } finally {
            lock.unlock();
        }
    }

    public static ResultSet executeStatement(String query, Statement statement) throws SQLException {
        lock.lock();
        try {
            statement.execute(DB_NAME_QUERY);
            return statement.executeQuery(query);
        } catch (SQLException e) {
            System.out.println("Błąd w executeStatement!");
            throw e;
        } finally {
            lock.unlock();
        }
    }

    public static void updateStatement(String query, Statement statement) throws SQLException {
        lock.lock();
        try {
            statement.execute(DB_NAME_QUERY);
            statement.executeUpdate(query);
        } catch (SQLException e) {
            System.out.println("Błąd w updateStatement!");
            throw e;
        } finally {
            lock.unlock();
        }
    }

    public static void checkSetupDB(Statement statement) throws SQLException {
        try {
            String setupDB = "CREATE DATABASE IF NOT EXISTS javalab;";
            String useDB = "USE javalab;";
            String setupTableQ = """
                CREATE TABLE IF NOT EXISTS questions (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    question TEXT NOT NULL,
                    answers TEXT NOT NULL,
                    correct_answer TEXT NOT NULL);""";
            String setupTableA = """
                CREATE TABLE IF NOT EXISTS user_answers (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    username VARCHAR(255) NOT NULL,
                    question_id INT NOT NULL,
                    answer TEXT NOT NULL,
                    FOREIGN KEY (question_id) REFERENCES questions(id));""";
        /*  String questions = """
                INSERT IGNORE INTO questions (question, answers, correct_answer) VALUES
                     ('Która planeta jest największa w Układzie Słonecznym?', 'Mars Ziemia Jowisz Saturn', 'Jowisz'),
                     ('W którym roku wybuchła II wojna światowa?', '1939 1945 1914 1920', '1939'),
                     ('Ile nóg ma pająk?', '6 8 10 12', '8'),
                     ('Które zwierzę jest znane z tego, że śpi najdłużej w ciągu dnia?', 'Kot Słoń Leniwiec Kangur', 'Leniwiec'),
                     ('Który pierwiastek ma symbol chemiczny "O"?', 'Tlen Wodór Złoto Azot', 'Tlen');"""; */

            statement.executeUpdate(setupDB);
            statement.executeUpdate(useDB);
            statement.executeUpdate(setupTableQ);
            statement.executeUpdate(setupTableA);
        //  statement.executeUpdate(questions);

        } catch (SQLException e) {
            System.out.println("Błąd przygotowania bazy lub tabel!");
            throw e;
        }
    }
}
