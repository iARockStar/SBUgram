package client.Controllers;

import client.Client;
import client.Main;
import client.thisUser;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.CopyOnWriteArrayList;

import other.*;
import client.PageLoader;
/**
 * this class sets the information for each cell of the posts' list.
 */
public class PostItemController implements ItemController {

    @FXML
    private AnchorPane root;
    @FXML
    private ImageView profileImage;
    @FXML
    private Label username;

    @FXML
    private Text description;
    @FXML
    private Label title;
    @FXML
    private Circle proPic;
    @FXML
    private ImageView postImageView;
    @FXML
    private Label date;
    private Post post;
    @FXML
    private Label descriptionLabel;
    @FXML
    private ImageView likeButton;
    private boolean isLiked = false;
    @FXML
    private Label likeLabel;
    @FXML
    private Label repostLabel;


    /**
     * a constructor which loads the commentItem's FXMl.
     * @param post param which is bout to be set
     */
    public PostItemController(Post post) throws IOException {
        new PageLoader().load("postItem", this);
        this.post = post;
    }

    /**
     * this method sets the post's information
     */
    @Override
    public AnchorPane init() {
        username.setText("@" + post.getWriter());
        title.setText(post.getTitle());
        for (Integer listPost :
                thisUser.getUser().getPostsLiked()) {
            if (listPost.equals(post.getPostId())) {
                isLiked = true;
                likeButton.setImage(new Image("/images/heart_outline_480px.png"));
                break;
            }
        }
        descriptionLabel.setText(post.getDescription());
        Image image;
        byte[] pic;
        pic = post.getProfilePic();
        image = new Image(new ByteArrayInputStream(pic));
        byte[] postPic = post.getPostPic();
        if (postPic != null) {
            Image postImage = new Image(new ByteArrayInputStream(postPic));
            postImageView.setImage(postImage);
        }
        proPic.setFill(new ImagePattern(image));
        proPic.setEffect(new DropShadow(+25d, 0d, +2d, Color.DARKGREEN));
        Date dateTime = post.getDateTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String dateAsString = dateFormat.format(dateTime);
        date.setText("Published on: " + dateAsString);
        likeLabel.setText("   " + post.getNumOfLikes() + "\n" + "likes");
        repostLabel.setText("   " + post.getNumOfReposts() + "\n" + "Reposts");
        return root;
    }


    /**
     * when the user hits the like button this method is called and
     * then if the like button was not selected then it is a like.
     * otherwise the command sent to the server would be disliking this post.
     */
    public void like(ActionEvent actionEvent) {
        if (!isLiked) {
            likeThePost();
        } else {
            dislikeThePost();
        }

    }

    /**
     * this method sends a dislike command to the server.
     */
    private void dislikeThePost() {
        likeButton.setImage(new Image("/images/heart_512px.png"));
        isLiked = false;
        post.getNumOfLikes().addAndGet(-1);
        post.removeFromLikers(thisUser.getUser().getUsername());
        try {
            Client.getObjectOutputStream().reset();
            Client.getObjectOutputStream().writeObject(
                    new CommandSender(CommandType.DISLIKE, post, thisUser.getUser()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        likeLabel.setText("   " + post.getNumOfLikes() + "\n" + "likes");

        thisUser.getUser().removeLikedPost(post.getPostId());
    }


    /**
     * this method sends a like command to the server.
     */
    private void likeThePost() {
        likeButton.setImage(new Image("/images/heart_outline_480px.png"));
        isLiked = true;
        post.getNumOfLikes().addAndGet(1);
        post.addToLikers(thisUser.getUser().getUsername());

        try {
            Client.getObjectOutputStream().reset();
            Client.getObjectOutputStream().writeObject(
                    new CommandSender(CommandType.LIKE, post, thisUser.getUser()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        likeLabel.setText("   " + post.getNumOfLikes() + "\n" + "likes");
        thisUser.getUser().addLikedPost(post.getPostId());
    }

    /**
     * this method is used for loading the comment section
     */
    public void comment(ActionEvent actionEvent) throws IOException {
        thisUser.getUser().setPostToComment(post);
        Main.loadAPage(actionEvent
                , "/FXMLs/CommentPage.fxml"
                , "SBUgram - Comment menu"
        );
    }

    /**
     * this method is used for reposting.
     * <p>
     * if the user has already reposted the post then it will be not possible to repost
     * again; otherwise when the user hits the repost button one repost is added
     * to his / her list of posts.
     */
    public void rePost(ActionEvent actionEvent) {
        CommandSender repostCommand = new CommandSender
                (CommandType.REPOST, thisUser.getUser(), post);
        try {
            Client.getObjectOutputStream().reset();
            Client.getObjectOutputStream().writeObject(repostCommand);
            ApprovedType approvedType = (ApprovedType) Client.getObjectInputStream().readObject();
            if (approvedType == ApprovedType.APPROVED) {
                post.getNumOfReposts().addAndGet(1);
            }
            repostLabel.setText("   " + post.getNumOfReposts() + "\n" + "Reposts");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * this method is called when the user hits the post's publisher
     * it sends the username to the server and receives an updated user.
     * then it loads the profile page of the received user.
     */
    public void loadUserPage(ActionEvent event) throws IOException {
        String username = this.username.getText().replaceFirst("@","");
        CommandType searchUserCommand = CommandType.SEARCHUSER;
        CommandSender searchTheServer =
                new CommandSender(searchUserCommand, username, thisUser.getUser());
        try {
            Client.getObjectOutputStream().reset();
            Client.getObjectOutputStream().writeObject(searchTheServer);
            Object object;
            User user;
            if ((object = Client.getObjectInputStream().readObject()) instanceof User) {
                user = (User) object;
                thisUser.setSearchedUser(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (thisUser.getSearchedUser().getUsername()
                .equalsIgnoreCase(thisUser.getUser().getUsername()))
            Main.loadAPage(event
                    , "../FXMLs/MyProfile.fxml"
                    , "SBUgram - Your profile"
            );
        else
            Main.loadAPage(event
                    , "../FXMLs/ProfilePage.fxml"
                    , "SBUgram - Profile page"
            );
    }
}


