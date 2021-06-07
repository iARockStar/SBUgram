package client.Controllers;

import client.Client;
import client.Controllers.mainPage;
import client.PostItem;
import client.thisUser;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import other.CommandSender;
import other.CommandType;
import other.Post;
import other.User;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.CopyOnWriteArrayList;

public class ProfilePageController extends mainPage implements Initializable {

    @FXML
    ListView<Post> postList;
    CopyOnWriteArrayList<Post> posts = new CopyOnWriteArrayList<>();

    @FXML
    Circle profilePic;
    @FXML
    Label usernameLabel;
    @FXML
    Label namePlusLastnameLabel;
    @FXML
    Label birthDateLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        User user = thisUser.getSearchedUser();
        thisUser.setIsAnotherUser(true);
        try {
            loadPosts(user);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }



        //show the post array in list view
        postList.setItems(FXCollections.observableArrayList(posts));

        //customize each cell of postList with new graphic object PostItem
        postList.setCellFactory(postList -> new PostItem());
//        thisUser.setIsAnotherUser(false);

        setProfileDetails();

    }

    private void setProfileDetails() {
        Image image;
        byte[] pic;
        if(thisUser.isAnotherUser()) {
            pic = thisUser.getSearchedUser().getProfileImage();
        }else{
            pic = thisUser.getUser().getProfileImage();
        }
        image = new Image(new ByteArrayInputStream(pic));
        profilePic.setFill(new ImagePattern(image));
        profilePic.setEffect(new DropShadow(+25d, 0d,+2d, Color.DARKGREEN));
        String username = thisUser.getSearchedUser().getUsername();
        String namePlusLastname = thisUser.getSearchedUser().getName()
                +" "+thisUser.getSearchedUser().getLastName();
        String dateOfBirth = thisUser.getSearchedUser().getDatePicker();
        usernameLabel.setText("@"+username);
        namePlusLastnameLabel.setText(namePlusLastname);
        birthDateLabel.setText("birthDate: "+dateOfBirth);
    }

    public void loadPosts(User user) throws IOException {
        Client.getObjectOutputStream().writeObject(new CommandSender(CommandType.LOADAPOST, user));
        try {
            posts = (CopyOnWriteArrayList<Post>) Client.getObjectInputStream().readObject();
        }catch (Exception e){
            posts = new CopyOnWriteArrayList<>();
            e.printStackTrace();
        }
    }

    public void block(ActionEvent event) {
    }

    public void follow(ActionEvent event) {
    }

    public void mute(ActionEvent event) {
    }
}
