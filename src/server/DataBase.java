package server;

import other.User;

import java.io.*;

import other.*;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * this is the class where everything is saved and sent to the client side
 * in order to synchronize the client with the server.
 * some methods are only updates and some are for returning a value
 * which is likely to be a list.
 * (the lists and the variables are also thread - safe).
 */
public class DataBase {

    /**
     * the file which every user is saved using a vector.
     */
    private static final String USERS_FILE
            = "C:\\Users\\USER\\IdeaProjects\\signUp.bin";

    /**
     * posts didn't have anything unique so i made a file where
     * the number of posts created are saved as an id so the next time
     * the server is up it reads the latest not uses id from this file.
     */
    private static final String POST_ID_FILE
            = "C:\\Users\\USER\\IdeaProjects\\postId.bin";

    /**
     * ds which i used for saving the info of the users
     */
    private static Vector<User> listOfUsers
            = new Vector<>();

    /**
     * the idCounter for posts are saved as an atomic integer for
     * multithreading problems
     */
    private static AtomicInteger idCounter =
            new AtomicInteger(0);


    /**
     * this method reads the db from the file and creates the db
     * file if it's not created.
     * it also reads the id of the posts from the file.
     */
    public synchronized static void initializeServer() {
        File file;
        file = new File(USERS_FILE);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
        try {
            FileInputStream fin = new FileInputStream(USERS_FILE);
            ObjectInputStream inFromFile = new ObjectInputStream(fin);
            listOfUsers = (Vector<User>) inFromFile.readObject();
            inFromFile.close();
            fin.close();
        } catch (EOFException | StreamCorruptedException e) {
            listOfUsers = new Vector<>();
        } catch (Exception e) {
            e.printStackTrace();
        }

        File file2;
        file2 = new File(POST_ID_FILE);
        if (!file2.exists()) {
            try {
                file2.createNewFile();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
        try {
            FileInputStream fin = new FileInputStream(POST_ID_FILE);
            ObjectInputStream inFromFile = new ObjectInputStream(fin);
            idCounter = (AtomicInteger) inFromFile.readObject();
            Post.setIdCounter(idCounter);
            inFromFile.close();
            fin.close();
        } catch (EOFException | StreamCorruptedException e) {
            listOfUsers = new Vector<>();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    /**
     * this method searches the list of users in order to check
     * if the username and password entered for logging in is valid.
     * if the info is not valid then it sends notApproved message to the client.
     * it it is then it sends the user who wants to sign in with to the client
     *
     * @param objectOutputStream the stream needed to write objects
     * @param username           username of the user
     * @param password           password of the user.
     */
    public synchronized static void login(ObjectOutputStream objectOutputStream
            , String username, String password) throws IOException, ClassNotFoundException {
        ObjectInputStream objectInputStream1 = null;
        Vector<User> users = new Vector<>();
        try {
            FileInputStream fileInputStream = new FileInputStream(USERS_FILE);
            objectInputStream1 = new ObjectInputStream(fileInputStream);
            users = (Vector<User>) objectInputStream1.readObject();
        } catch (EOFException e) {
            objectOutputStream.writeObject(ApprovedType.NOT_APPROVED);
            objectOutputStream.flush();
            return;
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (User user :
                users) {
            boolean isValid = user.getUsername().equalsIgnoreCase(username)
                    && user.getPassword().equals(password);
            if (isValid) {
                objectOutputStream.writeObject(user);
                objectOutputStream.flush();
                objectInputStream1.close();
                return;
            }
        }

        try {
            objectOutputStream.writeObject(ApprovedType.NOT_APPROVED);
            objectOutputStream.flush();
            assert objectInputStream1 != null;
            objectInputStream1.close();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }


    /**
     * this method signs up a new user and saves it into the db
     * the only thing which is not checked in the client is that if the
     * username is already used or not which is checked here
     *
     * @param user the new user which we are trying to save in the db.
     * @return the return type declares if the signUp process was successful
     * or not.
     */
    public static ApprovedType signupUpdate(User user) {
        if (!listOfUsers.contains(user)) {
            listOfUsers.add(user);
            updateUser();
            return ApprovedType.APPROVED;
        }
        return ApprovedType.NOT_APPROVED;

    }

    /**
     * this method is called whenever the db update is needed.
     * it writes the new list to the file.
     * it also sorts the posts in the db by their date.
     */
    private static void updateUser() {
        sortPosts();
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(USERS_FILE, false);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(listOfUsers);
            objectOutputStream.flush();
            fileOutputStream.close();


            FileOutputStream fileOutputStream1 = new FileOutputStream(POST_ID_FILE, false);
            ObjectOutputStream objectOutputStream1 = new ObjectOutputStream(fileOutputStream1);
            objectOutputStream1.writeObject(Post.getIdCounter());
            objectOutputStream1.flush();
            fileOutputStream1.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * method for sorting the posts
     */
    private static void sortPosts() {
        for (User user :
                listOfUsers) {
            Collections.sort(user.getListOfPosts());
        }
    }

    /**
     * this method adds a new post to the list of the posts of a user.
     *
     * @param post the new post added to the list
     * @param user owner of the post
     */
    public static void updatePost(Post post, String user) {
        for (User listOfUser : listOfUsers) {
            if (user.equals(listOfUser.getUsername())) {
                post.setPostId();
                listOfUser.addPost(post);
                break;
            }
        }
        updateUser();
    }


    /**
     * this method loads the posts need for each page therefore
     * it fins the owner of the posts and returns it to the client.
     *
     * @param requester this param is needed for checking if the user is blocked or not
     * @param requested the user whom se want to load his / her posts.
     * @return the value is a list of posts of the user.
     */
    public static User loadPost(User requester, User requested) {
        User foundUser = null;
        for (User listUser :
                listOfUsers) {
            if (requested.getUsername().equals(listUser.getUsername())) {
                if (!requested.getBlockedList().contains(requester.getUsername()))
                    foundUser = listUser;
            }
        }
        return foundUser;
    }

    /**
     * method for retrieving the password of a user
     */
    public static User retrievePass(User user) {
        for (User listUser :
                listOfUsers) {
            if (user.getUsername().equalsIgnoreCase(listUser.getUsername()))
                return listUser;
        }
        return null;
    }

    /**
     * this method checks if the answer to the security question was
     * valid or not. if yes then the new pass is saved.
     * otherwise nothing happens.
     *
     * @param user the user who is searching for his / her password.
     * @return its false or true according to the answer of the
     * user to the security question.
     */
    public static boolean saveNewPass(User user) {
        for (User listUser :
                listOfUsers) {
            if (user.getUsername().equals(listUser.getUsername())) {
                if (user.getSecurityQuestion().equals(listUser.getSecurityQuestion())) {
                    listUser.setPassword(user.getPassword());
                    updateUser();
                    return true;
                } else return false;
            }
        }
        return false;
    }

    /**
     * this method is for the search page controller which gives a username
     * and sends the user of it to the client.
     *
     * @param username the name of the searched user.
     * @return return value is null if the user is not found in the list
     * and a user if the username is found.
     */
    public static User search(String username) {
        for (User listUser :
                listOfUsers) {
            if (listUser.getUsername().equalsIgnoreCase(username))
                return listUser;
        }
        return null;
    }

    /**
     * this method adds a comment to the list of comments and then
     * sends the list of the comments back to the client
     *
     * @param user    the user whom posted the comment.
     * @param post    the post the user is commenting on
     * @param comment the comment the user committed
     * @return a list which contains the comments of the post.
     */
    public static Vector<Comment> addAndSendComments(String user, Post post, Comment comment) {
        Vector<Comment> commentList = new Vector<>();
        for (User listUser :
                listOfUsers) {
            if (user.equals(listUser.getUsername())) {
                for (Post listUserPost : listUser.getListOfPosts()) {
                    if (listUserPost.equals(post)) {
                        listUserPost.getComments().add(comment);
                        commentList = listUserPost.getComments();
                    }
                }
                updateUser();
                return commentList;
            }
        }
        return null;
    }

    /**
     * this method sends the list of comments of a post
     *
     * @param user the owner of the post which has the list of comments
     * @param post the post which we want its comments
     * @return the list of comments
     */
    public static Vector<Comment> sendComments(String user, Post post) {
        for (User listUser :
                listOfUsers) {
            if (user.equals(listUser.getUsername())) {
                for (Post listUserPost : listUser.getListOfPosts()) {
                    if (listUserPost.equals(post))
                        return listUserPost.getComments();
                }
            }
        }
        return null;
    }

    /**
     * this method adds the new follower by adding the username of the follower
     * to list of the following's followers and adding the username of the
     * following to the list of the follower's followings.
     *
     * @param following the user who is being followed
     * @param follower  the follower
     * @return return value is the num of the followers of the followed user.
     * the return value is -1 if the follower is blocked by the following.
     */
    public static AtomicInteger follow(User following, User follower) {
        AtomicInteger atomicInteger = new AtomicInteger(0);
        for (User listUser :
                listOfUsers) {
            if (following.getUsername().equals(listUser.getUsername())) {
                if (!following.getBlockedList().contains(follower.getUsername())) {
                    listUser.addFollower(follower.getUsername());
                    atomicInteger = listUser.getNumOfFollowers();
                } else {
                    atomicInteger = new AtomicInteger(-1);
                }
                break;
            }
        }
        if (atomicInteger.intValue() != -1) {
            for (User listUser :
                    listOfUsers) {
                if (follower.getUsername().equals(listUser.getUsername())) {
                    listUser.addFollowing(following.getUsername());
                }
            }
        }
        updateUser();
        return atomicInteger;
    }

    /**
     * the same method as the follow method but reversed.
     *
     * @param unFollowing the user who is being unfollowed
     * @param unFollower  the unFollower.
     * @return return value is the new num of followers
     */
    public static AtomicInteger unfollow(User unFollowing, User unFollower) {
        AtomicInteger atomicInteger = new AtomicInteger(0);
        for (User listUser :
                listOfUsers) {
            if (unFollowing.getUsername().equals(listUser.getUsername())) {
                listUser.removeFollower(unFollower.getUsername());
                atomicInteger = listUser.getNumOfFollowers();
                break;
            }
        }
        for (User listUser :
                listOfUsers) {
            if (unFollower.getUsername().equals(listUser.getUsername())) {
                listUser.removeFollowing(unFollowing.getUsername());
            }
        }
        updateUser();
        return atomicInteger;
    }

    /**
     * this method is exclusively for the timeline feed and returns the posts
     * of the users whom the user followed
     *
     * @param user the user who wants his / her list of followings' posts.
     * @return a list which contains the list of the followings' post.
     */
    public static Vector<Post> loadFollowingPosts(User user) {
        Vector<Post> posts = new Vector<>();
        User user2 = null;
        for (User listUser :
                listOfUsers) {
            if (user.getUsername().equalsIgnoreCase(listUser.getUsername())) {
                user2 = listUser;
                break;
            }
        }
        for (User listUser : listOfUsers) {
            if (user2.getFollowings().contains(listUser.getUsername()))
                if (!user2.getMutedList().contains(listUser.getUsername())
                        && !listUser.getBlockedList().contains(user2.getUsername())
                        && !user2.getBlockedList().contains(listUser.getUsername()))
                    posts.addAll(listUser.getListOfPosts());
        }
        Collections.sort(posts);
        return posts;
    }

    /**
     * this method is user for adding the number of likes to a post
     * but the problem is the reposted posts which their like numbers
     * should be added so we find those in a loop,too.
     *
     * @param user the user who wants to like the post
     * @param post the post which is being liked.
     */
    public static void like(User user, Post post) {
        for (User listUser : listOfUsers) {
            if (user.getUsername().equalsIgnoreCase(listUser.getUsername())) {
                post.getNumOfLikes().addAndGet(1);
                listUser.addLikedPost(post.getPostId());
                post.addToLikers(listUser.getUsername());
            }
        }
        for (User listUser :
                listOfUsers) {
            for (Post listPost :
                    listUser.getListOfPosts()) {
                if (listPost.equals(post) && !listPost.getLikers().contains(user.getUsername())) {
                    listPost.getNumOfLikes().addAndGet(1);
                    listPost.addToLikers(user.getUsername());
                }
            }
        }
        updateUser();
    }

    /**
     * same as the like method but reversed
     *
     * @param user the user who wants to take back his like
     * @param post the target post which its likes is bout to be reduced.
     */
    public static void dislike(User user, Post post) {
        for (User listUser : listOfUsers) {
            if (user.getUsername().equalsIgnoreCase(listUser.getUsername())) {
                post.getNumOfLikes().addAndGet(-1);
                listUser.removeLikedPost(post.getPostId());
                post.removeFromLikers(listUser.getUsername());
            }
        }
        for (User listUser :
                listOfUsers) {
            for (Post listPost :
                    listUser.getListOfPosts()) {
                if (listPost.equals(post) && listPost.getLikers().contains(user.getUsername())) {
                    listPost.getNumOfLikes().addAndGet(-1);
                    listPost.removeFromLikers(user.getUsername());
                }
            }
        }
        updateUser();
    }

    /**
     * don't remember what the hell this is :|.
     */
    public static User updateUser(String username) {
        for (User user :
                listOfUsers) {
            if (user.getUsername().equalsIgnoreCase(username))
                return user;
        }
        return null;
    }

    /**
     * this method is the worst method of my project but at least im proud i did it^_^.
     * well it receives the user who wants to repost and the target post.
     * now the num of reposts of the post is added and
     * the user is added to the reposters of the post.
     * so the next time the user wants to repost the
     * post the db checks if the user has already reposted
     * the post or not. it also checks if the user doesn't want to
     * repost his / her own post.
     * in the end it searches the likes of the post in other places and
     * replaces the new repost(with more num of reposts) in the whole db.
     *
     * @param user the user who wants to repost
     * @param post the target post to repost.
     * @return return value is like a true or
     * false which shows the success in the process.
     */
    public static ApprovedType repost(User user, Post post) {
        for (User listUser :
                listOfUsers) {
            if (user.getUsername().equalsIgnoreCase(listUser.getUsername()) &&
                    !listUser.getListOfPosts().contains(post)) {
                post.getNumOfReposts().addAndGet(1);
                post.addToRepsters(user.getUsername());
                listUser.getListOfPosts().add(post);
                for (int i = listOfUsers.size() - 1; i >= 0; i--) {
                    if (listOfUsers.get(i).getListOfPosts().contains(post)) {
                        int indexOfPost =
                                listOfUsers.get(i).getListOfPosts().indexOf(post);
                        listOfUsers.get(i).getListOfPosts().set(indexOfPost, post);
                    }
                }
                updateUser();
                return ApprovedType.APPROVED;
            }
        }
        return ApprovedType.NOT_APPROVED;
    }

    /**
     * this method receives a user and updates its information in the whole
     * db.(comments,posts,...).
     *
     * @param user the user who is updated.
     */
    public static void settingUpdate(User user) {
        for (int i = 0; i < listOfUsers.size(); i++) {
            if (listOfUsers.get(i).getUsername().equalsIgnoreCase(user.getUsername())) {
                listOfUsers.remove(i);
                listOfUsers.add(i, user);
                listOfUsers.get(i).setPassword(user.getPassword());
                for (Post post : listOfUsers.get(i).getListOfPosts()) {
                    if (post.getOwner()
                            .equalsIgnoreCase(listOfUsers.get(i).getUsername())) {
                        for (Comment comment :
                                post.getComments()) {
                            if (comment.getOwner()
                                    .equalsIgnoreCase(listOfUsers.get(i).getUsername())) {
                                comment.setOwner(user.getUsername());
                                comment.setProPic(user.getProfileImage());
                            }
                        }
                        post.setProfilePic(user.getProfileImage());
                    }
                }
            } else {
                for (Post post : listOfUsers.get(i).getListOfPosts()) {
                    for (Comment comment :
                            post.getComments()) {
                        if (comment.getOwner()
                                .equalsIgnoreCase(listOfUsers.get(i).getUsername())) {
                            comment.setOwner(user.getUsername());
                            comment.setProPic(user.getProfileImage());
                        }
                    }
                    if (post.getOwner()
                            .equalsIgnoreCase(user.getUsername())) {
                        post.setProfilePic(user.getProfileImage());
                    }
                }
            }
        }
        updateUser();
    }

    /**
     * this method is for deleting accounts and consists of deleting
     * different parts which are related to the deleted user.
     *
     * @param deletedUser is the user whom the user tends to delete.
     */
    public static void deleteAccount(User deletedUser) {
        deleteLikes(deletedUser);
        deleteFollows(deletedUser);
        deleteReposts(deletedUser);
        deleteChats(deletedUser);
        deleteAcc(deletedUser);
        for (User listUser :
                listOfUsers) {
            for (Post post :
                    listUser.getListOfPosts()) {
                post.getComments().removeIf(
                        comment -> comment.getOwner().equals(deletedUser.getUsername())
                );
            }
        }
        updateUser();
    }

    /**
     * method for deleting the chats the deletedUser did
     *
     * @param deletedUser the user who is out of the db.
     */
    private static void deleteChats(User deletedUser) {
        for (User listUser :
                listOfUsers) {
            listUser.getUsers()
                    .removeIf(userList -> userList.getAddressed()
                            .equalsIgnoreCase(deletedUser.getUsername()));
            listUser.getUsers()
                    .removeIf(userList -> userList.getMyUser()
                            .equalsIgnoreCase(deletedUser.getUsername()));
            listUser.getReceived().remove(deletedUser.getUsername());
            listUser.getSent().remove(deletedUser.getUsername());
        }
        updateUser();
    }

    /**
     * method for deleting the reposts the deletedUser did
     *
     * @param deletedUser the user who is out of the db.
     */
    private static void deleteReposts(User deletedUser) {
        for (User listUser :
                listOfUsers) {
            for (Post post :
                    listUser.getListOfPosts()) {
                if (post.getReposters().contains(deletedUser.getUsername())) {
                    post.removeFromReposters(deletedUser);
                    post.getNumOfReposts().addAndGet(-1);
                }
            }
        }
    }

    /**
     * in this method the deleted user is out of the main list of users.
     *
     * @param deletedUser the user who is out of the db.
     */
    private static void deleteAcc(User deletedUser) {
        for (User listUser :
                listOfUsers) {
            if (deletedUser.getUsername().equalsIgnoreCase(listUser.getUsername())) {
                listOfUsers.remove(listUser);
                break;
            }
        }
    }


    /**
     * method for deleting the follows the deletedUser did
     *
     * @param deletedUser the user who is out of the db.
     */
    private static void deleteFollows(User deletedUser) {
        for (User listUser :
                listOfUsers) {
            if (listUser.getFollowers().contains(deletedUser.getUsername())) {
                listUser.getFollowers().remove(deletedUser.getUsername());
                listUser.getNumOfFollowers().addAndGet(-1);
            }

            if (listUser.getFollowings().contains(deletedUser.getUsername())) {
                listUser.getFollowings().remove(deletedUser.getUsername());
                listUser.getNumOfFollowings().addAndGet(-1);
            }
        }
    }

    /**
     * method for deleting the likes the deletedUser did
     *
     * @param deletedUser the user who is out of the db.
     */
    private static void deleteLikes(User deletedUser) {
        for (User listUser :
                listOfUsers) {
            for (Post post :
                    listUser.getListOfPosts()) {
                if (post.getLikers().contains(deletedUser.getUsername())) {
                    post.removeFromLikers(deletedUser.getUsername());
                    post.getNumOfLikes().addAndGet(-1);
                }
            }
        }
    }

    /**
     * the method for muting a user
     */
    public static void mute(User muter, User muted) {
        for (User listUser :
                listOfUsers) {
            if (listUser.getUsername().equals(muter.getUsername())) {
                listUser.addMuted(muted.getUsername());
                break;
            }
        }
        updateUser();
    }

    /**
     * the method for unMuting a user
     */
    public static void unMute(User unMuter, User unMuted) {
        for (User listUser :
                listOfUsers) {
            if (listUser.getUsername().equals(unMuter.getUsername())) {
                listUser.removeMuted(unMuted);
                break;
            }
        }
        updateUser();
    }

    /**
     * the method for blocking a user.
     * if you block a user then you are no longer his / her follower
     * so the num of the followers of the blocked user reduces.
     */
    public static void block(User blocker, User blocked) {
        for (User listUser :
                listOfUsers) {
            if (listUser.getUsername().equals(blocker.getUsername())) {
                if (blocked.getFollowings().contains(listUser.getUsername())) {
                    listUser.removeFollower(blocked.getUsername());
                }
                for (UserList list :
                        listUser.getUsers()) {
                    if (list.getAddressed().equalsIgnoreCase(blocked.getUsername())) {
                        list.setBlockedEachOther(true);
                        break;
                    }
                }
                listUser.addToBlockedList(blocked.getUsername());
                break;
            }
        }
        for (User listUser :
                listOfUsers) {
            if (blocked.getUsername().equalsIgnoreCase(listUser.getUsername())) {
                if (blocker.getFollowers().contains(listUser.getUsername()))
                    listUser.removeFollowing(blocker.getUsername());
                for (UserList list :
                        listUser.getUsers()) {
                    if (list.getAddressed().equalsIgnoreCase(blocker.getUsername())) {
                        list.setBlockedEachOther(true);
                        break;
                    }
                }
                break;
            }
        }
        updateUser();
    }

    /**
     * the method for blocking a user.
     */
    public static void unBlock(User unBlocker, User unblocked) {
        for (User listUser :
                listOfUsers) {
            if (listUser.getUsername().equals(unBlocker.getUsername())) {
                for (UserList list : listUser.getUsers()) {
                    if (list.getAddressed().equalsIgnoreCase(unblocked.getUsername())) {
                        list.setBlockedEachOther(false);
                        break;
                    }
                }
                listUser.removeFromBlockedList(unblocked.getUsername());
                break;
            }
        }
        for (User listUser :
                listOfUsers) {
            if (listUser.getUsername().equals(unblocked.getUsername())) {
                for (UserList list : listUser.getUsers()) {
                    if (list.getAddressed().equalsIgnoreCase(unBlocker.getUsername())) {
                        list.setBlockedEachOther(false);
                        break;
                    }
                }
                break;
            }
        }
        updateUser();
    }

    /**
     * this method is for creating an item which allows the user to
     * chat with the searchedUser.
     * this item which is created here will be available in the direct
     * section.
     *
     * @param chatSender   the user who wants to chat with another user
     * @param chatReceiver the addressed user.
     */
    public static void createChatItem(User chatSender, User chatReceiver) {
        UserList newUserListForSender = new UserList(
                chatSender.getUsername(), chatReceiver.getUsername(), new Date()
        );
        UserList newUserListForReceiver = new UserList(
                chatReceiver.getUsername(), chatSender.getUsername(), new Date()
        );
        int indexOfSender = listOfUsers.indexOf(chatSender);
        int indexOfReceiver = listOfUsers.indexOf(chatReceiver);
        if (!listOfUsers.get(indexOfSender).getUsers().contains(newUserListForSender)) {
            if (!listOfUsers.get(indexOfSender).getBlockedList().contains(chatReceiver.getUsername())
                    && !listOfUsers.get(indexOfReceiver).getBlockedList().contains(chatSender.getUsername())) {
                listOfUsers.get(indexOfSender).getUsers().add(newUserListForSender);
                listOfUsers.get(indexOfReceiver).getUsers().add(newUserListForReceiver);
            }
        }
        updateUser();
    }

    /**
     * this method is for finding the list of users our main user has chatted with.
     *
     * @param myUser the user who is controlling the app
     * @return the list of users which we want.
     */
    public static Vector<UserList> getUsers(User myUser) {
        int indexOfUser = listOfUsers.indexOf(myUser);
        return listOfUsers
                .get(indexOfUser)
                .getUsers()
                .stream()
                .filter(userList -> userList.getNumOfChats() != 0)
                .filter(userList -> !userList.isBlockedEachOther())
                .collect(Collectors.toCollection(Vector::new));
    }

    /**
     * this method is for loading messages of a chat.
     *
     * @param myUsername       the user who is controlling the app.
     * @param theOtherUsername the user we are chatting with.
     * @return a list of all messages sent and received
     */
    public static Vector<Message> loadMessages(String myUsername, String theOtherUsername) {
        User myUser = null;
        for (User user : listOfUsers) {
            if (user.getUsername().equalsIgnoreCase(myUsername))
                myUser = user;
        }
        Vector<Message> sent = new Vector<>();
        if (myUser.getSent().containsKey(theOtherUsername))
            sent = myUser.getSent().get(theOtherUsername);
        Vector<Message> received = new Vector<>();
        if (myUser.getReceived().containsKey(theOtherUsername))
            received = myUser.getReceived().get(theOtherUsername);
        Vector<Message> all = new Vector<>();
        all.addAll(sent);
        all.addAll(received);

        for (UserList list :
                myUser.getUsers()) {
            if (list.getAddressed().equalsIgnoreCase(theOtherUsername)) {
                list.restartNumOfUnSeen();
                list.setSeen(true);
                break;
            }
        }
        updateUser();
        Collections.sort(all);
        return all;
    }

    /**
     * this method adds a message to the database of a chat between
     * two users and then returns a newList consisting of all the
     * chats.
     *
     * @param message newMessage which is bout to be added.
     * @return the return value is a list of chats.
     */
    public static Vector<Message> sendMessage(Message message) {
        String sender = message.getSender();
        String receiver = message.getReceiver();
        ReversedMessage reversedMessage = new ReversedMessage(
                message.getDateOfPublish(),
                sender,
                receiver,
                message.getText()
        );
        User myUser = findMyUser(message, sender, receiver);
        User theOtherUser = findTheOtherUser(sender, receiver, reversedMessage, myUser);
        addNumOfChats(sender, theOtherUser);
        Vector<Message> all = findMessages(message, receiver, myUser, theOtherUser);
        updateUser();
        return all;
    }

    /**
     * this method is for adding to the numOf chats of a userList item so
     * that the chat is created if the num of chats is more than 1.
     */
    private static void addNumOfChats(String sender, User theOtherUser) {
        for (User user :
                listOfUsers) {
            if (sender.equalsIgnoreCase(user.getUsername())) {
                for (UserList list :
                        user.getUsers()) {
                    if (list.getAddressed().equalsIgnoreCase(theOtherUser.getUsername())) {
                        list.addNumOfChats();
                        break;
                    }
                }
            }
        }
    }

    /**
     * this method is for adding to the number of unSeen chats for the user
     * so when the other user(the addressed one) checks his list in direct
     * sees that he / she has unseen messages.
     */
    private static User findTheOtherUser(String sender, String receiver, ReversedMessage reversedMessage, User myUser) {
        User theOtherUser = null;
        for (User user :
                listOfUsers) {
            if (receiver.equalsIgnoreCase(user.getUsername())) {
                user.getReceived().computeIfAbsent(sender, k -> new Vector<>());
                user.getReceived().get(sender).add(reversedMessage);
                theOtherUser = user;
                for (UserList list :
                        user.getUsers()) {
                    if (list.getAddressed().equalsIgnoreCase(myUser.getUsername())) {
                        list.addNumOfChats();
                        list.addNumOfUnSeen();
                        break;
                    }
                }
            }
        }
        return theOtherUser;
    }

    /*
     * don't know what the hell happened here so no javadoc :(
     */
    private static User findMyUser(Message message, String sender, String receiver) {
        User myUser = null;

        for (User user :
                listOfUsers) {
            if (sender.equalsIgnoreCase(user.getUsername())) {
                user.getSent().computeIfAbsent(receiver, k -> new Vector<>());
                user.getSent().get(receiver).add(message);
                myUser = user;
            }
        }
        return myUser;
    }

    /**
     * this method finds the messages between two users
     */
    private static Vector<Message> findMessages(Message message, String receiver,
                                                User myUser, User theOtherUser) {
        for (UserList userList :
                myUser.getUsers()) {
            if (userList.getAddressed().equalsIgnoreCase(theOtherUser.getUsername())) {
                userList.setDate(message.getDateOfPublish());
                break;
            }
        }
        Vector<Message> sent = myUser.getSent().get(receiver);
        Vector<Message> received = myUser.getReceived().get(receiver);
        Vector<Message> all = new Vector<>();
        if (sent != null)
            all.addAll(sent);
        if (received != null)
            all.addAll(received);
        return all;
    }

    /**
     * this method is for deleting a message from the
     * database of the messages between two users.
     * but it seems that there is a bug in java FX listView so change of plans!
     * the message isn't actually deleted but its boolean is set to true
     * so the graphic changes when it is deleted.
     *
     * @param deletedMessage the message which is bout to be deleted.
     */
    public static void deleteMessage(Message deletedMessage) {
        User myUser = null;
        String sender = deletedMessage.getSender();
        String receiver = deletedMessage.getReceiver();


        for (User user :
                listOfUsers) {
            if (sender.equalsIgnoreCase(user.getUsername())) {
                user.getSent().get(receiver).remove(deletedMessage);
                myUser = user;
            }
        }
        ReversedPicMessage reversedPicMessage;
        ReversedMessage reversedMessage;

        if (deletedMessage instanceof PicMessage) {
            reversedPicMessage = new ReversedPicMessage(
                    deletedMessage.getDateOfPublish(),
                    sender,
                    receiver,
                    deletedMessage.getText(),
                    ((PicMessage) deletedMessage).getPic()
            );
            for (User user :
                    listOfUsers) {
                if (receiver.equalsIgnoreCase(user.getUsername())) {
                    user.getReceived().get(sender).remove(reversedPicMessage);
                    for (UserList list :
                            user.getUsers()) {
                        if (list.getAddressed().equalsIgnoreCase(myUser.getUsername())) {
                            list.reduceNumOfChats();
                            if (!list.isSeen())
                                list.reduceNumOfUnSeen();
                            break;
                        }
                    }
                }
            }
        } else {
            reversedMessage = new ReversedMessage(
                    deletedMessage.getDateOfPublish(),
                    sender,
                    receiver,
                    deletedMessage.getText()
            );
            for (User user :
                    listOfUsers) {
                if (receiver.equalsIgnoreCase(user.getUsername())) {
                    user.getReceived().get(sender).remove(reversedMessage);
                    for (UserList list :
                            user.getUsers()) {
                        if (list.getAddressed().equalsIgnoreCase(myUser.getUsername())) {
                            list.reduceNumOfChats();
                            break;
                        }
                    }
                }
            }
        }
        updateUser();
    }

    /**
     * this method is for editing an existing message.
     * the old message message and the new text is sent and is replaced with
     * the old one.
     *
     * @param editedMessage the message we tend to edit
     * @param editedText    the new text of the message.
     */
    public static void editMessage
    (Message editedMessage, String editedText) {
        String sender = editedMessage.getSender();
        String receiver = editedMessage.getReceiver();
        ReversedPicMessage reversedPicMessage;
        ReversedMessage reversedMessage;
        if (editedMessage instanceof PicMessage) {
            reversedPicMessage = new ReversedPicMessage(
                    editedMessage.getDateOfPublish(),
                    sender,
                    receiver,
                    editedMessage.getText(),
                    ((PicMessage) editedMessage).getPic()
            );
            for (User user :
                    listOfUsers) {
                if (receiver.equalsIgnoreCase(user.getUsername())) {
                    user.getReceived().get(sender).remove(reversedPicMessage);
                    reversedPicMessage.setText(editedText);
                    user.getReceived().get(sender).add(reversedPicMessage);
                    break;
                }
            }
        } else {
            reversedMessage = new ReversedMessage(
                    editedMessage.getDateOfPublish(),
                    sender,
                    receiver,
                    editedMessage.getText()
            );
            for (User user :
                    listOfUsers) {
                if (receiver.equalsIgnoreCase(user.getUsername())) {
                    user.getReceived().get(sender).remove(reversedMessage);
                    reversedMessage.setText(editedText);
                    user.getReceived().get(sender).add(reversedMessage);
                    break;
                }
            }
        }
        for (User user :
                listOfUsers) {
            if (sender.equalsIgnoreCase(user.getUsername())) {
                user.getSent().get(receiver).remove(editedMessage);
                editedMessage.setText(editedText);
                user.getSent().get(receiver).add(editedMessage);
                break;
            }
        }
        updateUser();
    }

    /**
     * this method is for searching the user whom we want to chat with.
     */
    public static User searchChat(User searcher, String username) {
        for (User listUser :
                listOfUsers) {
            if (listUser.getUsername().equalsIgnoreCase(username)
                    && !listUser.getBlockedList().contains(searcher.getUsername()))
                return listUser;
        }
        return null;
    }

    /**
     * this method is like sendMessage method but it adds a picMessage
     * instead of a normal Message.
     *
     * @param picMessage the message we want to add to the database.
     * @return the list of messages between two users.
     */
    public static Vector<Message> sendPicMessage(PicMessage picMessage) {
        String sender = picMessage.getSender();
        String receiver = picMessage.getReceiver();
        ReversedPicMessage reversedMessage = new ReversedPicMessage(
                picMessage.getDateOfPublish(),
                sender,
                receiver,
                picMessage.getText(),
                picMessage.getPic()
        );
        User myUser = findMyUser(picMessage, sender, receiver);
        User theOtherUser = createOtherUserItem(sender, receiver, reversedMessage, myUser);
        addNumOfChats(sender, theOtherUser);
        Vector<Message> all = findMessages(picMessage, receiver, myUser, theOtherUser);
        updateUser();
        return all;
    }

    private static User createOtherUserItem(String sender, String receiver, ReversedPicMessage reversedMessage, User myUser) {
        User theOtherUser = null;
        for (User user :
                listOfUsers) {
            if (receiver.equalsIgnoreCase(user.getUsername())) {
                user.getReceived().computeIfAbsent(sender, k -> new Vector<>());
                user.getReceived().get(sender).add(reversedMessage);
                theOtherUser = user;
                for (UserList list :
                        user.getUsers()) {
                    if (list.getAddressed().equalsIgnoreCase(myUser.getUsername())) {
                        list.addNumOfChats();
                        list.addNumOfUnSeen();
                        break;
                    }
                }
            }
        }
        return theOtherUser;
    }
}
