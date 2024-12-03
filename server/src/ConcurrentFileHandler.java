import java.io.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.locks.ReentrantLock;

public class ConcurrentFileHandler {

    private Connection connection;
    private Statement statement;

    private BufferedReader br = null;
    private BufferedWriter bw = null;
    private ReentrantLock rwLock;

    String filenameIn = null;
    String filenameOut = null;

    ConcurrentFileHandler(String filenameIn, String filenameOut, ReentrantLock lock) throws SQLException {
        this.connection = DbConnection.connect();
        this.statement = DbConnection.createStatement(this.connection);
        this.filenameIn = filenameIn;
        this.filenameOut = filenameOut;
        this.rwLock = lock;
    }

    public String[] readfromDB(int id) throws SQLException {
        String query = "SELECT id, question, answers, correct_answer FROM questions WHERE id = " + id;
        String[] resultTable = new String[2];
        try (ResultSet result = DbConnection.executeStatement(query, this.statement)) {
            if (result.next()) {
                resultTable[0] = result.getInt("id") + ". " + result.getString("question") + "ANS: " +
                        result.getString("correct_answer");
                resultTable[1] = result.getString("answers"); // Poprawione na `answers`, jeśli to lista odpowiedzi
            } else {
                System.out.println("No results found for id: " + id);
            }
        }
        return resultTable;
    }
    public void savetoDB(String student, int id, String studentAnswer) throws SQLException {
        String query = """
                     INSERT INTO user_answers (username, question_id, answer)
                     VALUES ('%s', '%d', '%s');""".formatted(student, id, studentAnswer);
        DbConnection.updateStatement(query, this.statement);
    }
}