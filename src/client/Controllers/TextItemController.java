package client.Controllers;

import client.Client;
import client.Main;
import client.PageLoader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import other.CommandSender;
import other.CommandType;
import other.Message;
import other.ReversedMessage;


import java.io.IOException;
import java.util.Vector;

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


    public void deleteMessage(ActionEvent event) {
        CommandSender sendMessageCommand = new CommandSender(CommandType.DELETEMESSAGE,message);
        try {
            Client.getObjectOutputStream().reset();
            Client.getObjectOutputStream().writeObject(sendMessageCommand);
            Main.loadAPage(event
                    , "../FXMLs/ChatPage.fxml"
                    , "SBUgram - PV"
            );
        }catch (IOException ioException){
            ioException.printStackTrace();
        }
    }

    public void editMessage(ActionEvent mouseEvent) {
    }
}
