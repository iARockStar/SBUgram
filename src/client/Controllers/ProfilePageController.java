package client.Controllers;

import client.Client;
import client.items.PostItem;
import client.thisUser;
import com.jfoenix.controls.JFXCheckBox;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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
import java.util.Vector;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * one of the most important classes
 * which controls the profilePages which the user
 * wants to see.
 */
public class ProfilePageController extends mainPage {

    /**
     * list of posts of the searched user.
     */
    @FXML
    private ListView<Post> postList;
    private Vector<Post> posts = new Vector<>();

    @FXML
    private Circle profilePic;
    @FXML
    private Label usernameLabel;
    @FXML
    private Label namePlusLastnameLabel;
    @FXML
    private Label birthDateLabel;
    @FXML
    private JFXCheckBox followCheckbox;
    @FXML
    private Label followingLabel;
    @FXML
    private Label followerLabel;
    @FXML
    private JFXCheckBox muteCheckBox;
    @FXML
    private JFXCheckBox blockCheckbox;
    @FXML
    private Label errorLabel;

    /**
     * this method updates and initializes the page.
     * when it is called the posts and the num of followers and
     * followings are updated.
     */
    @FXML
    public void initialize() {
        updateUser();
        updateMyUser(thisUser.getUser());
        User user = thisUser.getSearchedUser();
        User myUser = thisUser.getUser();
        if(user == null || myUser == null){
            errorLabel.setText("this user has deleted his / her account");
            return;
        }
        boolean isBlocked = myUser.getBlockedList().contains(thisUser.getSearchedUser().getUsername());
        if (isBlocked)
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
        if (user.getBlockedList().contains(myUser.getUsername()))
            followCheckbox.setSelected(false);
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
     * this method updates your profile
     *
     * @param user your profile.
     */
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

    /**
     * this method sets searched profile's info such as
     * the num of followers and followings,its username,proPic and...
     */
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

    /**
     * this method sends a user to the server and receives the list of
     * the user's posts. it also receives empty list if the searched profile
     * blocked you.
     */
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

    /**
     * this method is called when the user hits the block checkBox
     * if the checkBox is selected then it's a block.otherwise it's an unblock.
     */
    public void block(ActionEvent event) {
        if (blockCheckbox.isSelected()) {
            blockUser();
        } else {
            unblockUser();
        }
    }

    /**
     * this method sends the blocker and blocked user to the server to block
     * the searched profile.
     */
    private void unblockUser() {
        CommandSender commandSender = new CommandSender(
                CommandType.UNBLOCK, thisUser.getSearchedUser(), thisUser.getUser()
        );
        try {
            thisUser.getUser().getBlockedList().remove
                    (thisUser.getSearchedUser().getUsername());
            Client.getObjectOutputStream().reset();
            Client.getObjectOutputStream().writeObject(commandSender);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    /**
     * this method sends the unBlocker and unBlocked user to the server to unblock
     * the searched profile.
     */
    private void blockUser() {
        CommandSender commandSender = new CommandSender(
                CommandType.BLOCK, thisUser.getSearchedUser(), thisUser.getUser()
        );
        try {
            thisUser.getUser().getBlockedList().add(thisUser.getSearchedUser().getUsername());
            Client.getObjectOutputStream().reset();
            Client.getObjectOutputStream().writeObject(commandSender);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

    }

    /**
     * this method is called when the user hits the follow checkBox
     * if the checkBox is selected then it's a follow.otherwise it's an unfollow.
     */
    public void follow(ActionEvent event) {
        if (followCheckbox.isSelected()) {
            followUser();
        } else {
            unfollowUser();
        }
    }

    /**
     * this method sends the unFollower and unFollowed user to the server to unFollow
     * the searched profile.
     */
    private void unfollowUser() {
        try {
            CommandSender commandSender = new CommandSender(
                    CommandType.UNFOLLOW, thisUser.getSearchedUser(), thisUser.getUser());
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

    /**
     * this method sends the Follower and Followed user to the server to Follow
     * the searched profile.
     */
    private void followUser() {
        try {
            CommandSender commandSender = new CommandSender(CommandType.FOLLOW, thisUser.getSearchedUser(), thisUser.getUser());
            Client.getObjectOutputStream().reset();
            Client.getObjectOutputStream().writeObject(commandSender);
            AtomicInteger atomicInteger = (AtomicInteger) Client.getObjectInputStream().readObject();
            if (atomicInteger.intValue() != -1) {
                System.out.println("shouldnt be here");
                followerLabel.setText(atomicInteger + " Followers");
                muteCheckBox.setVisible(true);
                thisUser.getSearchedUser().addFollower(thisUser.getUser().getUsername());
                thisUser.getUser().addFollowing(thisUser.getSearchedUser().getUsername());
                errorLabel.setText("");
            } else {
                errorLabel.setText("This User has blocked you and is unavailable");
                followCheckbox.setSelected(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * this method is called when the user hits the mute checkBox
     * if the checkBox is selected then it's a mute.otherwise it's an unMute.
     */
    public void mute(ActionEvent event) {
        if (muteCheckBox.isSelected()) {
            muteUser();
        } else {
            unmuteUser();
        }
    }

    /**
     * this method sends the muter and muted user to the server to mute
     * the searched profile.
     */
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

    /**
     * this method sends the unMuter and unMuted user to the server to unMute
     * the searched profile.
     */
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

    /**
     * this method recalls the init method and therefore
     * refreshes the page.
     */
    public void refresh(MouseEvent mouseEvent) {
        errorLabel.setText("");
        this.initialize();
    }
}
