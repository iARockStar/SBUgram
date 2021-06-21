package client.Controllers;

import javafx.scene.Node;

/**
 * the mother of all the itemControllers which must implement the init method.
 */
public interface ItemController {
    /**
     * this method must be called for setting information for any item like the
     * post item,comment item or the list of users' item.
     */
    Node init();
}
