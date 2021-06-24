package client.Controllers;

import client.Client;
import client.Main;
import client.PageLoader;
import client.thisUser;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import other.CommandSender;
import other.CommandType;
import other.User;
import other.UserList;

import java.io.IOException;

/**
 * this class helps to control the items of users available for chatting
 */
public class UserListItemController implements ItemController {
    private UserList userList;
    @FXML
    private AnchorPane root;
    @FXML
    private Label dateLabel;
    @FXML
    private Label usernameLabel;
    @FXML
    private Label unSeenLabel;

    public UserListItemController(UserList userList) throws IOException {
        new PageLoader().load("User", this);
        this.userList = userList;
    }

    @Override
    public AnchorPane init() {
        usernameLabel.setText(userList.getAddressed());
        dateLabel.setText("Latest message published on: " + userList.getDate().toString());
        unSeenLabel.setText(String.valueOf(userList.getNumOfUnSeen()));
        return root;
    }


    /**
     * this method sends the user to the chatpage with the user he / she wants
     * it loads the previous pages and sets the addressed user by a request
     * to the server.
     */
    public void enterChat(ActionEvent event) throws IOException {
        thisUser.setSearchedUserName(userList.getAddressed());
        CommandType searchUserCommand = CommandType.SEARCHUSER;
        CommandSender searchTheServer =
                new CommandSender(searchUserCommand,
                        userList.getAddressed() , thisUser.getUser());
        try {
            Client.getObjectOutputStream().reset();
            Client.getObjectOutputStream().writeObject(searchTheServer);
            Object object;
            User user;
            if ((object = Client.getObjectInputStream().readObject()) instanceof User) {
                user = (User) object;
                thisUser.setSearchedUser(user);
                thisUser.setSearchedUserName(user.getUsername());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Main.loadAPage(event
                , "../FXMLs/ChatPage.fxml"
                , "SBUgram - PV"
        );
    }


    public void loadProfile(ActionEvent event) {
    }
}
