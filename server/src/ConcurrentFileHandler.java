import java.io.*;
import java.util.concurrent.locks.ReentrantLock;

public class ConcurrentFileHandler {

    private BufferedReader br = null;
    private BufferedWriter bw = null;
    private StringBuilder sb = null;
    private ReentrantLock rwLock;

    String filenameIn = null;
    String filenameOut = null;

    ConcurrentFileHandler(final String filenameIn, final String filenameOut, ReentrantLock lock) throws FileNotFoundException {
        this.filenameIn = filenameIn;
        this.filenameOut = filenameOut;
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

    // Write
    // We have to test >100 threads!!!
}
