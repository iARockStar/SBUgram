package other;

import java.util.Date;

/**
 * this class is for creating a message which contains a pic ,too.
 */
public class PicMessage extends Message {
    byte[] pic;

    public PicMessage(Date dateOfPublish
            , String sender
            , String receiver
            , String text
            , byte[] pic) {
        super(dateOfPublish, sender, receiver, text);
        this.pic = pic;
    }

    public byte[] getPic() {
        return pic;
    }

    public void setPic(byte[] pic) {
        this.pic = pic;
    }
}
