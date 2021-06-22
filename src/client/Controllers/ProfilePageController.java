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
import java.util.Vector;
import java.util.concurrent.CompletionException;

import java.util.concurrent.atomic.AtomicInteger;

public class ProfilePageController extends mainPage {

    @FXML
    ListView<Post> postList;
    Vector<Post> posts = new Vector<>();

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
    private JFXCheckBox blockCheckbox;
    @FXML
    private Label errorLabel;

    @FXML
    public void initialize() {
        updateUser();
        updateMyUser(thisUser.getUser());
        User user = thisUser.getSearchedUser();
        User myUser = thisUser.getUser();
        System.out.println(myUser.getBlockedList());
        if(myUser.getBlockedList().contains(thisUser.getSearchedUser().getUsername()));
            blockCheckbox.setSelected(true);
        for (String listUser :
                myUser.getFollowings()) {
            if (user.getUsername().equals(listUser)) {
                muteCheckBox.setVisible(true);
                followCheckbox.setSelected(true);
                if (myUser.getMutedList().contains(listUser))
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

    private void updateMyUser(User user) {
        try {
            Client.getObjectOutputStream().reset();
            Client.getObjectOutputStream().writeObject(new CommandSender(CommandType.UPDATEUSER, user));
            User newUser = (User) Client.getObjectInputStream().readObject();
            thisUser.setUser(newUser);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * it is called in init method and updates user information.
     */
    private void updateUser() {
        try {
            Client.getObjectOutputStream().reset();
            Client.getObjectOutputStream().writeObject(new CommandSender(CommandType.UPDATEUSER, thisUser.getSearchedUser()));
            User newUser = (User) Client.getObjectInputStream().readObject();
            thisUser.setSearchedUser(newUser);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void setProfileDetails() {
        Image image;
        byte[] pic;
        pic = thisUser.getSearchedUser().getProfileImage();
        User searchedUser = thisUser.getSearchedUser();
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
        followerLabel.setText(searchedUser.getNumOfFollowers() + " Followers");
        followingLabel.setText(searchedUser.getNumOfFollowings() + " Followings");
    }

    public void loadPosts(User user) throws IOException {
        Client.getObjectOutputStream().reset();
        Client.getObjectOutputStream().writeObject(new CommandSender(CommandType.LOADAPOST, user, thisUser.getUser()));
        Object object = null;
        try {
            if ((object = Client.getObjectInputStream().readObject()) instanceof Vector)
                posts = (Vector<Post>) object;
            else
                posts = new Vector<>();
        } catch (Exception e) {
            posts = new Vector<>();
            e.printStackTrace();
        }
    }

    public void block(ActionEvent event) {
        if (blockCheckbox.isSelected()) {
            blockUser();
        } else {
            unblockUser();
        }
    }

    private void unblockUser() {
        CommandSender commandSender = new CommandSender(
                CommandType.UNBLOCK, thisUser.getSearchedUser(), thisUser.getUser()
        );
        try {
            thisUser.getUser().getBlockedList().remove
                    (thisUser.getSearchedUser().getUsername());
            Client.getObjectOutputStream().reset();
            Client.getObjectOutputStream().writeObject(commandSender);
        }catch (IOException ioException){
            ioException.printStackTrace();
        }
    }

    private void blockUser() {
            CommandSender commandSender = new CommandSender(
                    CommandType.BLOCK, thisUser.getSearchedUser(), thisUser.getUser()
            );
            try {
                thisUser.getUser().getBlockedList().add(thisUser.getSearchedUser().getUsername());
                Client.getObjectOutputStream().reset();
                Client.getObjectOutputStream().writeObject(commandSender);
            }catch (IOException ioException){
                ioException.printStackTrace();
            }

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

            CommandSender commandSender = new CommandSender(CommandType.UNFOLLOW, thisUser.getSearchedUser(), thisUser.getUser());
            Client.getObjectOutputStream().reset();
            Client.getObjectOutputStream().writeObject(commandSender);
            AtomicInteger atomicInteger = (AtomicInteger) Client.getObjectInputStream().readObject();
            thisUser.getSearchedUser().removeFollower(thisUser.getUser().getUsername());
            thisUser.getUser().removeFollowing(thisUser.getSearchedUser().getUsername());
            followerLabel.setText(atomicInteger + " Followers");
            muteCheckBox.setVisible(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void followUser() {
        try {

            CommandSender commandSender = new CommandSender(CommandType.FOLLOW, thisUser.getSearchedUser(), thisUser.getUser());

            Client.getObjectOutputStream().reset();
            Client.getObjectOutputStream().writeObject(commandSender);
            AtomicInteger atomicInteger = (AtomicInteger) Client.getObjectInputStream().readObject();
            if(!atomicInteger.equals(new AtomicInteger(-1))) {
                followerLabel.setText(atomicInteger + " Followers");
                muteCheckBox.setVisible(true);
                thisUser.getSearchedUser().addFollower(thisUser.getUser().getUsername());
                thisUser.getUser().addFollowing(thisUser.getSearchedUser().getUsername());
            }else{
                errorLabel.setText("This User has blocked you and is unavailable");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void mute(ActionEvent event) {
        if (muteCheckBox.isSelected()) {
            muteUser();
        } else {
            unmuteUser();
        }
    }

    private void muteUser() {
        CommandSender commandSender = new CommandSender(
                CommandType.MUTE, thisUser.getSearchedUser(), thisUser.getUser()
        );
        try {
            thisUser.getUser().getMutedList().add(thisUser.getSearchedUser().getUsername());
            Client.getObjectOutputStream().reset();
            Client.getObjectOutputStream().writeObject(commandSender);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void unmuteUser() {
        CommandSender commandSender = new CommandSender(
                CommandType.UNMUTE, thisUser.getSearchedUser(), thisUser.getUser()
        );
        try {
            thisUser.getUser().getMutedList().remove(thisUser.getSearchedUser().getUsername());
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
