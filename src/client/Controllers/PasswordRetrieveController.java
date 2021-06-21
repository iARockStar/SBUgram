package client.Controllers;

import client.Client;
import client.Main;
import other.SecurityQuestion;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import other.*;

/**
 * this class and its page allows user to
 * retrieve his/her password using the security question.
 */
public class PasswordRetrieveController {
    @FXML
    Button submitButton;
    @FXML
    JFXTextField usernameField;
    @FXML
    JFXTextField myAnswerField;
    @FXML
    JFXPasswordField newPassField;
    @FXML
    Label questionLabel;
    @FXML
    Label infoLabel;
    @FXML
    private Stage stage;
    private Scene scene;
    private Parent root;
    private User user;

    /**
     * first the user calls this method in order to find the question
     * which the user answered when he / she signedUp.
     * it sends a username and receives a security question which the
     * user must remember the answer to it.
     */
    public void find(ActionEvent event) {
        String username;
        if (usernameField.getText().length() != 0) {
            username = usernameField.getText();
            user = new User(username);
            CommandSender commandSender = new CommandSender(CommandType.RETRIEVEPASS, user);
            try {
                Client.getObjectOutputStream().writeObject(commandSender);
                Object object;
                if ((object = Client.getObjectInputStream().readObject()) instanceof User) {
                    user = (User) object;
                    questionLabel.setText(user.getSecurityQuestion().getQuestion());
                    infoLabel.setText("");
                } else {
                    infoLabel.setText("Username not found!");
                    infoLabel.setTextFill(Color.RED);

                }
            } catch (IOException | ClassNotFoundException ioException) {
                ioException.printStackTrace();
            }
        } else {
            infoLabel.setText("No Username is entered!");
            infoLabel.setTextFill(Color.RED);
        }

    }


    /**
     * after the user entered the answer and the new password this method is called
     * and if the answer is correct and the password is valid, then the newPass
     * will be saved in the db.
     */
    public void submit(ActionEvent actionEvent) {
        String answer;
        String newPass;
        if (myAnswerField.getText().length() != 0 && newPassField.getText().length() != 0) {
            answer = myAnswerField.getText();
            newPass = newPassField.getText();
            String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,20}$";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(newPass);
            if (matcher.find()) {
                SecurityQuestion securityQuestion =
                        new SecurityQuestion(questionLabel.getText(), answer);
                User user = new User(newPass, securityQuestion, this.user.getUsername());
                try {
                    CommandSender commandSender = new CommandSender(CommandType.RETRIEVEPASS2NDPART, user);
                    Client.getObjectOutputStream().writeObject(commandSender);
                    boolean result = (boolean) Client.getObjectInputStream().readObject();
                    if (result) {
                        infoLabel.setText("newPassword saved successfully!");
                        infoLabel.setTextFill(Color.DARKGREEN);
                    } else {
                        infoLabel.setText("Your answer is wrong!");
                        infoLabel.setTextFill(Color.RED);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                infoLabel.setText("password not acceptable!");
                infoLabel.setTextFill(Color.RED);

            }
        } else {
            infoLabel.setText("Please fill in the fields!");
            infoLabel.setTextFill(Color.RED);
        }
    }


    /**
     * returns to login menu
     */
    public void goBack(ActionEvent event) throws IOException {
        Main.loadAPage(event
                , "../FXMLs/sample.fxml"
                , "SBUgram - Login menu"
        );
    }


}
