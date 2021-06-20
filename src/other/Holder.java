package other;

import java.io.Serializable;

public class Holder implements Serializable {
    private User user;
    private static Holder holder = new Holder();

    private Holder() {
    }

    public static Holder getInstance(){
        return holder;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
