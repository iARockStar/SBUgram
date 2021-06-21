package client.Controllers;

import client.Client;
import client.Main;
import client.thisUser;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import other.CommandType;
import other.CommandSender;
import other.User;

import java.io.IOException;
import java.lang.ref.Cleaner;


public class SearchPageController extends mainPage {
    @FXML
    private Label warningLabel;
    @FXML
    private JFXTextField username;

    public void searchUser(ActionEvent event) throws IOException {
        CommandType searchUserCommand = CommandType.SEARCHUSER;
        CommandSender searchTheServer =
                new CommandSender(searchUserCommand, username.getText());
        try {
            Client.getObjectOutputStream().reset();
            Client.getObjectOutputStream().writeObject(searchTheServer);
            Object object;
            User user;
            if ((object = Client.getObjectInputStream().readObject()) instanceof User) {
                user = (User) object;
                boolean isEquals  = false;
                if(thisUser.getSearchedUser() != null)
                    isEquals = user.getNumOfFollowers().equals(thisUser.getSearchedUser().getNumOfFollowers());
                System.out.println(isEquals);
                System.out.println(user.getNumOfFollowers());
                thisUser.setSearchedUser(user);
            } else {
                warningLabel.setText("Username not found!");
                warningLabel.setTextFill(Color.RED);
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (thisUser.getSearchedUser().getUsername()
                .equalsIgnoreCase(thisUser.getUser().getUsername()))
            Main.loadAPage(event
                    , "../FXMLs/MyProfile.fxml"
                    , "SBUgram - Your profile"
            );
        else
            Main.loadAPage(event
                    , "../FXMLs/ProfilePage.fxml"
                    , "SBUgram - Profile page"
            );
    }

    public void returnToMainMenu(ActionEvent Event) throws IOException {
        Main.loadAPage(Event
        ,"../FXMLs/MainMenu.fxml"
        ,"SBUgram - Main menu");
    }
}
