package client.Controllers;

import client.*;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import other.*;

import java.io.IOException;
import java.util.Date;
import java.util.Vector;

/**
 * this class loads the page of a user chatting with another user.
 */
public class ChatPageController extends mainPage{

    @FXML
    private ListView<Message> chatListView;
    @FXML
    private Label usernameLabel;
    private Vector<Message> messagesSent = new Vector<>();
    private Vector<Message> messagesReceived = new Vector<>();


    /**
     * this method is called before all other methods and loads
     * the chats which occurred in the past.
     */
    @FXML
    public void initialize() throws IOException, ClassNotFoundException {
        usernameLabel.setText(thisUser.getSearchedUserName());
        loadMessages(thisUser.getUser().getUsername(),thisUser.getSearchedUserName());
        for (int i = 1; i <= 5; i++) {
            Message p = new Message(new Date(),thisUser.getUser(),thisUser.getSearchedUser(),
                    "salam");
            messagesSent.add(p);
        }
        //show the post array in list view
        chatListView.setItems(FXCollections.observableArrayList(messagesSent));

        //customize each cell of postList with new graphic object PostItem
        chatListView.setCellFactory(chatListView -> new TextItem());
    }

    /**
     * this method sends the main user and the one he / she
     * is chatting with to the server and then receives 2 maps.
     * one which is for the messages of the main user and the other is for
     * the other user
     * @param myUser the user that we control.
     * @param theOtherUser the user we are chatting with.
     */
    private void loadMessages(String myUser,String theOtherUser) {
        CommandSender commandSender = new CommandSender(
                CommandType.GETCHATS,myUser,theOtherUser
        );
        ChatHolder holder;
        try{
            Client.getObjectOutputStream().reset();
            Client.getObjectOutputStream().writeObject(commandSender);
            holder = (ChatHolder) Client.getObjectInputStream().readObject();
            messagesSent = holder.getSentMessages();
            messagesReceived = holder.getReceived();
        }catch (IOException | ClassNotFoundException ioException){
            ioException.printStackTrace();
        }

    }

    public void attach(ActionEvent event) {
    }

    public void send(ActionEvent event) {
    }

    public void goBack(ActionEvent event) throws IOException {
        Main.loadAPage(event
                ,"../FXMLs/UserListPage.fxml"
                , "SBUgram - Direct"
        );
    }
}
