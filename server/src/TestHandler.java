import java.io.FileNotFoundException;
import java.net.Socket;
import java.sql.SQLException;
import java.util.concurrent.locks.ReentrantLock;

public class TestHandler implements Runnable {

    private ConcurrentFileHandler concurrentFileHandler;
    private ReentrantLock rwLock;

    public TestHandler(ReentrantLock rwLock) throws FileNotFoundException, SQLException {
        this.rwLock = rwLock;
        this.concurrentFileHandler = new ConcurrentFileHandler(rwLock);
    }

    @Override
    public void run() {

//        this.co

    }
}
