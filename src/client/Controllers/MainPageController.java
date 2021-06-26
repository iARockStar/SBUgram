package client.Controllers;

import client.*;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import other.*;


import java.io.IOException;
import java.util.Vector;

/**
 * this controller is for the page which shows the timeLine.
 */
public class MainPageController extends mainPage {

    /**
     * list of posts which are set to be shown.
     */
    private Vector<Post> posts = new Vector<>();

    /**
     * listView which it's graphic is set in another class(PostItem class).
     */
    @FXML
    ListView<Post> postList;


    /**
     * this method is called before all other methods and loads
     * the posts of the users which the main user followed.
     */
    @FXML
    public void initialize() throws IOException, ClassNotFoundException {
        loadPosts(thisUser.getUser());

        //show the post array in list view
        postList.setItems(FXCollections.observableArrayList(posts));

        //customize each cell of postList with new graphic object PostItem
        postList.setCellFactory(postList -> new PostItem());
    }

    /**
     * this method sends the user and sends it's followings' posts.
     * @param user the mainUser.
     */
    public void loadPosts(User user) throws IOException {
        Client.getObjectOutputStream().writeObject(
                new CommandSender(CommandType.LOADFOLLOWINGPOSTS, user));
        try {
            posts = (Vector<Post>) Client.getObjectInputStream().readObject();
        }catch (Exception e){
            posts = new Vector<>();
            e.printStackTrace();
        }
    }


    /**
     * loads newPostPageController
     */
    public void newPost(ActionEvent event) throws IOException {
        Main.loadAPage(event
                ,"../FXMLs/NewPostPage.fxml"
                , "SBUgram - NewPost page"
        );
    }

    /**
     * it calls the init method so everything will be refreshed after
     * calling this method.
     */
    public void refresh(ActionEvent mouseEvent) {
        try {
            this.initialize();
        } catch (IOException | ClassNotFoundException ioException) {
            ioException.printStackTrace();
        }
    }

}
