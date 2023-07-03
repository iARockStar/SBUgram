package client.Controllers;

import client.Client;
import client.Main;
import client.thisUser;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import other.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;

/**
 * this controller is for the page which allows user to post it's new post.
 */
public class NewPostPageController extends mainPage {

    @FXML
    private JFXTextField title;
    @FXML
    private JFXTextArea description;
    @FXML
    private ImageView postImage;

    private byte[] postPic;

    /**
     * this method creates a new post and sends it
     * to the server in order to add it to
     * user's list of posts.
     */
    public void post(ActionEvent actionEvent) throws IOException, ClassNotFoundException {
        Post post;
        if (postPic != null) {
            post = new Post(thisUser.getUser().getUsername(), title.getText()
                    , description.getText(), new Date(), thisUser.getUser().getProfileImage(), postPic);
            post.setPostPicAddress(file.toString());
        } else {
            post = new Post(thisUser.getUser().getUsername(), title.getText()
                    , description.getText(), new Date(), thisUser.getUser().getProfileImage());
            post.setPostPicAddress("No postPic");
        }

        post.setOwner(thisUser.getUser().getUsername());

        CommandSender commandSender = new CommandSender(CommandType.NEWPOST
                , post);

        Client.getObjectOutputStream().writeObject(commandSender);

        thisUser.setSearchedUser(thisUser.getUser());
        Main.loadAPage(actionEvent
                , "../FXMLs/MyProfile.fxml"
                , "SBUgram - Your Profile"
        );

    }

    private File file;

    /**
     * adds a picture to the post if the user intends to.
     */
    public void addPicture() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Upload your pic for this post");
        file = chooser.showOpenDialog(null);
        if (file != null) {

            Image profilePicImage = new Image(file.toURI().toString());
            try {
                this.postPic = new FileInputStream(file).readAllBytes();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            postImage.setImage(profilePicImage);
        }
    }
}
