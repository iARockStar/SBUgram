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
import java.util.concurrent.CompletionException;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class ProfilePageController extends mainPage {

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
    @FXML
    private JFXCheckBox muteCheckBox;

    @FXML
    public void initialize() {
//        updateUser();
        User user = thisUser.getSearchedUser();
        User myUser = thisUser.getUser();
        thisUser.setIsAnotherUser(true);
        for (User listUser :
                myUser.getFollowings()) {
            if (user.getUsername().equals(listUser.getUsername())) {
                muteCheckBox.setVisible(true);
                followCheckbox.setSelected(true);
                if(myUser.getMutedList().contains(listUser))
                    muteCheckBox.setSelected(true);
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
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void setProfileDetails() {
//        updateUser();
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
            followUser();
        } else {
            unfollowUser();
        }
    }

    private void unfollowUser() {
        try {
            muteCheckBox.setVisible(false);
            CommandSender commandSender = new CommandSender(CommandType.UNFOLLOW, thisUser.getUser(), thisUser.getSearchedUser());
            thisUser.getSearchedUser().removeFollower(thisUser.getUser());
            thisUser.getUser().removeFollowing(thisUser.getSearchedUser());
            Client.getObjectOutputStream().writeObject(commandSender);
            AtomicInteger atomicInteger = (AtomicInteger) Client.getObjectInputStream().readObject();
            followerLabel.setText(atomicInteger + " Followers");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void followUser() {
        try {
            muteCheckBox.setVisible(true);
            CommandSender commandSender = new CommandSender(CommandType.FOLLOW, thisUser.getUser(), thisUser.getSearchedUser());
            thisUser.getSearchedUser().addFollower(thisUser.getUser());
            thisUser.getUser().addFollowing(thisUser.getSearchedUser());
            Client.getObjectOutputStream().writeObject(commandSender);
            AtomicInteger atomicInteger = (AtomicInteger) Client.getObjectInputStream().readObject();
            followerLabel.setText(atomicInteger + " Followers");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void mute(ActionEvent event) {
        if (muteCheckBox.isSelected()) {
            muteUser();
        }else{
            unmuteUser();
        }
    }

    private void muteUser() {
        CommandSender commandSender = new CommandSender(
                CommandType.MUTE, thisUser.getUser(), thisUser.getSearchedUser()
        );
        try {
            thisUser.getUser().getMutedList().add(thisUser.getSearchedUser());
            Client.getObjectOutputStream().reset();
            Client.getObjectOutputStream().writeObject(commandSender);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void unmuteUser() {
        CommandSender commandSender = new CommandSender(
                CommandType.UNMUTE, thisUser.getUser(), thisUser.getSearchedUser()
        );
        try {
            thisUser.getUser().getMutedList().remove(thisUser.getSearchedUser());
            Client.getObjectOutputStream().reset();
            Client.getObjectOutputStream().writeObject(commandSender);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void refresh(MouseEvent mouseEvent) {
        this.initialize();
    }
}
