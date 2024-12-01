import java.io.PrintWriter;
import java.util.Arrays;

public class Message {

    private MessageType type;
    private String payload;

    public Message(MessageType type, String payload) {
        this.type = type;
        this.payload = payload;
    }

    public String ConstructFullPayload() {
        String fullPayload = "";
        if (this.payload == null) {
            fullPayload += type.getTypeId();
        } else {
            fullPayload += type.getTypeId();
            fullPayload += this.payload;
        }
        return fullPayload;
    }

    public void Send(PrintWriter out) {
        String msgPayload = this.ConstructFullPayload();
        out.println(msgPayload); // send
        out.flush();
    }

    public void SetPayload(String payload) {
        this.payload = payload;
    }
}
