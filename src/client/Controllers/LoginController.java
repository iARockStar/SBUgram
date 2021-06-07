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

public class LoginController implements Initializable {
    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    Label warningLabel;
    @FXML
    TextField nameTextField;
    @FXML
    ImageView sbuPic = new ImageView(new Image("images/unnamed.png"));
    @FXML
    CheckBox show;
    @FXML
    Button signupButton;
    @FXML
    PasswordField password;
    @FXML
    TextField fakePassfield;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        sbuPic.setImage(new Image("images/Screenshot (122).png"));
    }


    public void loadSignup(ActionEvent actionEvent) throws IOException {
        Main.loadAPage(actionEvent
                , "/FXMLs/signUp.fxml"
                , "SBUgram - Signup menu"
        );
    }

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


    @FXML
    private void login(ActionEvent actionEvent) throws IOException {
        CommandSender commandSender = createCommandSender();
        ApprovedType approvedType = checkValidUserAndPass(commandSender);
        if (approvedType == ApprovedType.APPROVED) {
            Main.loadAPage(actionEvent
                    , "../FXMLs/MainPage.fxml"
                    , "SBUgram - Home page"
            );
        } else {
            warningLabel.setText("wrong username or password!");
        }
    }

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

    @FXML
    public void retrievePass(ActionEvent event) throws IOException {
        Main.loadAPage(event
                , "/FXMLs/RetrievePass.fxml"
                , "SBUgram - Retrieve password menu"
        );
    }


}
