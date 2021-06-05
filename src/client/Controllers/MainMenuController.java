package client.Controllers;

import client.Main;
import javafx.event.ActionEvent;


import java.io.IOException;

public class MainMenuController extends mainPage {

    public void newPost(ActionEvent event) throws IOException {
        Main.loadAPage(event
                ,"../FXMLs/NewPostPage.fxml"
                , "SBUgram - NewPost page"
        );
    }

    public void logout(ActionEvent event) throws IOException {
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
}
