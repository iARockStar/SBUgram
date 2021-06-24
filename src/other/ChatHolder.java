package other;

import java.util.Vector;

/**
 * this class holds two lists for every chat:
 *  one for the sent messages and one for received messages
 */
public class ChatHolder {
    Vector<Message> sentMessages;
    Vector<Message> received;

    public ChatHolder(Vector<Message> sent, Vector<Message> received) {
        this.sentMessages = sent;
        this.received = received;
    }

    public Vector<Message> getSentMessages() {
        return sentMessages;
    }

    public void setSentMessages(Vector<Message> sent) {
        this.sentMessages = sent;
    }

    public Vector<Message> getReceived() {
        return received;
    }

    public void setReceived(Vector<Message> received) {
        this.received = received;
    }
}
