package client.Controllers;

import client.Main;
import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class mainPage {
     protected Stage stage;
     protected Scene scene;
     protected Parent root;
    public void loadTimeLine(ActionEvent event) throws IOException{
        Main.loadAPage(event
                ,"../FXMLs/MainPage.fxml"
                , "SBUgram - Main page"
        );
    }


    public void loadMainMenu(ActionEvent event)throws IOException{
        Main.loadAPage(event
                ,"../FXMLs/MainMenu.fxml"
                , "SBUgram - Main menu"
        );
    }




    public void loadDirect(ActionEvent event)throws IOException{

    }


    public void loadMyProfile(ActionEvent event)throws IOException{
        Main.loadAPage(event
                ,"../FXMLs/MyProfile.fxml"
                , "SBUgram - Your Profile"
        );
    }

}
