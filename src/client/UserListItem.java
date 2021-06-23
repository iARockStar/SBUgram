package client;

import client.Controllers.PostItemController;
import client.Controllers.UserListItemController;
import javafx.scene.control.ListCell;
import other.Post;
import other.UserList;

import java.io.IOException;

public class UserListItem extends ListCell<UserList> {

    @Override
    public void updateItem(UserList userList, boolean empty) {
        super.updateItem(userList, empty);
        if (userList != null) {
            try {
                setGraphic(new UserListItemController(userList).init());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
