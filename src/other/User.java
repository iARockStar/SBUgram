package other;

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;
import java.util.Vector;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * this class holds everything needed for a new profile.
 * it includes the list of followers and blocked and followed and muted people.
 */
public class User implements Serializable {
    private String name;
    private String lastName;
    private String username;
    private String password;
    private String phoneNumber;
    private SecurityQuestion securityQuestion;
    private String datePicker;
    private String email;
    private Vector<Post> listOfPosts = new Vector<>();
    private byte[] profileImage;
    private String profilePicAddress;
    private Post postToComment = new Post();
    private Comment newComment;
    private Vector<String> followers = new Vector<>();
    private AtomicInteger numOfFollowers = new AtomicInteger(0);
    private AtomicInteger numOfFollowings = new AtomicInteger(0);
    private Vector<String> followings = new Vector<>();
    private Vector<Integer> postsLiked = new Vector<>();
    private Vector<String> mutedList = new Vector<>();
    private Vector<String> blockedList = new Vector<>();
    private Map<String, Vector<Message>> sent = new ConcurrentHashMap<>();
    private Map<String, Vector<Message>> received = new ConcurrentHashMap<>();
    private Vector<UserList> users = new Vector<>();


    public User(String name, String lastName, String username, String password, String phoneNumber, SecurityQuestion securityQuestion, String datePicker, String email) {
        this.name = name;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.securityQuestion = securityQuestion;
        this.datePicker = datePicker;
        this.email = email;
    }

    public User(String name, String lastName, String username, String password, String phoneNumber, SecurityQuestion securityQuestion, String datePicker, String email, byte[] profileImage) {
        this.name = name;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.securityQuestion = securityQuestion;
        this.datePicker = datePicker;
        this.email = email;

        this.profileImage = profileImage;
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User(String username) {
        this.username = username;
    }

    public User(String password, SecurityQuestion securityQuestion, String username) {
        this.password = password;
        this.securityQuestion = securityQuestion;
        this.username = username;
    }


    public void addFollower(String follower) {
        followers.add(follower);
        numOfFollowers.addAndGet(1);
    }

    public void addFollowing(String following) {
        followings.add(following);
        numOfFollowings.addAndGet(1);
    }

    public void removeFollower(String follower) {
        followers.remove(follower);
        numOfFollowers.addAndGet(-1);
    }

    public void addLikedPost(Integer postId) {
        postsLiked.add(postId);
    }

    public void removeLikedPost(Integer postId) {
        postsLiked.remove(postId);
    }


    public void removeFollowing(String following) {
        followings.remove(following);
        numOfFollowings.addAndGet(-1);
    }


    public void addMuted(String username) {
        mutedList.add(username);
    }

    public void removeMuted(User user) {
        mutedList.remove(user.getUsername());
    }

    public Vector<String> getFollowers() {
        return followers;
    }

    public Vector<String> getFollowings() {
        return followings;
    }

    public AtomicInteger getNumOfFollowers() {
        return numOfFollowers;
    }

    public AtomicInteger getNumOfFollowings() {
        return numOfFollowings;
    }

    public void setNumOfFollowers(AtomicInteger numOfFollowers) {
        this.numOfFollowers = numOfFollowers;
    }

    public void setNumOfFollowings(AtomicInteger numOfFollowings) {
        this.numOfFollowings = numOfFollowings;
    }

    public Comment getNewComment() {
        return newComment;
    }

    public void setNewComment(Comment newComment) {
        this.newComment = newComment;
    }

    public Post getPostToComment() {
        return postToComment;
    }

    public void setPostToComment(Post postToComment) {
        this.postToComment = postToComment;
    }

    public void addPost(Post post) {
        listOfPosts.add(post);
    }

    public Vector<Post> getListOfPosts() {
        return listOfPosts;
    }

    public void setListOfPosts(Vector<Post> listOfPosts) {
        this.listOfPosts = listOfPosts;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public SecurityQuestion getSecurityQuestion() {
        return securityQuestion;
    }

    public void setSecurityQuestion(SecurityQuestion securityQuestion) {
        this.securityQuestion = securityQuestion;
    }

    public String getDatePicker() {
        return datePicker;
    }

    public void setDatePicker(String datePicker) {
        this.datePicker = datePicker;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public byte[] getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(byte[] profileImage) {
        this.profileImage = profileImage;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(username, user.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }


    public Vector<Integer> getPostsLiked() {
        return postsLiked;
    }

    public void setPostsLiked(Vector<Integer> postsLiked) {
        this.postsLiked = postsLiked;
    }

    public Vector<String> getMutedList() {
        return mutedList;
    }

    public void setMutedList(Vector<String> mutedList) {
        this.mutedList = mutedList;
    }

    public String getProfilePicAddress() {
        return profilePicAddress;
    }

    public void setProfilePicAddress(String profilePicAddress) {
        this.profilePicAddress = profilePicAddress;
    }

    public Vector<String> getBlockedList() {
        return blockedList;
    }

    public void setBlockedList(Vector<String> blockedList) {
        this.blockedList = blockedList;
    }

    public void addToBlockedList(String username) {
        blockedList.add(username);
    }

    public void removeFromBlockedList(String username) {
        blockedList.remove(username);
    }

    public void addToSent(String username,Message message){
        Vector<Message> listOfMessages = this.sent.get(username);
        listOfMessages.add(message);
        this.sent.put(username,listOfMessages);
    }

    public void addToReceived(String username,Message message){
        Vector<Message> listOfMessages = this.received.get(username);
        listOfMessages.add(message);
        this.received.put(username,listOfMessages);
    }

    public Vector<UserList> getUsers() {
        return users;
    }

    public void setUsers(Vector<UserList> users) {
        this.users = users;
    }

    public void addToUsers(UserList userList){
        users.add(userList);
    }
}
