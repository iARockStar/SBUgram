package server;

import other.User;
import java.io.*;
import other.*;

import java.util.Collections;
import java.util.concurrent.CopyOnWriteArrayList;

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
        for (User user:
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
                if(user.getSecurityQuestion().equals(listUser.getSecurityQuestion())) {
                    listUser.setPassword(user.getPassword());
                    updateUser();
                    return true;
                }else return false;
            }
        }
        return false;
    }

    public synchronized static User search(String username) {
        for (User listUser :
                listOfUsers) {
            if(listUser.getUsername().equalsIgnoreCase(username))
                return listUser;
        }
        return null;
    }

    public synchronized static CopyOnWriteArrayList<Comment> addAndSendComments(Comment comment) {
        User user = comment.getOwner();
        Post post = comment.getPost();
        for (User listUser:
             listOfUsers) {
            if(user.getUsername().equals(listUser.getUsername())) {
                listUser.setPostToComment(post);
                listUser.getPostToComment().getComments().add(comment);
                updateUser();
                return listUser.getPostToComment().getComments();
            }
        }
        return null;
    }

    public static CopyOnWriteArrayList<Comment> sendComments(Post post) {
        User user = post.getOwner();
        for (User listUser:
                listOfUsers) {
            if(user.equals(listUser)) {
                return listUser.getPostToComment().getComments();
            }
        }
        return null;
    }
}
