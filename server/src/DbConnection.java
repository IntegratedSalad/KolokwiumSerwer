import java.sql.*;
import java.util.concurrent.locks.ReentrantLock;

public class DbConnection {

    static final String URL = "jdbc:mysql://localhost:3306";
    static final String DB_NAME = "javalab";
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
            System.out.print("Błąd łączenia z bazą!");
            return null;
        } finally {
            lock.unlock();
        }
    }

    public static Statement createStatement(Connection connection) throws SQLException {
        lock.lock();
        try {
            return connection.createStatement();
        } catch (SQLException e) {
            System.out.print("Błąd w tworzeniu statement!");
            return null;
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
            System.out.print("Błąd w zamykaniu statement!");
        } finally {
            lock.unlock();
        }
    }

    public static void executeStatement(String query, Statement statement) throws SQLException {
        lock.lock();
        try {
            statement.executeQuery(query);
        } catch (SQLException e) {
            System.out.print("Błąd w readStatement!");
        } finally {
            lock.unlock();
        }
    }

    public static void updateStatement(String query, Statement statement) throws SQLException {
        lock.lock();
        try {
            statement.executeUpdate(query);
        } catch (SQLException e) {
            System.out.print("Błąd w updateStatement!");
        } finally {
            lock.unlock();
        }
    }

    public static void checkSetupDB(Statement statement) throws SQLException {
        try {
            String setupQuery = """
                CREATE DATABASE IF NOT EXISTS javalab;
                USE javalab;
                CREATE TABLE IF NOT EXISTS questions (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    question TEXT NOT NULL,
                    answers TEXT NOT NULL,
                    correct_answer TEXT NOT NULL);
                CREATE TABLE IF NOT EXISTS user_answers (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    username VARCHAR(255) NOT NULL,
                    question_id INT NOT NULL,
                    answer TEXT NOT NULL,
                    FOREIGN KEY (question_id) REFERENCES questions(id));
                                    INSERT INTO questions (question, answers, correct_answer) VALUES\s
                    ('Wiecej niz jedno zwierze to?', 'lama stado orzel tatra', 'lama'),
                    ('Bilbo Baggins wyrzuca na kosci 9...', 'nie zalezy moze tak', 'nie');    

                                                                        """;

            statement.executeUpdate(setupQuery);

        } catch (SQLException e) {
            System.out.print("Błąd przygotowania bazy lub tabel!");
        }
    }

//    public static void createDatabase(String dbName) throws SQLException {
//        lock.lock();
//            String createDbQuery = "CREATE DATABASE IF NOT EXISTS " + dbName;
//            statement.executeUpdate(createDbQuery);
//            System.out.println("Database " + dbName + " is ready.");
//        } catch (SQLException e) {
//            System.out.print("Błąd tworzenia bazy danych!");
//        } finally {
//            lock.unlock();
//        }
//    }
}
