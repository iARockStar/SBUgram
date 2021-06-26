package server;

import java.io.*;
import java.util.List;
import java.util.Objects;
import java.util.Vector;

import java.util.concurrent.atomic.AtomicInteger;

import other.*;


/**
 * this class is a thread and its run method is started each time a user
 * connects to the server and input and output streams are set in order
 * to send and receive data.
 */
public class ClientHandler extends Thread {
    /**
     * the streams needed for reading and writing objects.
     */
    private final ObjectInputStream objectInputStream;
    private final ObjectOutputStream objectOutputStream;
    private boolean isClientOnline = true;

    /**
     * constructor which sets the streams
     *
     * @param objectInputStream  stream for reading the objects
     * @param objectOutputStream stream for writing the objects.
     */
    public ClientHandler(ObjectInputStream objectInputStream, ObjectOutputStream objectOutputStream) {
        this.objectInputStream = objectInputStream;
        this.objectOutputStream = objectOutputStream;

    }

    /**
     * this method is called for each thread and all the requests to the
     * server are handles here.
     * the objectInputStream receives an object which is always
     * an instance of the CommandSender class.
     * it receives this instance and according to the CommandType,
     * does some actions related to the command and then if needed it sends
     * some objects using other methods.
     */
    @Override
    public void run() {
        while (isClientOnline) {
            try {
                CommandSender commandSender = (CommandSender) objectInputStream.readObject();
                CommandType commandType = commandSender.getCommandType();
                switch (commandType) {
                    case UPDATEUSER:
                        if(commandSender.getObject() == null)
                            break;
                        String username = ((User) commandSender.getObject()).getUsername();
                        updateUser(username);
                        break;
                    case LOGIN:
                        User user = (User) commandSender.getObject();
                        username = user.getUsername();
                        String password = user.getPassword();
                        login(username, password);
                        Log.login(username);
                        break;
                    case SIGNUP:
                        user = (User) commandSender.getObject();
                        signup(user);
                        Log.signup(user);
                        break;
                    case NEWPOST:
                        Post post = (Post) commandSender.getObject();
                        String owner = post.getOwner();
                        updatePost(post, owner);
                        Log.newPost(post, owner);
                        break;
                    case LOADAPOST:
                        User requester = commandSender.getRequester();
                        User requested = commandSender.getRequested();
                        loadPost(requester, requested);
                        Log.getMyPosts(requested);
                        break;
                    case RETRIEVEPASS:
                        user = (User) commandSender.getObject();
                        retrievePass(user);
                        break;
                    case RETRIEVEPASS2NDPART:
                        user = (User) commandSender.getObject();
                        saveNewPass(user);
                        break;
                    case SEARCHUSER:
                        String searched = (String) commandSender.getObject();
                        User searcher = commandSender.getSearcher();
                        User searchedUser = search(searched);
                        Log.search(searchedUser, searcher);
                        break;
                    case LOGOUT:
                        isClientOnline = false;
                        User loggedOut = (User) commandSender.getObject();
                        Log.logout(loggedOut);
                        break;
                    case LOADCOMMENTS:
                        post = commandSender.getPostToComment();
                        String commentUsername = commandSender.getUserOfThePost();
                        sendComments(commentUsername, post);
                        break;
                    case COMMENT:
                        Comment comment = commandSender.getComment();
                        commentUsername = commandSender.getUserOfThePost();
                        User commenter = commandSender.getCommenter();
                        post = commandSender.getPostToComment();
                        addAndSendComment(commentUsername, post, comment);
                        Log.comment(commenter, post);
                        break;
                    case FOLLOW:
                        User follower = commandSender.getRequester();
                        User following = commandSender.getRequested();
                        follow(following, follower);
                        Log.follow(following, follower);
                        break;
                    case UNFOLLOW:
                        follower = commandSender.getRequester();
                        following = commandSender.getRequested();
                        unfollow(following, follower);
                        Log.unfollow(following, follower);
                        break;
                    case LOADFOLLOWINGPOSTS:
                        user = (User) commandSender.getObject();
                        loadFollowingsPosts(user);
                        Log.getMyPosts(user);
                        break;
                    case LIKE:
                        post = commandSender.getPostToLike();
                        user = commandSender.getUserWhoLiked();
                        like(user, post);
                        Log.like(user, post);
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
                        User updatedUser = (User) commandSender.getObject();
                        settingUpdate(updatedUser);
                        Log.setting(updatedUser);
                        break;
                    case DELETEACCOUNT:
                        User deletedUser = (User) commandSender.getObject();
                        deleteAccount(deletedUser);
                        Log.deleteAcc(deletedUser);
                        break;
                    case MUTE:
                        User muter = commandSender.getRequester();
                        User muted = commandSender.getRequested();
                        mute(muter, muted);
                        Log.mute(muter, muted);
                        break;
                    case UNMUTE:
                        User unMuter = commandSender.getRequester();
                        User unMuted = commandSender.getRequested();
                        unMute(unMuter, unMuted);
                        Log.unmute(unMuter, unMuted);
                        break;
                    case BLOCK:
                        User blocker = commandSender.getRequester();
                        User blocked = commandSender.getRequested();
                        block(blocker, blocked);
                        Log.block(blocker, blocked);
                        break;
                    case UNBLOCK:
                        User unBlocker = commandSender.getRequester();
                        User unblocked = commandSender.getRequested();
                        unblock(unBlocker, unblocked);
                        Log.unBlock(unBlocker, unblocked);
                        break;
                    /*
                     * this part is for the chat
                     * and its abilities.
                     */
                    case SEARCHCHAT:
                        String searched1 = (String) commandSender.getObject();
                        User searcher1 = commandSender.getSearcher();
                        searchChat(searcher1, searched1);
                        break;
                    case CREATECHATITEM:
                        User chatSender = commandSender.getRequester();
                        User chatReceiver = commandSender.getRequested();
                        createChatItem(chatSender, chatReceiver);
                        break;
                    case GETUSERS:
                        User myUser = (User) commandSender.getObject();
                        getUsers(myUser);
                        break;
                    case GETCHATS:
                        String myUsername = commandSender.getMe();
                        String theOtherUsername = commandSender.getTheOtherOne();
                        loadMessages(myUsername, theOtherUsername);
                        break;
                    case SENDMESSAGE:
                        Message message = commandSender.getSentMessage();
                        sendMessage(message);
                        Log.sendMessage(message);
                        Log.receiveMessage(message);
                        break;
                    case DELETEMESSAGE:
                        Message deletedMessage = commandSender.getSentMessage();
                        deleteMessage(deletedMessage);
                        break;
                    case EDITMESSAGE:
                        Message editedMessage = commandSender.getEditedMessage();
                        String editedText = commandSender.getEditedText();
                        editMessage(editedMessage, editedText);
                        break;
                    case SENDPICMESSAGE:
                        PicMessage picMessage = (PicMessage) commandSender.getSentMessage();
                        sendPicMessage(picMessage);
                        break;
                }
            } catch (IOException | ClassNotFoundException ioException) {
                ioException.printStackTrace();
            }
        }
        /*
         * the while loop breaks if the
         * user logs out from the app
         */
        try {
            objectOutputStream.close();
            objectInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendPicMessage(PicMessage picMessage) {
        Vector<Message> all = DataBase.sendPicMessage(picMessage);
        try {
            objectOutputStream.reset();
            objectOutputStream.writeObject(new Vector<>(all));
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    /*
    * here are the methods which call the more important methods in the DB.
    */
    private void searchChat(User searcher1, String searched1) {
        User user = DataBase.searchChat(searcher1, searched1);
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
    }

    private void editMessage(Message editedMessage, String editedText) {
        DataBase.editMessage(editedMessage, editedText);
    }

    private void deleteMessage(Message deletedMessage) {
        DataBase.deleteMessage(deletedMessage);
    }

    private void sendMessage(Message message) {
        Vector<Message> all = DataBase.sendMessage(message);
        try {
            objectOutputStream.reset();
            objectOutputStream.writeObject(new Vector<>(all));
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    /**
     * this method returns a list of chats which has been done between
     * two users.
     * @param myUsername       username which the user controls
     * @param theOtherUsername username which the user chats with
     */
    private void loadMessages(String myUsername, String theOtherUsername) {
        Vector<Message> all = DataBase.loadMessages(myUsername, theOtherUsername);
        try {
            objectOutputStream.reset();
            objectOutputStream.writeObject(new Vector<>(all));
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    /**
     * this method gets the list of users whom myUser chatted with
     *
     * @param myUser hte user we tend to get its list of users
     */
    private void getUsers(User myUser) {
        Vector<UserList> users;
        try {
            users = DataBase.getUsers(myUser);
            objectOutputStream.reset();
            objectOutputStream.writeObject(users);
            objectOutputStream.flush();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    private void createChatItem(User chatSender, User chatReceiver) {
        DataBase.createChatItem(chatSender, chatReceiver);

    }

    private void unblock(User unBlocker, User unblocked) {
        DataBase.unBlock(unBlocker, unblocked);
    }

    private void block(User blocker, User blocked) {
        DataBase.block(blocker, blocked);
    }

    private void unMute(User unMuter, User unMuted) {
        DataBase.unMute(unMuter, unMuted);
    }

    private void mute(User muter, User muted) {
        DataBase.mute(muter, muted);
    }

    private void deleteAccount(User deletedUser) {
        DataBase.deleteAccount(deletedUser);
    }

    private void settingUpdate(User user) {
        DataBase.settingUpdate(user);
    }

    private void repost(User user, Post post) {

        ApprovedType approvedType = DataBase.repost(user, post);
        try {
            objectOutputStream.reset();
            objectOutputStream.writeObject(approvedType);
        } catch (Exception e) {
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

    private void sendComments(String user, Post post) {
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

    private void addAndSendComment(String user, Post post, Comment comment) {
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
        try {
            objectOutputStream.reset();
            objectOutputStream.writeObject(isSignedUp);
            objectOutputStream.flush();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public synchronized void updatePost(Post post, String user) {

        DataBase.updatePost(post, user);

    }

    public synchronized void loadPost(User requester, User requested) {
        User user1 = DataBase.loadPost(requester, requested);
        if (user1 != null)
            try {
                objectOutputStream.writeObject(
                        new Vector<>(user1.getListOfPosts()));
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        else {
            try {
                objectOutputStream.reset();
                objectOutputStream.writeObject(ApprovedType.NOT_APPROVED);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }
}
