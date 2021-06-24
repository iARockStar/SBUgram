package client;

import client.Controllers.PostItemController;
import client.Controllers.UserListItemController;
import javafx.scene.control.ListCell;
import other.Post;
import other.UserList;

import java.io.IOException;
/**
 * this class assigns the graphic to a cell of the User list.
 */
public class UserListItem extends ListCell<UserList> {

    /**
     * the only method of the class which sets graphic for a cell of the users list.
     * @param userList user which is about to attain graphic.
     * @param empty don't know the use of it :) .
     */
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
