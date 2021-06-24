package client.Controllers;

import client.*;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import other.*;

import java.io.IOException;
import java.util.Collections;
import java.util.Vector;


public class UserListPageController extends mainPage{

    /**
     * listView which it's graphic is set in another class(UserListItem class).
     */
    @FXML
    private ListView<UserList> items;

    /**
     * list of UserLists which are set to be shown.
     */
    private Vector<UserList> users = new Vector<>();

    /**
     * this method is called before all other methods and loads
     * the posts of the users which the main user followed.
     */
    @FXML
    public void initialize() throws IOException, ClassNotFoundException {
        loadUsers(thisUser.getUser());

        Collections.sort(users);
        //show the post array in list view
        items.setItems(FXCollections.observableArrayList(users));

        //customize each cell of postList with new graphic object PostItem
        items.setCellFactory(items -> new UserListItem());
    }

    /**
     * this method sends the user and gets the people whom he / she chatted with
     * @param user the user we tend to find its list.
     */
    private void loadUsers(User user) {
        updateUser();
        try{
            Client.getObjectOutputStream().reset();
            CommandSender getUsersListCommand = new CommandSender(CommandType.GETUSERS,user);
            Client.getObjectOutputStream().writeObject(getUsersListCommand);
            users = (Vector<UserList>) Client.getObjectInputStream().readObject();
        }catch (IOException | ClassNotFoundException ioException){
            users = new Vector<>();
            ioException.printStackTrace();
        }
    }

    /**
     * it is called in init method and updates user information.
     */
    private void updateUser() {
        try {
            Client.getObjectOutputStream().reset();
            Client.getObjectOutputStream().writeObject(new CommandSender(CommandType.UPDATEUSER, thisUser.getUser()));
            User newUser = (User) Client.getObjectInputStream().readObject();
            thisUser.setUser(newUser);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    public void refresh(MouseEvent mouseEvent) {
        try {
            this.initialize();
        } catch (IOException | ClassNotFoundException ioException) {
            ioException.printStackTrace();
        }
    }

    /**
     * this method sends the user to the searchPage.
     */
    public void search(MouseEvent mouseEvent) throws IOException {
        Main.loadAPageMouse(mouseEvent
                , "../FXMLs/SearchChat.fxml"
                , "SBUgram - Search the user you want to chat with"
        );
    }
}
