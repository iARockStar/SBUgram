package client.Controllers;

import client.Main;
import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class mainPage {
     Stage stage;
     Scene scene;
     Parent root;
    public void loadTimeLine(ActionEvent event) throws IOException{
        Main.loadAPage(event
                ,"../FXMLs/MainPage.fxml"
                , "SBUgram - Main page"
                ,root,stage,scene);
    }


    public void loadMainMenu(ActionEvent event)throws IOException{
        Main.loadAPage(event
                ,"../FXMLs/MainMenu.fxml"
                , "SBUgram - Main menu"
                ,root,stage,scene);
    }




    public void loadDirect(ActionEvent event)throws IOException{

    }


    public void loadMyProfile(ActionEvent event)throws IOException{
        Main.loadAPage(event
                ,"../FXMLs/MyProfile.fxml"
                , "SBUgram - Your Profile"
                ,root,stage,scene);
    }
}
