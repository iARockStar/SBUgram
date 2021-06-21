package client.Controllers;

import client.Client;
import client.Main;
import client.thisUser;
import javafx.event.ActionEvent;
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

public class PostItemController implements ItemController {

    public AnchorPane root;
    public ImageView profileImage;
    public Label username;

    public Text description;
    public Label title;
    public Circle proPic;
    public ImageView postImageView;
    public Label date;
    Post post;
    public Label descriptionLabel;
    public ImageView likeButton;
    public boolean isLiked = false;
    public Label likeLabel;
    public Label repostLabel;

    //each list item will have its exclusive controller in runtime so we set the controller as we load the fxml
    public PostItemController(Post post) throws IOException {
        new PageLoader().load("postItem", this);
        this.post = post;
    }

    @Override
    public AnchorPane init() {
        updateUser();
        username.setText("@" + post.getWriter());
        title.setText(post.getTitle());
        for (Post listPost :
                thisUser.getUser().getPostsLiked()) {
            if (listPost.equals(post)) {
                isLiked = true;
                likeButton.setImage(new Image("/images/heart_outline_480px.png"));
                break;
            }
        }
        descriptionLabel.setText(post.getDescription());
        Image image;
        byte[] pic;
        thisUser.setSearchedUser(post.getOwner());
        pic = post.getOwner().getProfileImage();
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

    private void updateUser() {
        try {
            Client.getObjectOutputStream().reset();
            Client.getObjectOutputStream().writeObject(new CommandSender(CommandType.UPDATEUSER, thisUser.getUser()));
            User user = (User) Client.getObjectInputStream().readObject();
            thisUser.setUser(user);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    public void like(ActionEvent actionEvent) {
        if (!isLiked) {
            likeButton.setImage(new Image("/images/heart_outline_480px.png"));
            isLiked = true;
            post.getNumOfLikes().addAndGet(1);
            post.addToLikers(thisUser.getUser());

            try {
                Client.getObjectOutputStream().reset();
                Client.getObjectOutputStream().writeObject(
                        new CommandSender(CommandType.LIKE, post, thisUser.getUser()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            likeLabel.setText("   " + post.getNumOfLikes() + "\n" + "likes");
            thisUser.getUser().addLikedPost(post);
        } else {
            likeButton.setImage(new Image("/images/heart_512px.png"));
            isLiked = false;
            post.getNumOfLikes().addAndGet(-1);
            post.removeFromLikers(thisUser.getUser());
            try {
                Client.getObjectOutputStream().reset();
                Client.getObjectOutputStream().writeObject(
                        new CommandSender(CommandType.DISLIKE, post, thisUser.getUser()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            likeLabel.setText("   " + post.getNumOfLikes() + "\n" + "likes");

            thisUser.getUser().removeLikedPost(post);
        }

    }

    public void comment(ActionEvent actionEvent) throws IOException {
        thisUser.getUser().setPostToComment(post);
        Main.loadAPage(actionEvent
                , "/FXMLs/CommentPage.fxml"
                , "SBUgram - Comment menu"
        );
    }

    public void rePost(ActionEvent actionEvent) {
        post.setReferencePost(post);
        CommandSender repostCommand = new CommandSender
                (CommandType.REPOST, thisUser.getUser(), post.getOwner(), post);
        try {
            Client.getObjectOutputStream().reset();
            Client.getObjectOutputStream().writeObject(repostCommand);
            ApprovedType approvedType = (ApprovedType) Client.getObjectInputStream().readObject();
            if (approvedType == ApprovedType.APPROVED) {
                post.getNumOfReposts().addAndGet(1);
            }
            repostLabel.setText("   " + post.getNumOfReposts() + "\n" + "Reposts");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
    /*
    you can also add on mouse click for like and repost image 
     */



