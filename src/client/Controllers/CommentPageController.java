package client.Controllers;

import client.Client;
import client.CommentItem;
import client.PostItem;
import client.thisUser;
import com.jfoenix.controls.JFXTextArea;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import other.CommandSender;
import other.CommandType;
import other.Comment;
import other.Post;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.CopyOnWriteArrayList;

public class CommentPageController extends mainPage implements Initializable{

    @FXML
    private ListView<Comment> commentListView;
    @FXML
    private JFXTextArea commentText;

    CopyOnWriteArrayList<Comment> comments;
    public void comment(ActionEvent event) {
        Post post = thisUser.getUser().getPostToComment();
        Comment comment = new Comment(thisUser.getUser(),post,commentText.getText());

        addComment(comment);
        loadComments();
    }

    private void addComment(Comment comment) {
        CommandSender commandSender =
                new CommandSender(CommandType.COMMENT, comment);
        try {
            Client.getObjectOutputStream().writeObject(commandSender);
            Client.getObjectOutputStream().flush();
            comments = (CopyOnWriteArrayList<Comment>) Client.getObjectInputStream().readObject();
        }catch (Exception e){
            comments = new CopyOnWriteArrayList<>();
            e.printStackTrace();
        }
    }

    private void loadComments() {
        //show the post array in list view
        commentListView.setItems(FXCollections.observableArrayList(comments));

        //customize each cell of postList with new graphic object PostItem
        commentListView.setCellFactory(commentListView -> new CommentItem());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
