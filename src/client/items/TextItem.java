package client.items;

import client.Controllers.TextItemController;
import client.Controllers.UserListItemController;
import javafx.scene.control.ListCell;
import other.Message;
import other.UserList;

import java.io.IOException;
/**
 * this class assigns the graphic to a cell of the Texts list.
 */
public class TextItem extends ListCell<Message> {
    /**
     * the only method of the class which sets graphic for a cell of the texts list.
     * @param message message which is about to attain graphic.
     * @param empty don't know the use of it :) .
     */
    @Override
    public void updateItem(Message message, boolean empty) {
        super.updateItem(message, empty);
        if (message != null) {
            try {
                setGraphic(new TextItemController(message).init());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
