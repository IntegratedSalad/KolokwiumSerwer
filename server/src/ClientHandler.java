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

//            byte[] buffer = new byte[256];
//            PrintWriter out = new PrintWriter(new OutputStreamWriter(sock.getOutputStream()));
//            Message msgHello = new Message(MessageType.MSG_HELLO, null);
//            byte[] msgPayload = msgHello.ConstructFullPayload();
//            System.arraycopy(msgPayload, 0, buffer, 0, buffer.length);
//            out.println(Arrays.toString(buffer)); // send

            PrintWriter out = new PrintWriter(new OutputStreamWriter(sock.getOutputStream()));
            Message msgHello = new Message(MessageType.MSG_HELLO, null);
            msgHello.Send(out);
//            out.flush();
            System.out.println("Hello sent!");

            while (true) {

            }
            // while -> wait for response

            // Client closes the socket...
            // Listen to messages sent by the client



        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
