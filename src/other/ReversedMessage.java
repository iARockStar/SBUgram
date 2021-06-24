package other;

import java.util.Date;

public class ReversedMessage extends Message{
    public ReversedMessage(Date dateOfPublish, String sender, String receiver, String text) {
        super(dateOfPublish, sender, receiver, text);
    }
}
