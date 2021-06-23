package client.Controllers;

import client.Main;
import client.PageLoader;
import client.thisUser;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import other.UserList;

import java.io.IOException;

public class UserListItemController implements ItemController {
    private UserList userList;
    @FXML
    private AnchorPane root;
    @FXML
    private Label dateLabel;
    @FXML
    private Label usernameLabel;

    public UserListItemController(UserList userList) throws IOException {
        new PageLoader().load("User", this);
        this.userList = userList;
    }

    @Override
    public AnchorPane init() {
        usernameLabel.setText(userList.getAddressed());
        dateLabel.setText(userList.getDate().toString());
        return root;
    }


    public void enterChat(ActionEvent event) throws IOException {
        thisUser.setSearchedUserName(userList.getAddressed());
        Main.loadAPage(event
                ,"../FXMLs/ChatPage.fxml"
                , "SBUgram - PV"
        );
    }


    public void loadProfile(ActionEvent event) {
    }
}
