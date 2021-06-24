package client.Controllers;

import client.PageLoader;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import other.Message;
import other.ReversedMessage;
import other.UserList;


import java.io.IOException;

public class TextItemController implements ItemController {

    @FXML
    private Message message;
    @FXML
    private AnchorPane root;
    @FXML
    private Label usernameLabel;
    @FXML
    private Label dateLabel;
    @FXML
    private Label textLabel;


    public TextItemController(Message message) throws IOException {
        if (message instanceof ReversedMessage) {
            new PageLoader().load("ReversedMessage", this);
        } else {
            new PageLoader().load("Message", this);
        }
        this.message = message;
    }

    @Override
    public Node init() {
        usernameLabel.setText(message.getSender());
        dateLabel.setText(message.getDateOfPublish().toString());
        textLabel.setText(message.getText());
        return root;
    }
}
