package client.Controllers;

import client.Client;
import client.Main;
import client.thisUser;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import other.CommandSender;
import other.CommandType;


import java.io.IOException;

public class MainMenuController extends mainPage {

    public void newPost(ActionEvent event) throws IOException {
        Main.loadAPage(event
                ,"../FXMLs/NewPostPage.fxml"
                , "SBUgram - NewPost page"
        );
    }

    public void logout(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout");
        alert.setHeaderText("you are about to logout");
        alert.setContentText("Are you sure? ");
        if (alert.showAndWait().get() == ButtonType.OK) {
            try {
                Client.getObjectOutputStream().writeObject(new CommandSender(CommandType.LOGOUT, thisUser.getUser()));
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
        Client.setServerUp(false);
        Main.loadAPage(event
                ,"../FXMLs/sample.fxml"
                , "SBUgram - Login menu"
        );
    }

    public void search(ActionEvent event) throws IOException {
        Main.loadAPage(event
                ,"../FXMLs/SearchPage.fxml"
                , "SBUgram - Search user"
        );
    }

    public void deleteAccount(ActionEvent event) throws IOException {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Account");
        alert.setHeaderText("you are about to delete your account");
        alert.setContentText("Are you sure? all of your info will be lost.");
        if (alert.showAndWait().get() == ButtonType.OK) {
            CommandSender deleteAccCommand = new CommandSender(
                    CommandType.DELETEACCOUNT, thisUser.getUser());
            try{
                Client.getObjectOutputStream().writeObject(deleteAccCommand);
            } catch (IOException e) {
                e.printStackTrace();
            }

            Main.loadAPage(event
                    ,"../FXMLs/sample.fxml"
                    , "SBUgram - Login Menu"
            );
        }
    }
}
