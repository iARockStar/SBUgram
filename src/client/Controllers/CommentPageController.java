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
import javafx.geometry.Pos;
import javafx.scene.control.ListView;
import other.*;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Vector;


/**
 * this Class controls the page which the comments are shown.
 * <p>
 * it also adds a comment which the user tends to add.
 * </p>
 */
public class CommentPageController extends mainPage implements Initializable {

    @FXML
    private ListView<Comment> commentListView;
    @FXML
    private JFXTextArea commentText;
    Vector<Comment> comments;


    /**
     * this method is called before all other methods
     * and gets the post which the user wants to see it's comments.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Post post = thisUser.getUser().getPostToComment();
        ;
        loadComments(post);
    }


    /**
     * when the user presses the comment button this method is called
     * and makes a new comment.
     */
    public void comment() {
        Post post = thisUser.getUser().getPostToComment();
        Comment comment = new Comment(thisUser.getUser(), post, commentText.getText());
        addComment(post, comment);
    }

    /**
     * this method sends the comment and it's post to the server
     * and receives the new list of comments
     *
     * @param post    the post which the user wants to comment on it.
     * @param comment new comment to be sent to the server.
     */
    private void addComment(Post post, Comment comment) {
        CommandSender commandSender =
                new CommandSender(CommandType.COMMENT, post, post.getOwner(), thisUser.getUser(), comment);
        try {
            Client.getObjectOutputStream().writeObject(commandSender);
            Client.getObjectOutputStream().flush();
            comments = (Vector<Comment>) Client.getObjectInputStream().readObject();
        } catch (Exception e) {
            comments = new Vector<>();
            e.printStackTrace();
        }
        //show the post array in list view
        commentListView.setItems(FXCollections.observableArrayList(comments));
        //customize each cell of postList with new graphic object PostItem
        commentListView.setCellFactory(commentListView -> new CommentItem());
        thisUser.getUser().getPostToComment().setComments(comments);
    }

    /**
     * the difference of this method from addComment method is that
     * it doesn't add any new comment and only loads the comments.
     * @param post the post which the user wants to see it's comments.
     */
    private void loadComments(Post post) {
        CommandSender commandSender = new CommandSender(CommandType.LOADCOMMENTS, post, post.getOwner(), thisUser.getUser(), null);
        try {
            Client.getObjectOutputStream().writeObject(commandSender);
            comments = (Vector<Comment>) Client.getObjectInputStream().readObject();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        //show the post array in list view
        commentListView.setItems(FXCollections.observableArrayList(comments));
        //customize each cell of postList with new graphic object PostItem
        commentListView.setCellFactory(commentListView -> new CommentItem());
    }


}
