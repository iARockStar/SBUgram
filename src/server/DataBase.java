package server;

import other.User;

import java.io.*;

import other.*;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicInteger;

public class DataBase {

    private static final String USERS_FILE
            = "C:\\Users\\USER\\IdeaProjects\\signUp.bin";

    private static final String POST_ID_FILE
            = "C:\\Users\\USER\\IdeaProjects\\postId.bin";

    private static Vector<User> listOfUsers
            = new Vector<>();

    private static AtomicInteger idCounter =
            new AtomicInteger(0);


    public static void initializeServer() {
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
            objectInputStream1.close();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }


    public synchronized static ApprovedType signupUpdate(User user) {
        if (!listOfUsers.contains(user)) {
            listOfUsers.add(user);
            updateUser();
            return ApprovedType.APPROVED;
        }
        return ApprovedType.NOT_APPROVED;

    }

    private synchronized static void updateUser() {
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

    private synchronized static void sortPosts() {
        for (User user :
                listOfUsers) {
            Collections.sort(user.getListOfPosts());
        }
    }

    public synchronized static void updatePost(Post post, User user) {
        for (int i = 0; i < listOfUsers.size(); i++) {
            if (user.getUsername().equals(listOfUsers.get(i).getUsername())) {
                post.setPostId();
                listOfUsers.get(i).addPost(post);
            }
        }
        updateUser();
    }


    public synchronized static User loadPost(User requester, User requested) {
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

    public synchronized static User retrievePass(User user) {
        for (User listUser :
                listOfUsers) {
            if (user.getUsername().equalsIgnoreCase(listUser.getUsername()))
                return listUser;
        }
        return null;
    }

    public synchronized static boolean saveNewPass(User user) {
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

    public synchronized static User search(String username) {
        for (User listUser :
                listOfUsers) {
            if (listUser.getUsername().equalsIgnoreCase(username))
                return listUser;
        }
        return null;
    }

    public synchronized static Vector<Comment> addAndSendComments(User user, Post post, Comment comment) {
        List<Comment> commentList = new Vector<>();
        for (User listUser :
                listOfUsers) {
            if (user.getUsername().equals(listUser.getUsername())) {
                for (Post listUserPost : listUser.getListOfPosts()) {
                    if (listUserPost.equals(post)) {
                        listUserPost.getComments().add(comment);
                        commentList = listUserPost.getComments();
                    }
                }
                updateUser();
                return (Vector<Comment>) commentList;
            }
        }
        return null;
    }

    public synchronized static Vector<Comment> sendComments(User user, Post post) {
        for (User listUser :
                listOfUsers) {
            if (user.getUsername().equals(listUser.getUsername())) {
                for (Post listUserPost : listUser.getListOfPosts()) {
                    if (listUserPost.equals(post))
                        return listUserPost.getComments();
                }
            }
        }
        return null;
    }

    public synchronized static AtomicInteger follow(User following, User follower) {
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

    public synchronized static AtomicInteger unfollow(User following, User follower) {
        AtomicInteger atomicInteger = new AtomicInteger(0);
        for (User listUser :
                listOfUsers) {
            if (following.getUsername().equals(listUser.getUsername())) {
                listUser.removeFollower(follower.getUsername());
                atomicInteger = listUser.getNumOfFollowers();
                break;
            }
        }
        for (User listUser :
                listOfUsers) {
            if (follower.getUsername().equals(listUser.getUsername())) {
                listUser.removeFollowing(following.getUsername());
            }
        }
        updateUser();
        return atomicInteger;
    }

    public synchronized static Vector<Post> loadFollowingPosts(User user) {
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

    public synchronized static void like(User user, Post post) {
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

    public synchronized static void dislike(User user, Post post) {
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

    public synchronized static User updateUser(String username) {
        for (User user :
                listOfUsers) {
            if (user.getUsername().equalsIgnoreCase(username))
                return user;
        }
        return null;
    }

    public synchronized static ApprovedType repost(User user, Post post) {
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

    public synchronized static void settingUpdate(User user) {
        for (int i = 0; i < listOfUsers.size(); i++) {
            if (listOfUsers.get(i).getUsername().equalsIgnoreCase(user.getUsername())) {
                listOfUsers.remove(i);
                listOfUsers.add(i, user);
                listOfUsers.get(i).setPassword(user.getPassword());
                for (Post post : listOfUsers.get(i).getListOfPosts()) {
                    if (post.getOwner().getUsername()
                            .equalsIgnoreCase(listOfUsers.get(i).getUsername()))
                        post.setOwner(user);
                }
                break;
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
    public synchronized static void deleteAccount(User deletedUser) {
        deleteLikes(deletedUser);
        deleteFollows(deletedUser);
        deleteReposts(deletedUser);
        deleteAcc(deletedUser);
        for (User listUser :
                listOfUsers) {
            for (Post post :
                    listUser.getListOfPosts()) {
                post.getComments().removeIf(
                        comment -> comment.getOwner().equals(deletedUser)
                );
            }
        }
        updateUser();
    }

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

    private synchronized static void deleteAcc(User deletedUser) {
        for (User listUser :
                listOfUsers) {
            if (deletedUser.getUsername().equalsIgnoreCase(listUser.getUsername())) {
                listOfUsers.remove(listUser);
                break;
            }
        }
    }

    private synchronized static void deleteFollows(User deletedUser) {
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

    private synchronized static void deleteLikes(User deletedUser) {
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

    public synchronized static void mute(User muter, User muted) {
        for (User listUser :
                listOfUsers) {
            if (listUser.getUsername().equals(muter.getUsername())) {
                listUser.addMuted(muted.getUsername());
                break;
            }
        }
        updateUser();
    }

    public synchronized static void unMute(User unMuter, User unMuted) {
        for (User listUser :
                listOfUsers) {
            if (listUser.getUsername().equals(unMuter.getUsername())) {
                listUser.removeMuted(unMuted);
                break;
            }
        }
        updateUser();
    }

    public synchronized static void block(User blocker, User blocked) {
        for (User listUser :
                listOfUsers) {
            if (listUser.getUsername().equals(blocker.getUsername())) {
                if (blocked.getFollowings().contains(listUser.getUsername())) {
                    listUser.removeFollower(blocked.getUsername());
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
                break;
            }
        }
        updateUser();
    }

    public synchronized static void unBlock(User unBlocker, User unblocked) {
        for (User listUser :
                listOfUsers) {
            if (listUser.getUsername().equals(unBlocker.getUsername())) {
                listUser.removeFromBlockedList(unblocked.getUsername());
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
    public synchronized static void createChatItem(User chatSender, User chatReceiver) {
        UserList newUserListForSender = new UserList(
                chatSender.getUsername(), chatReceiver.getUsername(), new Date()
        );
        UserList newUserListForReceiver = new UserList(
                chatReceiver.getUsername(), chatSender.getUsername(), new Date()
        );
        int indexOfSender = listOfUsers.indexOf(chatSender);
        int indexOfReceiver = listOfUsers.indexOf(chatReceiver);
        if (!listOfUsers.get(indexOfSender).getUsers().contains(newUserListForSender)) {
            listOfUsers.get(indexOfSender).getUsers().add(newUserListForSender);
            listOfUsers.get(indexOfReceiver).getUsers().add(newUserListForReceiver);
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
        return listOfUsers.get(indexOfUser).getUsers();
    }

    /**
     * this method is for loading messages of a chat.
     *
     * @param myUsername       the user who is controlling the app.
     * @param theOtherUsername the user we are chatting with.
     * @return a list of all messages sent and received
     */
    public synchronized static Vector<Message> loadMessages(String myUsername, String theOtherUsername) {
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

        for (UserList list:
                myUser.getUsers()) {
            if(list.getAddressed().equalsIgnoreCase(theOtherUsername)){
                list.restartNumOfUnSeen();
                break;
            }
        }
        Collections.sort(all);
        return all;
    }

    /**
     * this method adds a message to the database of a chat between
     * two users and then returns a newList consisting of all the
     * chats.
     * @param message newMessage which is bout to be added.
     * @return the return value is a list of chats.
     */
    public synchronized static Vector<Message> sendMessage(Message message) {
        String sender = message.getSender();
        String receiver = message.getReceiver();
        ReversedMessage reversedMessage = new ReversedMessage(
                message.getDateOfPublish(),
                sender,
                receiver,
                message.getText()
        );
        User myUser = null;
        User theOtherUser = null;
        for (User user :
                listOfUsers) {
            if (sender.equalsIgnoreCase(user.getUsername())) {
                user.getSent().computeIfAbsent(receiver, k -> new Vector<>());
                user.getSent().get(receiver).add(message);
                myUser = user;

            }
        }
        for (User user :
                listOfUsers) {
            if (receiver.equalsIgnoreCase(user.getUsername())) {
                user.getReceived().computeIfAbsent(sender, k -> new Vector<>());
                user.getReceived().get(sender).add(reversedMessage);
                theOtherUser = user;
                for (UserList list:
                        user.getUsers()) {
                    if(list.getAddressed().equalsIgnoreCase(myUser.getUsername())){
                        list.addNumOfUnSeen();
                        break;
                    }
                }
            }

        }

        Vector<Message> all = findMessages(message, receiver, myUser, theOtherUser);
        updateUser();
        return all;
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
}
