package client.Controllers;

import client.*;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import other.*;


import java.io.IOException;
import java.util.Vector;


public class MainPageController extends mainPage {

    Post currentPost = new Post();
    Vector<Post> posts = new Vector<>();


    @FXML
    public void initialize() throws IOException, ClassNotFoundException {
        loadPosts(thisUser.getUser());

        //show the post array in list view
        postList.setItems(FXCollections.observableArrayList(posts));

        //customize each cell of postList with new graphic object PostItem
        postList.setCellFactory(postList -> new PostItem());
    }

    public void loadPosts(User user) throws IOException {
        thisUser.setIsAnotherUser(true);
        Client.getObjectOutputStream().writeObject(
                new CommandSender(CommandType.LOADFOLLOWINGPOSTS, user));
        try {
            posts = (Vector<Post>) Client.getObjectInputStream().readObject();
        }catch (Exception e){
            posts = new Vector<>();
            e.printStackTrace();
        }
    }


    @FXML
    ListView<Post> postList;

    public void newPost(ActionEvent event) throws IOException {
        Main.loadAPage(event
                ,"../FXMLs/NewPostPage.fxml"
                , "SBUgram - NewPost page"
        );
    }

    public void refresh(ActionEvent mouseEvent) {
        try {
            this.initialize();
        } catch (IOException | ClassNotFoundException ioException) {
            ioException.printStackTrace();
        }
    }

    public void showPost(MouseEvent mouseEvent) {
    }
}
