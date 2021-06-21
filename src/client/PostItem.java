package client;


import client.Controllers.PostItemController;
import javafx.scene.control.ListCell;
import other.*;
import java.io.IOException;
/**
 * this class assigns the graphic to a cell of the posts list.
 */
public class PostItem extends ListCell<Post> {

    /**
     * the only method of the class which sets graphic for a cell of the posts list.
     * @param post post which is about to attain graphic.
     * @param empty don't know the use of it :) .
     */

    @Override
    public void updateItem(Post post, boolean empty) {
        super.updateItem(post, empty);
        if (post != null) {
            try {
                setGraphic(new PostItemController(post).init());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
