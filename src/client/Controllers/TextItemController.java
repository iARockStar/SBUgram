package client.Controllers;

import client.PageLoader;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import other.Message;
import other.UserList;

import java.io.IOException;

public class TextItemController implements ItemController {

    @FXML
    private Message message;
    @FXML
    private AnchorPane root;
    static int f = 0;


    public TextItemController(Message message) throws IOException {
        if(f %2 == 0)
        new PageLoader().load("Message", this);
        else
            new PageLoader().load("ReversedMessage", this);
        this.message = message;
        f++;
    }

    @Override
    public Node init() {
        return root;
    }
}
