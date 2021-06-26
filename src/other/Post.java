package other;

import client.*;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;
import java.util.Vector;

import java.util.concurrent.atomic.AtomicInteger;
/**
 * this class is for posts and saving the information of one post at a time.
 */
public class Post implements Serializable, Comparable {

    private String writer;
    private String title;
    private String description;
    private String owner;
    private byte[] profilePic;
    private byte[] postPic;
    private String postPicAddress;
    private Date dateTime;
    private Vector<Comment> comments = new Vector<>();
    private AtomicInteger numOfLikes = new AtomicInteger(0);
    private Vector<String> likers = new Vector<>();
    private Vector<String> reposters = new Vector<>();
    private AtomicInteger numOfReposts = new AtomicInteger(0);
    //id which helps the post to be unique.
    private  int postId;
    /**
     * idCounter is a static field which increases ny each instantiation.
     * so it helps the posts to be unique nhy assigning
     * a unique id to each post.
     */
    private static AtomicInteger idCounter = new AtomicInteger(0);

    public static void setIdCounter(AtomicInteger idCounter) {
        Post.idCounter = idCounter;
    }

    public static AtomicInteger getIdCounter() {
        return idCounter;
    }

    public Post(String writer, String title, String description, Date date, byte[] profilePic, byte[] postPic) {
        this.writer = writer;
        this.title = title;
        this.description = description;
        this.profilePic = profilePic;
        this.postPic = postPic;
        this.dateTime = date;
    }

    public Post(String writer, String title, String description, Date date, byte[] profilePic) {
        this.writer = writer;
        this.title = title;
        this.description = description;
        this.profilePic = profilePic;
        this.dateTime = date;
    }

    public Post() {
        this.postId = idCounter.getAndAdd(+1);
    }

    //getters and setters
    public int getPostId() {
        return postId;
    }

    public void setPostId() {
        this.postId = idCounter.getAndAdd(1);
    }

    public void addToLikers(String username) {
        likers.add(username);
    }

    public void removeFromLikers(String username) {
        likers.remove(username);
    }

    public Vector<String> getLikers() {
        return likers;
    }

    public void setLikers(Vector<String> likers) {
        this.likers = likers;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date datetime) {
        this.dateTime = datetime;
    }


    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getOwner() {
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

    public Vector<Comment> getComments() {
        return comments;
    }

    public void setComments(Vector<Comment> comments) {
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
    public String toString() {
        return title;
    }

    @Override
    public int compareTo(Object o) {

        return ((Post) o).getDateTime().compareTo(this.getDateTime());
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

    public Vector<String> getReposters() {
        return reposters;
    }

    public void setReposters(Vector<String> reposters) {
        this.reposters = reposters;
    }

    public void addToRepsters(String username) {
        reposters.add(username);
    }

    public void removeFromReposters(User user) {
        reposters.remove(user.getUsername());
    }

    public String getPostPicAddress() {
        return postPicAddress;
    }

    public void setPostPicAddress(String postPicAddress) {
        this.postPicAddress = postPicAddress;
    }
}
