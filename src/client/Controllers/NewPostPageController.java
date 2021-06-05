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

public class NewPostPageController extends mainPage {

    @FXML
    private JFXTextField title;
    @FXML
    private JFXTextArea description;
    @FXML
    private ImageView postImage;

    private byte[] postPic;

    public void post(ActionEvent actionEvent) throws IOException, ClassNotFoundException {
        Post post;
        if (postPic != null)
            post = new Post(thisUser.getUser().getUsername(), title.getText()
                    , description.getText(),new Date(), thisUser.getUser().getProfileImage(), postPic);
        else {
            post = new Post(thisUser.getUser().getUsername(), title.getText()
                    , description.getText(),new Date(), thisUser.getUser().getProfileImage());
        }

        post.setOwner(thisUser.getUser());

        CommandSender commandSender = new CommandSender(CommandType.NEWPOST
                , post);

        Client.getObjectOutputStream().writeObject(commandSender);



        Main.loadAPage(actionEvent
                , "../FXMLs/MainPage.fxml"
                , "SBUgram - Home page"
        );
    }

    public void addPicture(ActionEvent event) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Upload your pic for this post");
        File file = chooser.showOpenDialog(null);
        if (file != null) {
            System.out.println(file.toString());
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
