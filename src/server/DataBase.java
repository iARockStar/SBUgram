package server;

import other.User;

import java.io.*;

import other.*;

import java.util.Collections;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicInteger;

public class DataBase {

    private static final String USERS_FILE
            = "C:\\Users\\USER\\IdeaProjects\\signUp.bin";

    private static Vector<User> listOfUsers
            = new Vector<>();


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
        if(!listOfUsers.contains(user)) {
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
            ObjectOutputStream objectOutputStream1 = new ObjectOutputStream(fileOutputStream);
            objectOutputStream1.writeObject(listOfUsers);
            objectOutputStream1.flush();
            fileOutputStream.close();
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
                if(!following.getBlockedList().contains(follower.getUsername())) {
                    listUser.addFollower(follower.getUsername());
                    atomicInteger = listUser.getNumOfFollowers();
                }else{
                    atomicInteger = new AtomicInteger(-1);
                }
                break;
            }
        }
        if(!atomicInteger.equals(new AtomicInteger(-1))) {
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
                listUser.addLikedPost(post);
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
                listUser.removeLikedPost(post);
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
        for (User listUser:
             listOfUsers) {
            if(listUser.getUsername().equals(blocker.getUsername())){
                if(listUser.getFollowings().contains(blocked.getUsername())){
                    listUser.removeFollowing(blocked.getUsername());
                    blocked.removeFollower(listUser.getUsername());

                }
                listUser.addToBlockedList(blocked.getUsername());
                break;
            }
        }
        updateUser();
    }

    public synchronized static void unBlock(User unBlocker, User unblocked) {
        for (User listUser:
             listOfUsers) {
            if(listUser.getUsername().equals(unBlocker.getUsername())){
                if(listUser.getFollowings().contains(unblocked.getUsername())){
                    listUser.addFollowing(unblocked.getUsername());
                    unblocked.addFollower(listUser.getUsername());
                }
                listUser.removeFromBlockedList(unblocked.getUsername());
                break;
            }
        }
        updateUser();
    }
}
