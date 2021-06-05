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


public class SearchPageController extends mainPage {
    @FXML
    private Label warningLabel;
    @FXML
    private JFXTextField username;
    public void searchUser(ActionEvent event) throws IOException {
        CommandType searchUserCommand = CommandType.SEARCHUSER;
        CommandSender searchTheServer =
                new CommandSender(searchUserCommand,username.getText());
        try{
            Client.getObjectOutputStream().writeObject(searchTheServer);
            Object object;
            User user;
            if((object = Client.getObjectInputStream().readObject()) instanceof User) {
                user = (User) object;
                thisUser.setSearchedUser(user);
            }else{
                warningLabel.setText("Username not found!");
                warningLabel.setTextFill(Color.RED);
                return;
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        Main.loadAPage(event
                ,"../FXMLs/ProfilePage.fxml"
                , "SBUgram - Profile page"
        );
    }
}
