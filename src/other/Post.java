package other;

import client.*;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class Post implements Serializable,Comparable {

    private String writer;
    private String title;
    private String description;
    private User owner;
    private byte[] profilePic;
    private byte[] postPic;
    private Date dateTime;
    private CopyOnWriteArrayList<Comment> comments = new CopyOnWriteArrayList<>();
    private AtomicInteger numOfLikes = new AtomicInteger(0);
    private CopyOnWriteArrayList<User> likers = new CopyOnWriteArrayList<>();
    private CopyOnWriteArrayList<User> reposters = new CopyOnWriteArrayList<>();
    private AtomicInteger numOfReposts = new AtomicInteger(0);
    private int postId;
    private static int idCounter = 0;
    private Post referencePost;

    public Post(String writer, String title, String description,Date date, byte[] profilePic, byte[] postPic) {
        this.writer = writer;
        this.title = title;
        this.description = description;
        this.profilePic = profilePic;
        this.postPic = postPic;
        this.dateTime = date;
        this.postId = idCounter++;
        this.referencePost = this;
    }

    public Post(String writer, String title, String description,Date date, byte[] profilePic) {
        this.writer = writer;
        this.title = title;
        this.description = description;
        this.profilePic = profilePic;
        this.dateTime = date;
        this.postId = idCounter++;
        this.referencePost = this;
    }

    public Post() {

    }

    public void addToLikers(User user){
        likers.add(user);
    }

    public void removeFromLikers(User user){
        likers.remove(user);
    }

    public CopyOnWriteArrayList<User> getLikers() {
        return likers;
    }

    public void setLikers(CopyOnWriteArrayList<User> likers) {
        this.likers = likers;
    }

    public void rePost(Post post){
        this.referencePost = post;
    }

    public Post getReferencePost() {
        return referencePost;
    }

    public void setReferencePost(Post referencePost) {
        this.referencePost = referencePost;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date datetime) {
        this.dateTime = datetime;
    }


    public void setOwner(User owner) {
        this.owner = owner;
    }

    public User getOwner() {
        return owner;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public byte[] getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(byte[] profilePic) {
        this.profilePic = profilePic;
    }

    public byte[] getPostPic() {
        return postPic;
    }

    public void setPostPic(byte[] postPic) {
        this.postPic = postPic;
    }

    public CopyOnWriteArrayList<Comment> getComments() {
        return comments;
    }

    public void setComments(CopyOnWriteArrayList<Comment> comments) {
        this.comments = comments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Post post = (Post) o;
        return postId == post.postId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(postId);
    }

    @Override
    public String toString() {
        return title;
    }

    @Override
    public int compareTo(Object o) {
        return ((Post)o).getDateTime().compareTo(this.getDateTime());
    }

    public AtomicInteger getNumOfLikes() {
        return numOfLikes;
    }

    public void setNumOfLikes(AtomicInteger numOfLikes) {
        this.numOfLikes = numOfLikes;
    }

    public AtomicInteger getNumOfReposts() {
        return numOfReposts;
    }

    public void setNumOfReposts(AtomicInteger numOfReposts) {
        this.numOfReposts = numOfReposts;
    }
}
