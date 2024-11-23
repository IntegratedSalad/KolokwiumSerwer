import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Arrays;

public class Message {

    private MessageType type;
    private byte[] payload;

    public Message(MessageType type, byte[] payload) {
        this.type = type;
        this.payload = payload;
    }

    public byte[] ConstructFullPayload() {
        int fullPayloadLength = 256;
        byte[] fullPayload = new byte[fullPayloadLength];
        if (this.payload == null) {
            fullPayload[0] = type.getByteType();
        } else {
            fullPayloadLength = this.payload.length + 1;
            fullPayload[0] = type.getByteType();
            System.arraycopy(payload, 0, fullPayload, 1, fullPayloadLength);
        }
        return fullPayload;
    }

    public void Send(PrintWriter out) {
        byte[] buffer = new byte[256];
        byte[] msgPayload = this.ConstructFullPayload();
        System.arraycopy(msgPayload, 0, buffer, 0, buffer.length);
        out.println(Arrays.toString(buffer)); // send
        out.flush();
    }
}
