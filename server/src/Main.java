import java.io.IOException;
import java.net.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;

public class Main {
    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(250);
        ReentrantLock rwLock = new ReentrantLock();

        // Test...

        try {
            ServerSocket serverSocket = new ServerSocket(2137);

            System.out.println("Waiting for connection...");
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Accepted connection from " + socket.getInetAddress());
                executor.execute(new ClientHandler(socket, rwLock));
                // Client connects... :D
            }

            // Each client receives MSG_HELLO
            // and has to decode it and print to the output.
            // Client holds the connection until he sends message "MSG_USER_QUIZ_ABANDONED"

        } catch (IOException e) {
            System.out.println("IO Error while creating server socket: " + e.getMessage() + "!");
            System.exit(1);
        }
    }

    public static void Test(ExecutorService ex, ReentrantLock rwlock) {


    }
}