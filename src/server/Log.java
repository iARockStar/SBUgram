package server;

import other.Post;
import other.User;

import java.util.Date;

/**
 * this class is just for printing the events which happen during the server
 * runtime.
 */
public class Log {

    public static void login(String username) {
        System.out.println(username + " Login");
        System.out.println(new Date());
        System.out.println();
    }

    public static void signup(User user) {
        System.out.println(user.getUsername() + " Register " + user.getProfilePicAddress());
        System.out.println(new Date());
        System.out.println();

    }

    public static void getMyPosts(User user) {
        System.out.println(user.getUsername() + " get posts list");
        System.out.println(new Date());
        System.out.println();

    }


    public static void follow(User following, User follower) {
        System.out.println("Follow " + follower.getUsername());
        System.out.println("message : " + following.getUsername());
        System.out.println(new Date());
        System.out.println();

    }

    public static void unfollow(User following, User follower) {
        System.out.println("Unfollow " + follower.getUsername());
        System.out.println("message: " + following.getUsername());
        System.out.println(new Date());
        System.out.println();

    }

    public static void newPost(Post post, User user1) {
        System.out.println(user1.getUsername() + " Publish");
        System.out.println(
                post.getTitle() + " " + post.getPostPicAddress() + " " + user1.getName());
        System.out.println(new Date());
        System.out.println();

    }


    public static void search(User searched, User searcher) {
        try {
            System.out.println(searcher.getUsername() + " get info " + searched.getUsername());
            System.out.println("message: " +
                    searched.getUsername() + " " + searched.getProfilePicAddress());
            System.out.println(new Date());
            System.out.println();

        } catch (NullPointerException e) {
            System.out.println(searcher.getUsername() + " get info" + " no user was found");
            System.out.println("message: " + "no user was found");
            System.out.println(new Date());
            System.out.println();

        }
    }

    public static void mute(User muter, User muted) {
        System.out.println(muter.getUsername() + " Mute");
        System.out.println("message: " + muted.getUsername());
        System.out.println(new Date());
        System.out.println();

    }

    public static void unmute(User unmuter, User unmuted) {
        System.out.println(unmuter.getUsername() + " unMute");
        System.out.println("message: " + unmuted.getUsername());
        System.out.println(new Date());
        System.out.println();

    }

    public static void like(User liker, Post post) {
        System.out.println(liker.getUsername() + " Like");
        System.out.println(post.getOwner().getUsername() + " " + post.getTitle());
        System.out.println(new Date());
        System.out.println();

    }

    public static void rePost(User rePoster, Post post) {
        System.out.println(rePoster.getUsername() + " Repost");
        System.out.println(post.getOwner().getUsername() + " " + post.getTitle());
        System.out.println(new Date());
        System.out.println();

    }

    public static void setting(User updatedUser) {
        System.out.println(updatedUser.getUsername() + " Update Info");
        System.out.println("message: " + updatedUser.getProfilePicAddress());
        System.out.println(new Date());
        System.out.println();

    }

    public static void comment(User commenter, Post post) {
        System.out.println(commenter.getUsername() + " Comment");
        System.out.println("message: " + post.getTitle());
        System.out.println(new Date());
        System.out.println();

    }

    public static void deleteAcc(User deletedUser) {
        System.out.println(deletedUser.getUsername() + " Delete Account");
        System.out.println(new Date());
        System.out.println();

    }

    public static void logout(User loggedOut) {
        try {
            System.out.println(loggedOut.getUsername() + " " + "logout");
            System.out.println(new Date());
            System.out.println();
        } catch (NullPointerException e) {
            System.out.println("just closed the app" + " " + "logout");
            System.out.println(new Date());
            System.out.println();
        }
    }
}
