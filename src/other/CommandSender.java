package other;

import java.io.Serializable;
import client.*;

public class CommandSender implements Serializable {

    private CommandType commandType;
    private Object object;
    private Post postToLike;
    private Post postToComment;
    private User userOfThePost;
    private User userWhoWantsToPost;;
    private Comment comment;
    private User userToFollowOrUnfollow;
    private User followerOrUnfollower;
    private User userWhoLiked;
    private User reposter;
    private User userOfTheRepostedPost;
    private Post repostedPost;


    public CommandSender(CommandType commandType, User reposter, User userOfTheRepostedPost, Post repostedPost) {
        this.commandType = commandType;
        this.reposter = reposter;
        this.userOfTheRepostedPost = userOfTheRepostedPost;
        this.repostedPost = repostedPost;
    }

    public CommandSender(CommandType commandType, Object object) {
        this.commandType = commandType;
        this.object =object;
    }

    public CommandSender(CommandType commandType, Post postToComment, User userOfThePost, User userWhoWantsToPost, Comment comment) {
        this.commandType = commandType;
        this.postToComment = postToComment;
        this.userOfThePost = userOfThePost;
        this.userWhoWantsToPost = userWhoWantsToPost;
        this.comment = comment;
    }

    public CommandSender(CommandType commandType, Post postToLike,User userWhoLiked) {
        this.commandType = commandType;
        this.postToLike = postToLike;
        this.userWhoLiked = userWhoLiked;
    }

    public CommandSender(CommandType commandType, User userToFollow, User follower) {
        this.commandType = commandType;
        this.userToFollowOrUnfollow = userToFollow;
        this.followerOrUnfollower = follower;
    }

    public User getFollower() {
        return followerOrUnfollower;
    }

    public User getUserWhoWantsToPost() {
        return userWhoWantsToPost;
    }

    public Object getObject() {
        return object;
    }

    public Post getPostToLike() {
        return postToLike;
    }

    public Post getPostToComment() {
        return postToComment;
    }

    public User getUserOfThePost() {
        return userOfThePost;
    }

    public Comment getComment() {
        return comment;
    }

    public User getUserToFollow() {
        return userToFollowOrUnfollow;
    }

    public CommandType getCommandType() {
        return commandType;
    }

    public Object getUser() {
        return object;
    }

    public User getUserWhoLiked() {
        return userWhoLiked;
    }

    public void setUserWhoLiked(User userWhoLiked) {
        this.userWhoLiked = userWhoLiked;
    }

    public User getReposter() {
        return reposter;
    }

    public void setReposter(User reposter) {
        this.reposter = reposter;
    }

    public User getUserOfTheRepostedPost() {
        return userOfTheRepostedPost;
    }

    public void setUserOfTheRepostedPost(User userOfTheRepostedPost) {
        this.userOfTheRepostedPost = userOfTheRepostedPost;
    }

    public Post getRepostedPost() {
        return repostedPost;
    }

    public void setRepostedPost(Post repostedPost) {
        this.repostedPost = repostedPost;
    }
}
