package client;


import client.Controllers.PostItemController;
import javafx.scene.control.ListCell;
import other.*;
import java.io.IOException;

public class PostItem extends ListCell<Post> {



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
