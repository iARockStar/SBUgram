package client;
import other.*;

/**
 * This class holds two static fields.
 * the user which we are using and the user which we search and follow in
 * the search section.
 */
public class thisUser {
    /**
     * the user whom he/she logged in.
     */
    private static User user;

    /**
     * the user whom we searched in the search section.
     */
    private static User searchedUser;
    private static String searchedUserName;

    //getters and setters
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

    public static String getSearchedUserName() {
        return searchedUserName;
    }

    public static void setSearchedUserName(String searchedUserName) {
        thisUser.searchedUserName = searchedUserName;
    }
}

