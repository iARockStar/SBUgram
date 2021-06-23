package other;

import java.io.Serializable;
import java.util.Date;

public class UserList implements Serializable {
    private String myUser;
    private String addressed;
    private Date date;
    private int numOfChats = 0;

    public UserList(String myUser, String addressed, Date date) {
        this.myUser = myUser;
        this.addressed = addressed;
        this.date = date;
    }

    public UserList() {
    }

    public int getNumOfChats() {
        return numOfChats;
    }

    public void addNumOfChats() {
        this.numOfChats = ++this.numOfChats;
        this.date = new Date();
    }

    public String getMyUser() {
        return myUser;
    }

    public void setMyUser(String myUser) {
        this.myUser = myUser;
    }

    public String getAddressed() {
        return addressed;
    }

    public void setAddressed(String addressed) {
        this.addressed = addressed;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
