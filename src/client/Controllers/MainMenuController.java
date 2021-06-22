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

/**
 * from the page of this controller the user can access almost everything.
 */
public class MainMenuController extends mainPage {

    /**
     * loads the newPostPageController
     */
    public void newPost(ActionEvent event) throws IOException {
        Main.loadAPage(event
                ,"../FXMLs/NewPostPage.fxml"
                , "SBUgram - NewPost page"
        );
    }

    /**
     * if the user selects the logout button this method is called and
     * the user logs out and disconnects from the server.
     * also a popup is shown which asks the uses if he / she
     * is sure to logout.
     */
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
            Client.setServerUp(false);
            Main.loadAPage(event
                    ,"../FXMLs/sample.fxml"
                    , "SBUgram - Login menu"
            );
        }

    }

    /**
     * loads the SearchPageController.
     * also a popup is shown which asks the uses if he / she
     * is sure to delete account.
     */
    public void search(ActionEvent event) throws IOException {
        Main.loadAPage(event
                ,"../FXMLs/SearchPage.fxml"
                , "SBUgram - Search user"
        );
    }

    /**
     * deletes the account of the user.
     * also a popup is shown which asks the uses if he / she
     * is sure to delete account.
     */
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
