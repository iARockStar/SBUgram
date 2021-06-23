package other;

import java.util.Date;

public class ReversedMessage extends Message{
    public ReversedMessage(Date dateOfPublish, User sender, User receiver, String text) {
        super(dateOfPublish, sender, receiver, text);
    }
}
