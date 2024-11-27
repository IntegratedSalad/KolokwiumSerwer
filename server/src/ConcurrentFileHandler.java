import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Lock;

public class ConcurrentFileHandler {

    private BufferedReader br = null;
    private BufferedWriter bw = null;
    private StringBuilder sb = null;
    private ReentrantLock rwLock;

    String filename = null;

    ConcurrentFileHandler(final String filename, ReentrantLock lock) throws FileNotFoundException {
        this.filename = filename;
        this.rwLock = lock;
//        try {
//            br = new BufferedReader(new FileReader(filename));
//            sb = new StringBuilder();
//
//            // 0. Create a buffered reader (open file)
//            // 1. Lock file
//            // 2. Read n lines (skip n lines if needed)
//            // 3. Unlock file
//            // 4. Close file
//
//        }  catch (FileNotFoundException e) {
//            System.out.println("File not found: " + filename);
//            System.exit(1);
//        }
    }

    public String[] readNLines(final int nlines, final int skip) throws IOException {
        this.rwLock.lock();
        this.br = new BufferedReader(new FileReader(this.filename));
        try {
            String[] linesToReturn = new String[nlines];
            if (skip > 0) {
                br.readLine();
            }
            for (int i = 0; i < nlines; i++) {
                linesToReturn[i] = br.readLine();
            }
            return linesToReturn;
        } finally {
            br.close();
            this.rwLock.unlock();
            this.br = null;
        }
    }

    public void writeLine(final String line) throws IOException {
        this.rwLock.lock();
        bw = new BufferedWriter(new FileWriter(this.filename, true));
        try {
            bw.write(line);
        } finally {
            bw.close();
            this.rwLock.unlock();
            this.bw = null;
        }
    }

    // Write
    // We have to test >100 threads!!!
}
