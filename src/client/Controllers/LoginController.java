package client.Controllers;

import client.Client;
import client.Main;
import client.thisUser;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import other.ApprovedType;
import other.CommandSender;
import other.CommandType;
import other.User;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * this class is for the page which the user logs in from it.
 * it has methods for loading the mainPage.
 */
public class LoginController implements Initializable {
    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    private Label warningLabel;
    @FXML
    private TextField nameTextField;
    @FXML
    private final ImageView sbuPic = new ImageView(new Image("images/unnamed.png"));
    @FXML
    private CheckBox show;
    @FXML
    private Button signupButton;
    @FXML
    private PasswordField password;
    @FXML
    private TextField fakePassfield;


    /**
     * sets the SBU label :) .
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        sbuPic.setImage(new Image("images/Screenshot (122).png"));
    }

    /**
     * this method helps the user to load the signUp page if he/she doesn't have
     * an account.
     */
    public void loadSignup(ActionEvent actionEvent) throws IOException {
        Main.loadAPage(actionEvent
                , "/FXMLs/signUp.fxml"
                , "SBUgram - Signup menu"
        );
    }

    /**
     * trick method for showing the password which the user entered.
     */
    @FXML
    public void show() {
        if (!fakePassfield.isVisible()) {
            fakePassfield.setVisible(true);
            password.setVisible(false);
            fakePassfield.setText(password.getText());

        } else {
            fakePassfield.setVisible(false);
            password.setVisible(true);
            password.setText(fakePassfield.getText());
        }
    }


    /**
     * this method checks the validation
     * of the username and the password which the user
     * entered. if the info is valid then the mainPage is loaded.
     */
    @FXML
    private void login(ActionEvent actionEvent) throws IOException {
        if(!Client.isServerUp()) {
            Client.connectToServer();
            Client.setServerUp(true);
        }
        CommandSender commandSender = createCommandSender();
        ApprovedType approvedType = checkValidUserAndPass(commandSender);
        if (approvedType == ApprovedType.APPROVED) {
            Main.loadAPage(actionEvent
                    , "../FXMLs/MainMenu.fxml"
                    , "SBUgram - Main menu"
            );
        } else {
            warningLabel.setText("wrong username or password!");
        }
    }

    /**
     * sends the username and the password to the server and if the
     * info is valid then the server returns true(true approved type).
     * @param commandSender object which i send to the server
     * @return return type is whether true or false(approved or not approved).
     */
    public ApprovedType checkValidUserAndPass(CommandSender commandSender) throws IOException {
        Client.getObjectOutputStream().writeObject(commandSender);
        Client.getObjectOutputStream().flush();
        ApprovedType approvedType = null;
        Object object;
        try {
            if ((object = Client.getObjectInputStream().readObject())
                    instanceof User) {
                approvedType = ApprovedType.APPROVED;
                thisUser.setUser((User) object);
            }else{
                approvedType = ApprovedType.NOT_APPROVED;
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return approvedType;
    }

    /**
     * creates the object which i tend to send to the server.
     * @return a CommandSender which i send to the server.
     */
    public CommandSender createCommandSender() {
        CommandSender commandSender;
        if (password.getText().length() == 0
                || password.getText().length() <=
                fakePassfield.getText().length())
            commandSender =
                    new CommandSender(CommandType.LOGIN
                            , new User(nameTextField.getText()
                            , fakePassfield.getText()));
        else
            commandSender =
                    new CommandSender(CommandType.LOGIN
                            , new User(nameTextField.getText()
                            , password.getText()));
        return commandSender;
    }

    /**
     * this method allows the user to load the retrievePass page
     * if he/she forgot his/her password.
     */
    @FXML
    public void retrievePass(ActionEvent event) throws IOException {
        Main.loadAPage(event
                , "/FXMLs/RetrievePass.fxml"
                , "SBUgram - Retrieve password menu"
        );
    }


}
