package other;

import java.util.Date;
/**
 * this class is for creating a message which contains a pic ,too
 * (but for the addressed user).
 */
public class ReversedPicMessage extends PicMessage {
    public ReversedPicMessage
            (
                    Date dateOfPublish
                    , String sender
                    , String receiver
                    , String text
                    , byte[] pic
            ) {
        super(dateOfPublish, sender, receiver, text, pic);
    }
}
