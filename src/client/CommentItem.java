package client;

import client.Controllers.CommentItemController;

import javafx.scene.control.ListCell;
import other.*;

import java.io.IOException;

/**
 * this class assigns the graphic to a cell of the comments list.
 */
public class CommentItem extends ListCell<Comment> {
    /**
     * the only method of the class which sets graphic for a cell of the comments list.
     * @param comment comment which is about to attain graphic.
     * @param empty don't know the use of it :) .
     */
    @Override
    public void updateItem(Comment comment, boolean empty) {
        super.updateItem(comment, empty);
        if (comment != null) {
            try {
                setGraphic(new CommentItemController(comment).init());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
