package client.Controllers;

import client.PageLoader;
import client.thisUser;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import other.Comment;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class CommentItemController implements ItemController {
    private Comment thisComment;
    @FXML
    private AnchorPane root;
    @FXML
    private Circle profilePic;
    @FXML
    private Label usernameLabel;
    @FXML
    private Label commentLabel;


    public CommentItemController(Comment comment) throws IOException {
        new PageLoader().load("CommentItem", this);
        thisComment = comment;
    }

    @Override
    public AnchorPane init() {
        Image image;
        byte[] pic;
        if (thisUser.isAnotherUser()) {
            pic = thisUser.getSearchedUser().getProfileImage();
        } else {
            pic = thisUser.getUser().getProfileImage();
        }
        image = new Image(new ByteArrayInputStream(pic));
        profilePic.setFill(new ImagePattern(image));
        profilePic.setEffect(new DropShadow(+25d, 0d, +2d, Color.DARKGREEN));
        usernameLabel.setText(thisUser.getUser().getUsername());
        commentLabel.setText(thisComment.getDescription());

        return root;
    }
}
