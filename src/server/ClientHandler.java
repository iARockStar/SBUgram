package server;

import java.io.*;
import java.util.List;
import java.util.Objects;
import java.util.Vector;

import java.util.concurrent.atomic.AtomicInteger;

import other.*;

public class ClientHandler extends Thread {
    private final ObjectInputStream objectInputStream;
    private final ObjectOutputStream objectOutputStream;
    private boolean isClientOnline = true;

    public ClientHandler(ObjectInputStream objectInputStream, ObjectOutputStream objectOutputStream) {
        this.objectInputStream = objectInputStream;
        this.objectOutputStream = objectOutputStream;

    }

    @Override
    public void run() {
        while (isClientOnline) {
            try {
                CommandSender commandSender = (CommandSender) objectInputStream.readObject();
                CommandType commandType = commandSender.getCommandType();
                switch (commandType) {
                    case UPDATEUSER:
                        String username = ((User) commandSender.getUser()).getUsername();
                        updateUser(username);
                        break;
                    case LOGIN:
                        User user = (User) commandSender.getUser();
                        username = user.getUsername();
                        String password = user.getPassword();
                        login(username, password);
                        Log.login(username);
                        break;
                    case SIGNUP:
                        user = (User) commandSender.getUser();
                        signup(user);
                        Log.signup(user);
                        break;
                    case NEWPOST:
                        Post post = (Post) commandSender.getUser();
                        User user1 = post.getOwner();
                        updatePost(post, user1);
                        Log.newPost(post,user1);
                        break;
                    case LOADAPOST:
                        User requester = commandSender.getRequester();
                        User requested = commandSender.getRequested();
                        loadPost(requester,requested);
                        Log.getMyPosts(requested);
                        break;
                    case RETRIEVEPASS:
                        user = (User) commandSender.getUser();
                        retrievePass(user);
                        System.out.println("done part 1");
                        break;
                    case RETRIEVEPASS2NDPART:
                        user = (User) commandSender.getUser();
                        saveNewPass(user);
                        System.out.println("done part 2");
                        break;
                    case SEARCHUSER:
                        String searched = (String) commandSender.getUser();
                        User searcher = commandSender.getSearcher();
                        User searchedUser = search(searched);
                        Log.search(searchedUser,searcher);
                        break;
                    case LOGOUT:
                        isClientOnline = false;
                        User loggedOut = (User) commandSender.getUser();
                        Log.logout( loggedOut);
                        break;
                    case LOADCOMMENTS:
                        post = commandSender.getPostToComment();
                        user = commandSender.getUserOfThePost();
                        sendComments(user, post);
                        break;
                    case COMMENT:
                        Comment comment = commandSender.getComment();
                        user = commandSender.getUserOfThePost();
                        User commenter = commandSender.getUserWhoWantsToPost();
                        post = commandSender.getPostToComment();
                        addAndSendComment(user, post, comment);
                        Log.comment(commenter, post);
                        break;
                    case FOLLOW:
                        User follower = commandSender.getRequester();
                        User following = commandSender.getRequested();
                        follow(following, follower);
                        Log.follow(following , follower);
                        break;
                    case UNFOLLOW:
                        follower = commandSender.getRequester();
                        following = commandSender.getRequested();
                        unfollow(following, follower);
                        Log.unfollow(following , follower);
                        break;
                    case LOADFOLLOWINGPOSTS:
                        user = (User) commandSender.getUser();
                        loadFollowingsPosts(user);
                        Log.getMyPosts(user);
                        break;
                    case LIKE:
                        post = commandSender.getPostToLike();
                        user = commandSender.getUserWhoLiked();
                        like(user, post);
                        Log.like(user,post);
                        break;
                    case DISLIKE:
                        post = commandSender.getPostToLike();
                        user = commandSender.getUserWhoLiked();
                        dislike(user, post);
                        break;
                    case REPOST:
                        user = commandSender.getReposter();
                        post = commandSender.getRepostedPost();
                        repost(user, post);
                        Log.rePost(user, post);
                        break;
                    case SETTING:
                        User updatedUser = (User) commandSender.getUser();
                        settingUpdate(updatedUser);
                        Log.setting(updatedUser);
                        break;
                    case DELETEACCOUNT:
                        User deletedUser = (User) commandSender.getUser();
                        deleteAccount(deletedUser);
                        Log.deleteAcc(deletedUser);
                        break;
                    case MUTE:
                        User muter = commandSender.getRequester();
                        User muted = commandSender.getRequested();
                        mute(muter,muted);
                        Log.mute(muter,muted);
                        break;
                    case UNMUTE:
                        User unMuter = commandSender.getRequester();
                        User unMuted = commandSender.getRequested();
                        unMute(unMuter,unMuted);
                        Log.unmute(unMuter,unMuted);
                        break;
                    case BLOCK:
                        User blocker = commandSender.getRequester();
                        User blocked = commandSender.getRequested();
                        block(blocker,blocked);
                        Log.block(blocker,blocked);
                        break;
                    case UNBLOCK:
                        User unBlocker = commandSender.getRequester();
                        User unblocked = commandSender.getRequested();
                        unblock(unBlocker,unblocked);
                        Log.unBlock(unBlocker,unblocked);
                        break;
                    case CREATECHATITEM:
                        User chatSender = commandSender.getRequester();
                        User chatReceiver = commandSender.getRequested();
                        createChatItem(chatSender,chatReceiver);
                        break;
                    case GETUSERS:
                        User myUser = (User) commandSender.getUser();
                        getUsers(myUser);
                        break;
                    case GETCHATS:
                        String myUsername = commandSender.getMe();
                        String theOtherUsername = commandSender.getTheOtherOne();
                        loadMessages(myUsername,theOtherUsername);
                        break;
                    case SENDMESSAGE:
                        Message message = commandSender.getSentMessage();
                        sendMessage(message);
                        break;
                }
            } catch (IOException | ClassNotFoundException ioException) {
                ioException.printStackTrace();
            }
        }

        try {
            objectOutputStream.close();
            objectInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendMessage(Message message) {
        Vector<Message> all =  DataBase.sendMessage(message);
        try{
            objectOutputStream.reset();
            objectOutputStream.writeObject(all);
        }catch (IOException ioException){
            ioException.printStackTrace();
        }
    }

    /**
     * this method returns a list of chats which has been done between
     * two users.
     * @param myUsername username which the user controls
     * @param theOtherUsername username which the user chats with
     */
    private void loadMessages(String myUsername, String theOtherUsername) {
       Vector<Message> all =  DataBase.loadMessages(myUsername,theOtherUsername);
       try{
           objectOutputStream.reset();
           objectOutputStream.writeObject(all);
       }catch (IOException ioException){
           ioException.printStackTrace();
       }
    }

    /**
     * this method gets the list of users whom myUser chatted with
     * @param myUser hte user we tend to get its list of users
     */
    private void getUsers(User myUser) {
        Vector<UserList> users;
        try{
            users = DataBase.getUsers(myUser);
            objectOutputStream.reset();
            objectOutputStream.writeObject(users);
            objectOutputStream.flush();
        }catch (IOException ioException){
            ioException.printStackTrace();
        }
    }

    private void createChatItem(User chatSender, User chatReceiver) {
        DataBase.createChatItem(chatSender,chatReceiver);

    }

    private void unblock(User unBlocker, User unblocked) {
        DataBase.unBlock(unBlocker,unblocked);
    }

    private void block(User blocker, User blocked) {
        DataBase.block(blocker , blocked);
    }

    private void unMute(User unMuter, User unMuted) {
        DataBase.unMute(unMuter,unMuted);
    }

    private void mute(User muter, User muted) {
        DataBase.mute(muter,muted);
    }

    private void deleteAccount(User deletedUser) {
        DataBase.deleteAccount(deletedUser);
    }

    private void settingUpdate(User user) {
        DataBase.settingUpdate(user);
    }

    private void repost(User user, Post post) {

        ApprovedType approvedType = DataBase.repost(user, post);
        try{
            objectOutputStream.reset();
            objectOutputStream.writeObject(approvedType);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void updateUser(String username) {
        User user = DataBase.updateUser(username);
        try {
            objectOutputStream.reset();
            objectOutputStream.writeObject(user);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void dislike(User user, Post post) {
        DataBase.dislike(user, post);
    }

    private void like(User user, Post post) {
        DataBase.like(user, post);
    }

    private void loadFollowingsPosts(User user) {
        Vector<Post> posts = DataBase.loadFollowingPosts(user);
        try {
            objectOutputStream.reset();
            objectOutputStream.writeObject(new Vector<>(posts));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void unfollow(User following, User follower) {

        AtomicInteger atomicInteger = DataBase.unfollow(following, follower);
        try {
            objectOutputStream.reset();
            objectOutputStream.writeObject(new AtomicInteger(atomicInteger.intValue()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void follow(User following, User follower) {
        AtomicInteger atomicInteger = DataBase.follow(following, follower);
        try {
            objectOutputStream.reset();
            objectOutputStream.writeObject(new AtomicInteger(atomicInteger.intValue()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendComments(User user, Post post) {
        Vector<Comment> comments = DataBase.sendComments(user, post);
        try {
            if (comments != null) {
                objectOutputStream.reset();
                objectOutputStream.writeObject(new Vector<>(comments));
                objectOutputStream.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addAndSendComment(User user, Post post, Comment comment) {
        List<Comment> comments = DataBase.addAndSendComments(user, post, comment);
        if (comments != null) {
            try {
                objectOutputStream.reset();
                objectOutputStream.writeObject(new Vector<>(comments));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                objectOutputStream.reset();
                objectOutputStream.writeObject(ApprovedType.NOT_APPROVED);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    private User search(String username) {
        User user = DataBase.search(username);
        if (user != null) {
            try {
                objectOutputStream.reset();
                objectOutputStream.writeObject(user);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                objectOutputStream.reset();
                objectOutputStream.writeObject(ApprovedType.NOT_APPROVED);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return user;
    }


    private synchronized void saveNewPass(User user) {
        boolean result = DataBase.saveNewPass(user);
        try {
            objectOutputStream.writeObject(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private synchronized void retrievePass(User user) {
        User user1 = DataBase.retrievePass(user);
        try {
            objectOutputStream
                    .writeObject(Objects
                            .requireNonNullElse
                                    (user1, ApprovedType.NOT_APPROVED));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private synchronized void login(String username, String password) throws IOException, ClassNotFoundException {
        DataBase.login(objectOutputStream, username, password);
    }

    public synchronized void signup(User user) throws IOException, ClassNotFoundException {
        ApprovedType isSignedUp = DataBase.
                signupUpdate(user);
        try{
            objectOutputStream.reset();
            objectOutputStream.writeObject(isSignedUp);
            objectOutputStream.flush();
        }catch (IOException ioException){
            ioException.printStackTrace();
        }
    }

    public synchronized void updatePost(Post post, User user) {

        DataBase.updatePost(post, user);

    }

    public synchronized void loadPost(User requester, User requested) {
        User user1 = DataBase.loadPost(requester,requested);
        if (user1 != null)
            try {
                objectOutputStream.writeObject(
                        new Vector<>(user1.getListOfPosts()));
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        else{
            try {
                objectOutputStream.reset();
                objectOutputStream.writeObject(ApprovedType.NOT_APPROVED);
            }catch (IOException ioException){
                ioException.printStackTrace();
            }
        }
    }
}
