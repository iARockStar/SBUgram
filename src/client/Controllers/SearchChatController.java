package client.Controllers;

import client.Client;
import client.Main;
import client.thisUser;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import other.CommandSender;
import other.CommandType;
import other.User;

import java.io.IOException;

/**
 * this class is similar to the SearchPageController
 * but it checks if the searched user for chatting is available or not due to the
 * blocking.
 */
public class SearchChatController extends mainPage {
    @FXML
    private Label warningLabel;
    @FXML
    private JFXTextField username;

    /**
     * this method sends the username of the user which the mainUser wants to search
     * and receives an updatedUser which he/she searched.
     * searchedUser is saved for further uses.
     */
    public void searchUser(ActionEvent event) throws IOException {
        CommandType searchUserCommand = CommandType.SEARCHCHAT;
        CommandSender searchTheServer =
                new CommandSender(searchUserCommand, username.getText(), thisUser.getUser());
        try {
            Client.getObjectOutputStream().reset();
            Client.getObjectOutputStream().writeObject(searchTheServer);
            Object object;
            User user;
            if ((object = Client.getObjectInputStream().readObject()) instanceof User) {
                user = (User) object;
                thisUser.setSearchedUser(user);
                thisUser.setSearchedUserName(user.getUsername());
            } else {
                warningLabel.setText("Username either not found or you are blocked!");
                warningLabel.setTextFill(Color.RED);
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        CommandSender commandSender = new CommandSender(
                CommandType.CREATECHATITEM,thisUser.getUser(),thisUser.getSearchedUser()
        );
        try{
            Client.getObjectOutputStream().reset();
            Client.getObjectOutputStream().writeObject(commandSender);
        }catch (IOException ioException){
            ioException.printStackTrace();
        }
            Main.loadAPage(event
                    , "../FXMLs/ChatPage.fxml"
                    , "SBUgram - PV"
            );

    }

    /**
     * a simple method which the user goes back to the main menu using it.
     */
    public void returnToMainMenu(ActionEvent Event) throws IOException {
        Main.loadAPage(Event
                , "../FXMLs/UserListPage.fxml"
                , "SBUgram - Direct");
    }
}
