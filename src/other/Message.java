package other;

import java.io.Serializable;
import java.util.Date;

public class Message implements Serializable,Comparable {
    private Date dateOfPublish;
    private User sender;
    private User receiver;
    private String text;

    public Message(Date dateOfPublish, User sender, User receiver, String text) {
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

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
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
        return ((Message) o).getDateOfPublish().compareTo(this.getDateOfPublish());
    }
}
