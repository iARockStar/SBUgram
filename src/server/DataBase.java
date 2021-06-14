package server;

import javafx.geometry.Pos;
import other.User;

import java.io.*;

import other.*;

import java.util.Collections;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class DataBase {

    private static final String USERS_FILE
            = "C:\\Users\\USER\\IdeaProjects\\signUp.bin";

    private static CopyOnWriteArrayList<User> listOfUsers
            = new CopyOnWriteArrayList<>();


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
            listOfUsers = (CopyOnWriteArrayList<User>) inFromFile.readObject();
            inFromFile.close();
            fin.close();
        } catch (EOFException | StreamCorruptedException e) {
            listOfUsers = new CopyOnWriteArrayList<>();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public synchronized static void login(ObjectOutputStream objectOutputStream
            , String username, String password) throws IOException, ClassNotFoundException {
        ObjectInputStream objectInputStream1 = null;
        try {
            FileInputStream fileInputStream = new FileInputStream(USERS_FILE);
            objectInputStream1 = new ObjectInputStream(fileInputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        CopyOnWriteArrayList<User> users =
                (CopyOnWriteArrayList<User>) objectInputStream1.readObject();
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


    public synchronized static void signupUpdate(User user) {
        listOfUsers.add(user);
        updateUser();
    }

    private synchronized static void updateUser() {
        sortPosts();
        try {
            FileOutputStream fileOutputStream = new FileOutputStream("C:\\Users\\USER\\IdeaProjects\\signUp.bin");
            ObjectOutputStream objectOutputStream1 = new ObjectOutputStream(fileOutputStream);
            objectOutputStream1.writeObject(listOfUsers);
            objectOutputStream1.flush();
        } catch (IOException ignored) {

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
            if (user.equals(listOfUsers.get(i))) {
                listOfUsers.get(i).addPost(post);
                updateUser();
            }
        }

    }


    public synchronized static User loadPost(User user) {
        for (User listUser :
                listOfUsers) {
            if (user.getUsername().equals(listUser.getUsername()))
                return listUser;
        }
        return null;
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

    public synchronized static CopyOnWriteArrayList<Comment> addAndSendComments(User user, Post post, Comment comment) {
        for (User listUser :
                listOfUsers) {
            if (user.getUsername().equals(listUser.getUsername())) {
                if (listUser.getPostToComment() == null)
                    listUser.setPostToComment(post);
                listUser.getPostToComment().getComments().add(comment);
                updateUser();
                return listUser.getPostToComment().getComments();
            }
        }
        return null;
    }

    public static CopyOnWriteArrayList<Comment> sendComments(User user, Post post) {
        for (User listUser :
                listOfUsers) {
            if (user.getUsername().equals(listUser.getUsername())) {
                return listUser.getPostToComment().getComments();
            }
        }
        return null;
    }

    public synchronized static void follow(User following, User follower) {
        for (User listUser :
                listOfUsers) {
            if (following.getUsername().equals(listUser.getUsername())) {
                AtomicInteger x = listUser.getNumOfFollowers();
                listUser.addFollower(follower);
                break;
            }
        }
        for (User listUser :
                listOfUsers) {
            if (follower.getUsername().equals(listUser.getUsername())) {
                AtomicInteger x = listUser.getNumOfFollowings();
                listUser.addFollowing(following);
                updateUser();
                return;
            }
        }
    }

    public synchronized static void unfollow(User following, User follower) {
        for (User listUser :
                listOfUsers) {
            if (following.getUsername().equals(listUser.getUsername())) {
                listUser.removeFollower(follower);
                break;
            }
        }
        for (User listUser :
                listOfUsers) {
            if (follower.getUsername().equals(listUser.getUsername())) {
                listUser.removeFollowing(following);
                updateUser();
                return;
            }
        }
    }

    public static CopyOnWriteArrayList<Post> loadFollowingPosts(User user) {
        CopyOnWriteArrayList<Post> posts = new CopyOnWriteArrayList<>();
        User user2 = null;
        for (User listUser:
             listOfUsers) {
            if(user.getUsername().equalsIgnoreCase(listUser.getUsername())) {
                user2 = listUser;
                break;
            }
        }
        for (User listUser : listOfUsers) {
            if (user2.getFollowings().contains(listUser))
                posts.addAll(listUser.getListOfPosts());
        }
        Collections.sort(posts);
        return posts;
    }

    public synchronized static void like(User user, Post post) {
        for (User listUser : listOfUsers) {
            if (post.getOwner().getUsername().equalsIgnoreCase(listUser.getUsername())) {
                post.getNumOfLikes().addAndGet(1);
                listUser.addLikedPost(post);
                updateUser();
                return;
            }
        }
    }

    public synchronized static void dislike(User user, Post post) {
        for (User listUser : listOfUsers) {
            if (post.getOwner().getUsername().equalsIgnoreCase(listUser.getUsername())) {
                post.getNumOfLikes().addAndGet(-1);
                listUser.removeLikedPost(post);
                updateUser();
                return;
            }
        }
    }

    public static User updateUser(String username) {
        for (User user :
                listOfUsers) {
            if (user.getUsername().equalsIgnoreCase(username))
                return user;
        }
        return null;
    }
}
