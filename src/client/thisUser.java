package client;
import other.*;

public class thisUser {
    private static User user;

    private static User searchedUser;
    private static boolean isAnotherUser = false;

    public static boolean isAnotherUser() {
        return isAnotherUser;
    }

    public static void setIsAnotherUser(boolean isAnotherUser) {
        thisUser.isAnotherUser = isAnotherUser;
    }

    public static User getUser() {
        return user;
    }

    public static void setUser(User user) {
        thisUser.user = user;
    }

    public static User getSearchedUser() {
        return searchedUser;
    }

    public static void setSearchedUser(User searchedUser) {

        thisUser.searchedUser = searchedUser;
    }
}

