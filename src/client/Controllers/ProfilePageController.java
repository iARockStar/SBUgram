package client.Controllers;

import client.Client;
import client.Controllers.mainPage;
import client.PostItem;
import client.thisUser;
import com.jfoenix.controls.JFXCheckBox;
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
    @FXML
    JFXCheckBox followCheckbox;
    @FXML
    Label followingLabel;
    @FXML
    Label followerLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        updateUser();
        User user = thisUser.getSearchedUser();
        User myUser = thisUser.getUser();
        thisUser.setIsAnotherUser(true);
        for (User listUser :
                myUser.getFollowings()) {
            if (user.getUsername().equals(listUser.getUsername())) {
                followCheckbox.setSelected(true);
                break;
            }
        }
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

    private void updateUser() {
        try {
            Client.getObjectOutputStream().writeObject(new CommandSender(CommandType.UPDATEUSER, thisUser.getUser()));
            User user = (User) Client.getObjectInputStream().readObject();
            thisUser.setUser(user);
        }catch (IOException e){
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void setProfileDetails() {
        updateUser();
        Image image;
        byte[] pic;
        if (thisUser.isAnotherUser()) {
            pic = thisUser.getSearchedUser().getProfileImage();
        } else {
            pic = thisUser.getUser().getProfileImage();
        }
        User user = thisUser.getSearchedUser();
        image = new Image(new ByteArrayInputStream(pic));
        profilePic.setFill(new ImagePattern(image));
        profilePic.setEffect(new DropShadow(+25d, 0d, +2d, Color.DARKGREEN));
        String username = thisUser.getSearchedUser().getUsername();
        String namePlusLastname = thisUser.getSearchedUser().getName()
                + " " + thisUser.getSearchedUser().getLastName();
        String dateOfBirth = thisUser.getSearchedUser().getDatePicker();
        usernameLabel.setText("@" + username);
        namePlusLastnameLabel.setText(namePlusLastname);
        birthDateLabel.setText("birthDate: " + dateOfBirth);
        followerLabel.setText(thisUser.getSearchedUser().getNumOfFollowers() + " Followers");
        followingLabel.setText(thisUser.getSearchedUser().getNumOfFollowings() + " Followings");
    }

    public void loadPosts(User user) throws IOException {
        Client.getObjectOutputStream().writeObject(new CommandSender(CommandType.LOADAPOST, user));
        try {
            posts = (CopyOnWriteArrayList<Post>) Client.getObjectInputStream().readObject();
        } catch (Exception e) {
            posts = new CopyOnWriteArrayList<>();
            e.printStackTrace();
        }
    }

    public void block(ActionEvent event) {
    }

    public void follow(ActionEvent event) {
        if (followCheckbox.isSelected()) {
            try {
                CommandSender commandSender = new CommandSender(CommandType.FOLLOW, thisUser.getUser(), thisUser.getSearchedUser());
                thisUser.getSearchedUser().addFollower(thisUser.getUser());
                thisUser.getUser().addFollowing(thisUser.getSearchedUser());
//                followerLabel.setText(thisUser.getSearchedUser().getNumOfFollowers() + " Followers");
                Client.getObjectOutputStream().writeObject(commandSender);
                followerLabel.setText(thisUser.getSearchedUser().getNumOfFollowers() + " Followers");
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        } else {
            try {
                CommandSender commandSender = new CommandSender(CommandType.UNFOLLOW, thisUser.getUser(), thisUser.getSearchedUser());
                thisUser.getSearchedUser().removeFollower(thisUser.getUser());
                thisUser.getUser().removeFollowing(thisUser.getSearchedUser());
//                followerLabel.setText(thisUser.getSearchedUser().getNumOfFollowers() + " Followers");
                Client.getObjectOutputStream().writeObject(commandSender);
                followerLabel.setText(thisUser.getSearchedUser().getNumOfFollowers() + " Followers");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void mute(ActionEvent event) {
    }
}
