package other;

import java.io.Serializable;

/**
 * this class acts like a holder for the information needed to be sent
 * to the server and to be received from it.
 * it has different constructors with different params which
 * hold the information needed.
 */
public class CommandSender implements Serializable {

    private CommandType commandType;
    private Object object;
    private Post postToLike;
    private Post postToComment;
    private String userOfThePost;
    private User userWhoWantsToPost;
    ;
    private Comment comment;
    private User requested;
    private User requester;
    private User userWhoLiked;
    private User reposter;
    private User userOfTheRepostedPost;
    private Post repostedPost;
    private User searcher;
    private String me;
    private String theOtherOne;
    private Message sentMessage;
    private Message editedMessage;
    private String editedText;
    private Post newPost;
    private String owner;
    private User commenter;


    /**
     * this constructor is user for holding the needs of creating a new post.
     * @param createNewPost the commandType
     * @param newPost the newPost
     * @param owner the publisher of the new post
     */
    public CommandSender(CommandType createNewPost, Post newPost, String owner) {
        this.commandType = createNewPost;
        this.newPost = newPost;
        this.owner = owner;
    }

    /**
     * this constructor is user for holding the needs of editing a message
     * @param editMessage the command type
     * @param editedMessage the message which is bout to be deleted
     * @param editedText the new text for the message.
     */
    public CommandSender(CommandType editMessage, Message editedMessage, String editedText) {
        this.commandType = editMessage;
        this.editedMessage = editedMessage;
        this.editedText = editedText;
    }

    /**
     * this constructor is user for holding the needs of sending message
     * @param sendMessage the command type
     * @param sentMessage the message which is bout to be sent to the server
     */
    public CommandSender(CommandType sendMessage, Message sentMessage) {
        this.commandType = sendMessage;
        this.sentMessage = sentMessage;
    }

    /**
     * this constructor is user for holding the needs of reposting a post
     * @param repost the command type
     * @param reposter the user who is reposting the post
     * @param userOfTheRepostedPost the owner of the reposted post
     * @param repostedPost the post which is reposted
     */
    public CommandSender(CommandType repost, User reposter, User userOfTheRepostedPost, Post repostedPost) {
        this.commandType = repost;
        this.reposter = reposter;
        this.userOfTheRepostedPost = userOfTheRepostedPost;
        this.repostedPost = repostedPost;
    }

    /**
     *this constructor is user for holding the needs of deleting,
     * logging in or signing up.
     * @param deleteOrLoginOrSignup the commandType
     * @param object the object which is set according to the commandType.
     */
    public CommandSender(CommandType deleteOrLoginOrSignup, Object object) {
        this.commandType = deleteOrLoginOrSignup;
        this.object = object;
    }

    /**
     * this method is for loading the posts of a user for another user.
     * @param searchUser the commandType.
     * @param nameOfTheSearched the user whom we are looking for
     * @param searcher the user who is loading the page.
     */
    public CommandSender(CommandType searchUser, Object nameOfTheSearched, User searcher) {
        this.commandType = searchUser;
        this.object = nameOfTheSearched;
        this.searcher = searcher;
    }


    /**
     * this constructor is user for holding the needs of commenting
     * @param commentCommand the commandType
     * @param postToComment the comment which the user is posting on
     * @param userOfThePost the username of the owner of the post.
     * @param userWhoWantsToComment the user who wants to comment on the post
     * @param comment the new Comment
     */
    public CommandSender(CommandType commentCommand, Post postToComment, String userOfThePost, User userWhoWantsToComment, Comment comment) {
        this.commandType = commentCommand;
        this.postToComment = postToComment;
        this.userOfThePost = userOfThePost;
        this.commenter = userWhoWantsToComment;
        this.comment = comment;
    }

    /**
     * this constructor is user for holding the needs of liking a post
     * @param like the commandType.
     * @param postToLike the post which is bout to be liked
     * @param userWhoLiked the user who liked the post
     */
    public CommandSender(CommandType like, Post postToLike, User userWhoLiked) {
        this.commandType = like;
        this.postToLike = postToLike;
        this.userWhoLiked = userWhoLiked;
    }


    /**
     * this constructor is user for holding the needs of following,
     * muting and blocking
     * @param request the requested command
     * @param requested the user whom requested the command
     * @param requester the user whom received a requested to be done on.
     */
    public CommandSender(CommandType request, User requested, User requester) {
        this.commandType = request;
        this.requested = requested;
        this.requester = requester;
    }

    /**
     * this constructor is user for holding the needs of receiving the
     * list of chats the user did with another user
     * @param getChats the commandType.
     * @param myUser the user who wants to see his chats list.
     * @param theOtherUser the user whom the main user chatted with.
     */
    public CommandSender(CommandType getChats, String myUser, String theOtherUser) {
        this.commandType = getChats;
        this.me = myUser;
        this.theOtherOne = theOtherUser;
    }

    /**
     * this constructor is user for holding the needs of reposting a post
     * @param repost the commandType.
     * @param user the user who wants to repost
     * @param post the post which is bout to be reposted.
     */
    public CommandSender(CommandType repost, User user, Post post) {
        this.commandType = repost;
        this.reposter = user;
        this.repostedPost = post;
    }


    // getters and setters
    public User getRequester() {
        return requester;
    }

    public User getRequested() {
        return requested;
    }

    public User getUserWhoWantsToPost() {
        return userWhoWantsToPost;
    }


    public Post getPostToLike() {
        return postToLike;
    }

    public Post getPostToComment() {
        return postToComment;
    }

    public String getUserOfThePost() {
        return userOfThePost;
    }

    public Comment getComment() {
        return comment;
    }


    public CommandType getCommandType() {
        return commandType;
    }

    public Object getObject() {
        return object;
    }

    public void setUser(Object object) {
        this.object = object;
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

    public User getSearcher() {
        return searcher;
    }

    public void setSearcher(User searcher) {
        this.searcher = searcher;
    }

    public String getMe() {
        return me;
    }

    public void setMe(String me) {
        this.me = me;
    }

    public String getTheOtherOne() {
        return theOtherOne;
    }

    public void setTheOtherOne(String theOtherOne) {
        this.theOtherOne = theOtherOne;
    }

    public Message getSentMessage() {
        return sentMessage;
    }

    public void setSentMessage(Message sentMessage) {
        this.sentMessage = sentMessage;
    }

    public Message getEditedMessage() {
        return editedMessage;
    }

    public void setEditedMessage(Message editedMessage) {
        this.editedMessage = editedMessage;
    }

    public String getEditedText() {
        return editedText;
    }

    public void setEditedText(String editedText) {
        this.editedText = editedText;
    }

    public Post getNewPost() {
        return newPost;
    }

    public void setNewPost(Post newPost) {
        this.newPost = newPost;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public User getCommenter() {
        return commenter;
    }

    public void setCommenter(User commenter) {
        this.commenter = commenter;
    }
}
