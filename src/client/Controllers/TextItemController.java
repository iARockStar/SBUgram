package client.Controllers;

import client.Client;
import client.Main;
import client.PageLoader;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import other.CommandSender;
import other.CommandType;
import other.Message;
import other.ReversedMessage;


import java.io.IOException;
import java.util.Vector;

/**
 * this class is for controlling the items of chats.
 */
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
    @FXML
    private JFXTextArea editField;
    @FXML
    private JFXButton editButton;
    @FXML
    private ImageView deletedImage;
    @FXML
    private ImageView editImage;
    @FXML
    private Button editButton2;

    public TextItemController(Message message) throws IOException {
        if (message instanceof ReversedMessage) {
            new PageLoader().load("ReversedMessage", this);
        } else {
            new PageLoader().load("Message", this);
        }
        this.message = message;
    }

    /**
     * so it seems like there is something wrong with
     * the listView of the java FX when i delete a chat
     * so the initialize method acts like whats app and
     * shows that this message is deleted and doesn't actually
     * delete it :))
     *
     * @return the return value is the message item.
     */
    @Override
    public Node init() {
        usernameLabel.setText(message.getSender());
        dateLabel.setText(message.getDateOfPublish().toString());
        textLabel.setText(message.getText());
        return root;
    }


    /**
     * this method deletes one item of a chat.
     * it sends the message which is about to be deleted and loads the page again.
     */
    public void deleteMessage(ActionEvent event) {
        CommandSender sendMessageCommand = new CommandSender(CommandType.DELETEMESSAGE, message);
        try {
            Client.getObjectOutputStream().reset();
            Client.getObjectOutputStream().writeObject(sendMessageCommand);
            Main.loadAPage(event
                    , "../FXMLs/ChatPage.fxml"
                    , "SBUgram - PV"
            );
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    /**
     * this method makes the text area and button for editing a message visible
     */
    public void editMessage(ActionEvent mouseEvent) {
        editButton.setVisible(true);
        editField.setVisible(true);
        textLabel.setVisible(false);
    }

    /**
     * this method is for editing the message.
     * when the user writes the new message it sends the message and
     * the new text to server and saves the changes.
     */
    public void edit(ActionEvent actionEvent) {
        String newMessage = editField.getText();
        CommandSender editCommand =
                new CommandSender(CommandType.EDITMESSAGE, message, newMessage);
        try {
            Client.getObjectOutputStream().reset();
            Client.getObjectOutputStream().writeObject(editCommand);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        textLabel.setText(newMessage);
        message.setText(newMessage);
        editButton.setVisible(false);
        editField.setVisible(false);
        textLabel.setVisible(true);

    }
}
