package client;

import client.Controllers.TextItemController;
import client.Controllers.UserListItemController;
import javafx.scene.control.ListCell;
import other.Message;
import other.UserList;

import java.io.IOException;

public class TextItem extends ListCell<Message> {

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
