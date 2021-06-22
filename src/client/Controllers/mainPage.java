package client.Controllers;

import client.Main;
import client.thisUser;
import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * the mother of all the pages which have the radioButtons at the bottom.
 * each method of this class load a page and nothing more.
 */
public class mainPage {
     protected Stage stage;
     protected Scene scene;
     protected Parent root;
    public void loadTimeLine(ActionEvent event) throws IOException{
        Main.loadAPage(event
                ,"../FXMLs/MainPage.fxml"
                , "SBUgram - Main page"
        );
        thisUser.setSearchedUser(null);
    }


    public void loadMainMenu(ActionEvent event)throws IOException{
        Main.loadAPage(event
                ,"../FXMLs/MainMenu.fxml"
                , "SBUgram - Main menu"
        );
        thisUser.setSearchedUser(null);

    }




    public void loadDirect(ActionEvent event)throws IOException{

    }


    public void loadMyProfile(ActionEvent event)throws IOException{
        Main.loadAPage(event
                ,"../FXMLs/MyProfile.fxml"
                , "SBUgram - Your Profile"
        );
        thisUser.setSearchedUser(thisUser.getUser());
    }

}
