import java.io.*;
import java.net.Socket;
import java.util.Arrays;
import java.util.concurrent.*;

public class ClientHandler implements Runnable {

    private Socket sock;
    public ClientHandler(Socket socket) {
        this.sock = socket;
    }
    private OutputStream outputStream;
    private InputStream inputStream;

    @Override
    public void run() {
        try {

            PrintWriter out = new PrintWriter(new OutputStreamWriter(sock.getOutputStream()));
            Message msgHello = new Message(MessageType.MSG_HELLO, null);
            msgHello.SetPayload("dawaj kurwo weed");
            msgHello.Send(out);
            System.out.println("Hello sent!");

            while (true) {
                // 1. Send question
                // 2. Get Answer
                // 3. Write Answer to file
                // loop

            }
            // while -> wait for response

            // Client closes the socket...
            // Listen to messages sent by the client


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
