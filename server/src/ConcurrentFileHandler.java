import lombok.Getter;
import lombok.Setter;

import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.locks.ReentrantLock;

@Getter
@Setter
public class ConcurrentFileHandler {

    Connection connection;
    Statement statement;

    private BufferedReader br = null;
    private BufferedWriter bw = null;
    private ReentrantLock rwLock;

//    String filenameIn = null;
//    String filenameOut = null;

    ConcurrentFileHandler(ReentrantLock lock) throws FileNotFoundException, SQLException {
        this.connection = DbConnection.connection;
        this.statement = DbConnection.createStatement(this.connection);
        this.rwLock = lock;
    }

    public void executeStatement(String query) throws SQLException {
        try {
            this.statement.executeQuery(query);
        } catch (SQLException e) {
            System.out.print("Błąd w readStatement!");
        }
    }
    public void updateStatement(String query) throws SQLException {
        try {
            this.statement.executeUpdate(query);
        } catch (SQLException e) {
            System.out.print("Błąd w updateStatement!");
        }
    }

    public String[] readNLines(final int nlines, final int skip) throws IOException {
        this.rwLock.lock();
        this.br = new BufferedReader(new FileReader(this.filenameIn)); // TODO: maybe instantiate once
        try {
            String[] linesToReturn = new String[nlines];
            for (int i = 0; i < skip; i++) {this.br.readLine();} // skip lines
            for (int i = 0; i < nlines; i++) {
                linesToReturn[i] = this.br.readLine();
            }
            return linesToReturn;
        } finally {
            this.br.close();
            this.rwLock.unlock();
            this.br = null;
        }
    }

    public void writeLine(final String line) throws IOException {
        this.rwLock.lock();
        this.bw = new BufferedWriter(new FileWriter(this.filenameOut, true));
        try {
            this.bw.write(line);
            this.bw.newLine();
            this.bw.flush();
        } finally {
            this.bw.close();
            this.rwLock.unlock();
            this.bw = null;
        }
    }
}
