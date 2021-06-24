package other;

import java.util.Date;

/**
 * the graphic which we see when we receive a message.
 * it's still a message though.
 */
public class ReversedMessage extends Message{
    public ReversedMessage(Date dateOfPublish, String sender, String receiver, String text) {
        super(dateOfPublish, sender, receiver, text);
    }
}
