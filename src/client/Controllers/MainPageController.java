package client.Controllers;

import client.*;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import other.*;


import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;

public class MainPageController extends mainPage {

    Post currentPost = new Post();
    CopyOnWriteArrayList<Post> posts = new CopyOnWriteArrayList<>();


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
            posts = (CopyOnWriteArrayList<Post>) Client.getObjectInputStream().readObject();
        }catch (Exception e){
            posts = new CopyOnWriteArrayList<>();
            e.printStackTrace();
        }
    }


    public void post(JFXTextField title, JFXTextArea description,ActionEvent event) throws IOException {
        currentPost.setTitle(title.getText());
        currentPost.setDescription(description.getText());
        currentPost.setWriter("ali alavi");

        //save the post in arraylist
        posts.add(currentPost);

        //show the arraylist in listview
        postList.setItems(FXCollections.observableArrayList(posts));
        postList.setCellFactory(postList -> new PostItem());

        /*
        if the listview cells are not customized and list view is made of strings
        this code will add new post title to the list view
        postList.getItems().add(currentPost.getTitle());
         */

        currentPost = new Post();

        //empty fields
        title.setText("");
        description.setText("");
    }

    @FXML
    ListView<Post> postList;

    public void newPost(ActionEvent event) throws IOException {
        Main.loadAPage(event
                ,"../FXMLs/NewPostPage.fxml"
                , "SBUgram - NewPost page"
        );
    }

    public void refresh(MouseEvent mouseEvent) {
        try {
            this.initialize();
        } catch (IOException | ClassNotFoundException ioException) {
            ioException.printStackTrace();
        }
    }

    public void showPost(MouseEvent mouseEvent) {
    }
}
