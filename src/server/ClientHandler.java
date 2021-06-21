package server;

import java.io.*;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import javafx.geometry.Pos;
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
                        commandSender.setUser(null);
                        username = user.getUsername();
                        String password = user.getPassword();
                        login(username, password);
                        break;
                    case SIGNUP:
                        user = (User) commandSender.getUser();
                        commandSender.setUser(null);
                        signup(user);
                        System.out.println("user signed up successfully");
                        break;
                    case NEWPOST:
                        Post post = (Post) commandSender.getUser();
                        User user1 = post.getOwner();
                        updatePost(post, user1);
                        break;
                    case LOADAPOST:
                        user = (User) commandSender.getUser();
                        loadPost(user);
                        System.out.println("done");
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
                        username = (String) commandSender.getUser();
                        search(username);
                        break;
                    case LOGOUT:
                        isClientOnline = false;
                        System.out.println("client disconnected.");
                        break;
                    case LOADCOMMENTS:
                        post = (Post) commandSender.getPostToComment();
                        user = commandSender.getUserOfThePost();
                        sendComments(user, post);
                        break;
                    case COMMENT:
                        Comment comment = commandSender.getComment();
                        user = commandSender.getUserOfThePost();
                        User commenter = commandSender.getUserWhoWantsToPost();
                        post = commandSender.getPostToComment();
                        addAndSendComment(user, post, comment);
                        break;
                    case FOLLOW:
                        User follower = commandSender.getUserToFollow();
                        User following = commandSender.getFollower();
                        follow(following, follower);
                        break;
                    case UNFOLLOW:
                        follower = commandSender.getUserToFollow();
                        following = commandSender.getFollower();
                        unfollow(following, follower);
                        break;
                    case LOADFOLLOWINGPOSTS:
                        user = (User) commandSender.getUser();
                        loadFollowingsPosts(user);
                        break;
                    case LIKE:
                        post = commandSender.getPostToLike();
                        user = commandSender.getUserWhoLiked();
                        like(user, post);
                        break;
                    case DISLIKE:
                        post = commandSender.getPostToLike();
                        user = commandSender.getUserWhoLiked();
                        dislike(user, post);
                        break;
                    case REPOST:
                        user = commandSender.getReposter();
                        user1 = commandSender.getUserOfTheRepostedPost();
                        post = commandSender.getRepostedPost();
                        repost(user, post);
                        System.out.println("repost done");
                        break;
                    case SETTING:
                        Holder holder = (Holder) commandSender.getUser();
                        User updatedUser = holder.getUser();
                        settingUpdate(updatedUser);
                        break;
                    case DELETEACCOUNT:
                        User deletedUser = (User) commandSender.getUser();
                        deleteAccount(deletedUser);
                        break;
                    case MUTE:
                        User muter = commandSender.getUserToFollow();
                        User muted = commandSender.getFollower();
                        mute(muter,muted);
                        break;
                    case UNMUTE:
                        User unMuter = commandSender.getUserToFollow();
                        User unMuted = commandSender.getFollower();
                        unMute(unMuter,unMuted);
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
        CopyOnWriteArrayList<Post> posts = DataBase.loadFollowingPosts(user);
        try {
            objectOutputStream.reset();
            objectOutputStream.writeObject(new CopyOnWriteArrayList<>(posts));
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
        CopyOnWriteArrayList<Comment> comments = DataBase.sendComments(user, post);
        try {
            if (comments != null) {
                objectOutputStream.reset();
                objectOutputStream.writeObject(new CopyOnWriteArrayList<>(comments));
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
                objectOutputStream.writeObject(new CopyOnWriteArrayList<>(comments));
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

    private void search(String username) {
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
        DataBase.
                signupUpdate(user);
    }

    public synchronized void updatePost(Post post, User user) {

        DataBase.updatePost(post, user);

    }

    public synchronized void loadPost(User user) {
        User user1 = DataBase.loadPost(user);
        if (user1 != null)
            try {
                objectOutputStream.writeObject(
                        new CopyOnWriteArrayList<>(user1.getListOfPosts()));
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
    }
}
