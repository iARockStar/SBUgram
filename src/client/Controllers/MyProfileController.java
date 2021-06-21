package client.Controllers;

import client.Client;
import client.Main;
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
import javafx.scene.input.MouseEvent;
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
import java.util.Vector;

/**
 * this class is for loading the page of the user itself
 * this page shows the timeline of the posts of the user
 * and shows users' personal info.
 * it also allows user to update his/her profile.
 */
public class MyProfileController extends mainPage {
    /**
     * list of users' posts.
     */
    @FXML
    ListView<Post> postList;
    Vector<Post> posts = new Vector<>();

    @FXML
    Label usernameLabel;
    @FXML
    Label namePlusLastnameLabel;
    @FXML
    Label birthDateLabel;
    @FXML
    Circle profilePic;
    @FXML
    Label followerLabel;
    @FXML
    Label followingLabel;

    private User user;

    /**
     * this method is called before any other method and
     * sets everything refreshed for the user(posts and the number of followers
     * and followings).
     */
    @FXML
    public void initialize() {
        updateUser();
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

    /**
     * sets user's personal info.
     */
    private void setProfileDetails() {
        Image image;
        byte[] pic;
        pic = thisUser.getUser().getProfileImage();
        image = new Image(new ByteArrayInputStream(pic));
        profilePic.setFill(new ImagePattern(image));
        profilePic.setEffect(new DropShadow(+25d, 0d, +2d, Color.DARKGREEN));
        String username = thisUser.getUser().getUsername();
        String namePlusLastname = thisUser.getUser().getName()
                + " " + thisUser.getUser().getLastName();
        String dateOfBirth = thisUser.getUser().getDatePicker();
        usernameLabel.setText("@" + username);
        namePlusLastnameLabel.setText(namePlusLastname);
        birthDateLabel.setText("birthDate: " + dateOfBirth);
        followerLabel.setText(user.getNumOfFollowers() + " Followers");
        followingLabel.setText(user.getNumOfFollowings() + " Followings");
    }

    /**
     * it is called in init method and updates user information.
     */
    private void updateUser() {
        try {
            Client.getObjectOutputStream().reset();
            Client.getObjectOutputStream().writeObject(new CommandSender(CommandType.UPDATEUSER, thisUser.getUser()));
            user = (User) Client.getObjectInputStream().readObject();
            thisUser.setUser(user);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    /**
     * it's called in init method and loads updated list of posts.
     */
    public void loadPosts(User user) throws IOException {
        Client.getObjectOutputStream().writeObject(new CommandSender(CommandType.LOADAPOST, user));
        try {
            posts = (Vector<Post>) Client.getObjectInputStream().readObject();
        } catch (Exception e) {
            posts = new Vector<>();
            e.printStackTrace();
        }
    }

    /**
     * loads SettingPageController
     */
    public void setting(ActionEvent actionEvent) throws IOException {
        Main.loadAPage(actionEvent
                , "../FXMLs/settingPage.fxml"
                , "SBUgram - change your info"
        );
    }

    /**
     * it recalls the init method for refreshing the feed and user's
     * personal info.
     */
    public void refresh(MouseEvent mouseEvent) {
        this.initialize();
    }
}
