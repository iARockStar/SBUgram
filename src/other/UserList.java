package other;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * a class for giving graphic to the list of users whom the main user has chatted with.
 */
public class UserList implements Serializable,Comparable {
    private String myUser;
    private String addressed;
    private Date date;
    private int numOfChats = 0;
    private int numOfUnSeen = 0;
    private boolean seen = false;
    private boolean blockedEachOther = false;

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

    public void reduceNumOfChats() {
        this.numOfChats = --this.numOfChats;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserList userList = (UserList) o;
        return Objects.equals(myUser, userList.myUser) && Objects.equals(addressed, userList.addressed) ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(myUser, addressed);
    }

    @Override
    public int compareTo(Object o) {
        return ((UserList) o).getDate().compareTo(this.getDate());
    }

    public void restartNumOfUnSeen() {
        this.numOfUnSeen = 0;
    }

    public void addNumOfUnSeen() {
        this.numOfUnSeen = ++numOfUnSeen;
    }
    public void reduceNumOfUnSeen() {
        this.numOfUnSeen = --numOfUnSeen;
    }

    public int getNumOfUnSeen() {
        return numOfUnSeen;
    }

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }

    public boolean isBlockedEachOther() {
        return blockedEachOther;
    }

    public void setBlockedEachOther(boolean blockedEachOther) {
        this.blockedEachOther = blockedEachOther;
    }
}
