import java.io.FileNotFoundException;
import java.net.Socket;
import java.util.concurrent.locks.ReentrantLock;

public class TestHandler implements Runnable {

    private ConcurrentFileHandler concurrentFileHandler;
    private ReentrantLock rwLock;

    public TestHandler(ReentrantLock rwLock) throws FileNotFoundException {
        this.rwLock = rwLock;
        this.concurrentFileHandler = new ConcurrentFileHandler("src/bazaPytan.txt",
                "src/bazaOdpowiedzi.txt", rwLock);
    }


    @Override
    public void run() {

//        this.co

    }
}
