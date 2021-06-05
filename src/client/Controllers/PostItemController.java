package client.Controllers;

import client.Main;
import client.thisUser;
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

    //each list item will have its exclusive controller in runtime so we set the controller as we load the fxml
    public PostItemController(Post post) throws IOException {
        new PageLoader().load("postItem", this);
        this.post = post;
    }

    //this anchor pane is returned to be set as the list view item
    @Override
    public AnchorPane init() {
        username.setText(post.getWriter());
        title.setText(post.getTitle());
//        description.setText(post.getDescription());

        descriptionLabel.setText(" "+post.getDescription());
        Image image;
        byte[] pic;
        if(thisUser.isAnotherUser()) {
            pic = thisUser.getSearchedUser().getProfileImage();
        }else{
            pic = thisUser.getUser().getProfileImage();
        }
        image = new Image(new ByteArrayInputStream(pic));
        byte[] postPic = post.getPostPic();
        if(postPic!=null) {
            Image postImage = new Image(new ByteArrayInputStream(postPic));
            postImageView.setImage(postImage);
        }
        proPic.setFill(new ImagePattern(image));
        proPic.setEffect(new DropShadow(+25d, 0d,+2d, Color.DARKGREEN));
        Date dateTime = post.getDateTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String dateAsString = dateFormat.format(dateTime);
        date.setText("Published on: "+dateAsString);

        //set another image dynamically
        if (post.getWriter().equals("s"))
            profileImage.setImage(new Image(Paths.get("images/ali_alavi.jpg").toUri().toString()));
        return root;
    }


    public void like(MouseEvent mouseEvent) {

    }

    public void comment(MouseEvent mouseEvent) throws IOException {
        thisUser.getUser().setPostToComment(post);
        Main.loadAPageMouse(mouseEvent
                , "/FXMLs/CommentPage.fxml"
                , "SBUgram - Comment menu"
        );
    }

    public void rePost(MouseEvent mouseEvent) {

    }
    /*
    you can also add on mouse click for like and repost image 
     */


}
