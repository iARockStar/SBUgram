package client.Controllers;

import client.*;
import client.items.TextItem;
import com.jfoenix.controls.JFXTextArea;
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
import javafx.stage.FileChooser;
import other.*;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Vector;

/**
 * this class loads the page of a user chatting with another user.
 */
public class ChatPageController extends mainPage {

    @FXML
    private ListView<Message> chatListView;
    @FXML
    private Label usernameLabel;
    @FXML
    private JFXTextArea message;
    @FXML
    private Circle proPicCircle;
    private Vector<Message> allMessages = new Vector<>();


    /**
     * this method is called before all other methods and loads
     * the chats which occurred in the past.
     */
    @FXML
    public void initialize() throws IOException, ClassNotFoundException {
        updateUser();
        setUserInfo();
        loadMessages(thisUser.getUser().getUsername(), thisUser.getSearchedUserName());
        //show the post array in list view
        chatListView.setItems(FXCollections.observableArrayList(allMessages));
        loadMessages(thisUser.getUser().getUsername(), thisUser.getSearchedUserName());

        //customize each cell of postList with new graphic object PostItem
        chatListView.setCellFactory(chatListView -> new TextItem());
        message.setText("");
    }

    /**
     * this method sets the information of the user whom
     * we are chatting with.
     */
    private void setUserInfo() {
        byte[] pic;
        pic = thisUser.getSearchedUser().getProfileImage();
        Image image = new Image(new ByteArrayInputStream(pic));
        proPicCircle.setFill(new ImagePattern(image));
        proPicCircle.setEffect(new DropShadow(+25d, 0d, +2d, Color.DARKGREEN));
        usernameLabel.setText("@" + thisUser.getSearchedUserName());
    }

    /**
     * this method sends the main user and the one he / she
     * is chatting with to the server and then receives 2 maps.
     * one which is for the messages of the main user and the other is for
     * the other user
     *
     * @param myUser       the user that we control.
     * @param theOtherUser the user we are chatting with.
     */
    private void loadMessages(String myUser, String theOtherUser) {
        CommandSender commandSender = new CommandSender(
                CommandType.GETCHATS, myUser, theOtherUser
        );
        try {
            Client.getObjectOutputStream().reset();
            Client.getObjectOutputStream().writeObject(commandSender);
            allMessages = (Vector<Message>) Client.getObjectInputStream().readObject();
        } catch (IOException | ClassNotFoundException ioException) {
            ioException.printStackTrace();
        }

    }

    /**
     * this method is for sending a picMessage to the server.
     */
    public void attach() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Upload your pic for this message");
        File file = chooser.showOpenDialog(null);
        if (file != null) {
            try {
                thisUser.setPicForMessage(new FileInputStream(file).readAllBytes());
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }else{
            return;
        }
        sendPicMessage();
    }

    /**
     * this method sends the picMessage which its picture is taken from the
     * attach method.
     */
    private void sendPicMessage() {
        String info = message.getText();
        Message picMessage = new PicMessage(
                new Date()
                , thisUser.getUser().getUsername()
                , thisUser.getSearchedUserName()
                , info
                , thisUser.getPicForMessage()
        );
        CommandSender sendMessageCommand = new CommandSender(
                CommandType.SENDPICMESSAGE, picMessage
        );
        try {
            Client.getObjectOutputStream().reset();
            Client.getObjectOutputStream().writeObject(sendMessageCommand);
            allMessages = (Vector<Message>) Client.getObjectInputStream().readObject();
            this.initialize();
        } catch (IOException | ClassNotFoundException ioException) {
            ioException.printStackTrace();
        }
    }

    /**
     * this method sends a new message for the other user he / she
     * is chatting with.
     */
    public void send() {
        String info = message.getText();
        Message message = new Message(
                new Date()
                , thisUser.getUser().getUsername()
                , thisUser.getSearchedUserName()
                , info
        );
        CommandSender sendMessageCommand = new CommandSender(CommandType.SENDMESSAGE, message);
        try {
            Client.getObjectOutputStream().reset();
            Client.getObjectOutputStream().writeObject(sendMessageCommand);
            allMessages = (Vector<Message>) Client.getObjectInputStream().readObject();
            this.initialize();
        } catch (IOException | ClassNotFoundException ioException) {
            ioException.printStackTrace();
        }

    }

    /**
     * this method returns the user to the direct menu
     */
    public void goBack(ActionEvent event) throws IOException {
        Main.loadAPage(event
                , "../FXMLs/UserListPage.fxml"
                , "SBUgram - Direct"
        );
    }

    /**
     * this method refreshes the chat page so that the new chats can be seen.
     */
    public void refresh(MouseEvent mouseEvent) throws IOException {
        Main.loadAPageMouse(mouseEvent
                , "../FXMLs/ChatPage.fxml"
                , "SBUgram - PV"
        );
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
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
