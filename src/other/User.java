package other;

import javafx.geometry.Pos;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class User implements Serializable {
    private String name;
    private String lastName;
    private String username;
    private String password;
    private String phoneNumber;
    private SecurityQuestion securityQuestion;
    private String datePicker;
    private String email;
    private CopyOnWriteArrayList<Post> listOfPosts = new CopyOnWriteArrayList<>();
    private byte[] profileImage;
    private Post postToComment = new Post();
    private Comment newComment;
    private CopyOnWriteArrayList<User> followers = new CopyOnWriteArrayList<>();
    private AtomicInteger numOfFollowers = new AtomicInteger(0);
    private AtomicInteger numOfFollowings = new AtomicInteger(0);
    private CopyOnWriteArrayList<User> followings = new CopyOnWriteArrayList<>();
    private CopyOnWriteArrayList<Post> postsLiked = new CopyOnWriteArrayList<>();


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



    public void addFollower(User follower){
        followers.add(follower);
        numOfFollowers.addAndGet(1);
    }

    public void addFollowing(User following){
        followings.add(following);
        numOfFollowings.addAndGet(1);
    }

    public void removeFollower(User follower){
        followers.remove(follower);
        numOfFollowers.addAndGet(-1);
    }

    public void addLikedPost(Post post){
        postsLiked.add(post);
    }

    public void removeLikedPost(Post post){
        postsLiked.remove(post);
    }


    public void removeFollowing(User following){
        followings.remove(following);
        numOfFollowings.addAndGet(-1);
    }

    public CopyOnWriteArrayList<User> getFollowers() {
        return followers;
    }

    public CopyOnWriteArrayList<User> getFollowings() {
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

    public CopyOnWriteArrayList<Post> getListOfPosts() {
        return listOfPosts;
    }

    public void setListOfPosts(CopyOnWriteArrayList<Post> listOfPosts) {
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


    public CopyOnWriteArrayList<Post> getPostsLiked() {
        return postsLiked;
    }

    public void setPostsLiked(CopyOnWriteArrayList<Post> postsLiked) {
        this.postsLiked = postsLiked;
    }
}
