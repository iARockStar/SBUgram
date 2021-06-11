package other;

import java.io.Serializable;
import client.*;

public class CommandSender implements Serializable {

    private CommandType commandType;
    private Object object;
    private Post postToLike;
    private Post postToComment;
    private User userOfThePost;
    private Comment comment;
    private User userToFollow;


    public CommandSender(CommandType commandType, Object object) {
        this.commandType = commandType;
        this.object =object;
    }

    public CommandSender(CommandType commandType, Post postToComment, User userOfThePost, Comment comment) {
        this.commandType = commandType;
        this.postToComment = postToComment;
        this.userOfThePost = userOfThePost;
        this.comment = comment;
    }

    public CommandSender(CommandType commandType, Post postToLike, User userOfThePost) {
        this.commandType = commandType;
        this.postToLike = postToLike;
        this.userOfThePost = userOfThePost;
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
        return userToFollow;
    }

    public CommandType getCommandType() {
        return commandType;
    }

    public Object getUser() {
        return object;
    }
}
