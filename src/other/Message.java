package other;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;


/**
 * the item for every chat with another user
 */
public class Message implements Serializable,Comparable {
    private Date dateOfPublish;
    private String sender;
    private String receiver;
    private String text;
    private boolean deleted = false;

    public Message(Date dateOfPublish, String sender, String receiver, String text) {
        this.dateOfPublish = dateOfPublish;
        this.sender = sender;
        this.receiver = receiver;
        this.text = text;
    }

    public Date getDateOfPublish() {
        return dateOfPublish;
    }

    public void setDateOfPublish(Date dateOfPublish) {
        this.dateOfPublish = dateOfPublish;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public int compareTo(Object o) {
        return -1 * ((Message) o).getDateOfPublish().compareTo(this.getDateOfPublish());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return Objects.equals(dateOfPublish, message.dateOfPublish) && Objects.equals(sender, message.sender) && Objects.equals(receiver, message.receiver) && Objects.equals(text, message.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dateOfPublish, sender, receiver, text);
    }


    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
