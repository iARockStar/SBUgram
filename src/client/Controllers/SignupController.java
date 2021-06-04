package client.Controllers;

import client.Client;
import client.Main;
import other.SecurityQuestion;
import client.thisUser;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import other.*;

/**
 * <h>Signup in SBUgram!</h>
 * This controller handles all the occurrences in the signup menu.
 * <p>
 * with the help of the methods of this class a User signs up
 * and connects to the server.
 */
public class SignupController implements Initializable {

    @FXML
    PasswordField password;
    @FXML
    PasswordField re_writePassField;
    @FXML
    TextField fakePassfield;
    @FXML
    TextField fakeReWritePass;

    @FXML
    DatePicker myDatePicker;

    @FXML
    private Button logoutButton;
    @FXML
    Label warningLabel;
    @FXML
    private TextField name;
    @FXML
    private TextField lastName;
    @FXML
    private TextField phoneNumber;

    private byte[] userImage;


    @FXML
    private AnchorPane secondPagePane;
    @FXML
    Label nameLabel;
    @FXML
    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    private Button signupButton;
    @FXML
    private MenuButton securityQuestionItems;
    @FXML
    private Button securityAnswerButton;
    @FXML
    private TextField answerTextField;
    @FXML
    ChoiceBox<String> questions;
    private String[] choices = {"What is your mother's maiden name?"
            , "What is the name of your first pet?"
            , "What was your first car?"
            , "What elementary school did you attend?"
            , "What is the name of the town where you were born?"};
    private String myQuestion;


    @FXML
    private TextField email;

    @FXML
    private Button loginButton2;

    @FXML
    private TextField username;

    Image profilePicImage;

    LocalDate myDate;


    @FXML
    private Circle profilePic1;

    @FXML
    Label notification;
    String question;
    String answer;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Image image = new Image("images/default_contact.png");
        try {
            this.userImage = new FileInputStream("C:\\Users\\USER\\IdeaProjects\\SBUgram\\src\\images\\default_contact.png").readAllBytes();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        profilePic1.setFill(new ImagePattern(image));
        profilePic1.setEffect(new DropShadow(+25d, 0d, +2d, Color.DARKGREEN));
        questions.getItems().addAll(choices);
        questions.setOnAction(this::getQuestion);

    }

    @FXML
    public void signup(ActionEvent event) throws IOException {
        String name = this.name.getText();
        String lastName = this.lastName.getText();
        String username = this.username.getText();
        String phoneNumber = this.phoneNumber.getText();
        try {
            Long.parseLong(phoneNumber);
        } catch (Exception e) {
            warningLabel.setText("write a correct phone number please");
            return;
        }
        String password = this.password.getText();
        String fakePass = this.fakePassfield.getText();
        String reWritePassField = this.re_writePassField.getText();
        String fakeReWritePass = this.fakeReWritePass.getText();
        String email = this.email.getText();
        DatePicker datePicker = this.myDatePicker;
        SecurityQuestion securityQuestion = new SecurityQuestion(question, answer);
        byte[] userImage = this.userImage;
        Matcher matcher = checkValidEmail(email);
        if(email.length() == 0 || matcher.find()) {
            if (name.length() == 0
                    || username.length() == 0
                    || lastName.length() == 0
                    || phoneNumber.length() == 0
                    || (password.length() == 0 && fakePass.length() == 0)
                    || (reWritePassField.length() == 0 && fakeReWritePass.length() == 0)) {
                warningLabel.setText("Please fill in the information fields please!");
                return;
            }
            String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,20}$";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher2 = pattern.matcher(password);
            Matcher matcher1 = pattern.matcher(fakePass);

            if (!matcher2.find() && !matcher1.find()) {
                warningLabel.setText("password not acceptable.");
                return;
            }
            if (reWritePassField.equals(password) || fakeReWritePass.equals(fakePass)) {
                String FormattedDate = myDate.format(DateTimeFormatter.ofPattern("MMM-dd-yyyy"));
                User newUser =
                        getUser(name, lastName, username, phoneNumber, password, fakePass, email, securityQuestion, userImage, FormattedDate);
                thisUser.setUser(newUser);
                CommandSender commandSender = new CommandSender(CommandType.SIGNUP, newUser);
                Client.getObjectOutputStream().writeObject(commandSender);
                Client.getObjectOutputStream().flush();
                System.out.println("new User sent");
                Main.loadAPage(event
                        , "../FXMLs/MainPage.fxml"
                        , "SBUgram - Home page"
                        , root, stage, scene);
            } else {
                warningLabel.setText("password field is not the same as it's reWritten version");
            }
        }else {
            warningLabel.setText("please enter a correct email address please");
        }
    }

    @FXML
    public void show() {
        if (!fakePassfield.isVisible()) {
            fakePassfield.setVisible(true);
            fakeReWritePass.setVisible(true);
            re_writePassField.setVisible(false);
            password.setVisible(false);
            fakeReWritePass.setText(re_writePassField.getText());
            fakePassfield.setText(password.getText());

        } else {
            fakePassfield.setVisible(false);
            fakeReWritePass.setVisible(false);
            password.setVisible(true);
            re_writePassField.setVisible(true);
            password.setText(fakePassfield.getText());
            re_writePassField.setText(fakeReWritePass.getText());
        }
    }

    private Matcher checkValidEmail(String email) {
        Pattern VALID_EMAIL_ADDRESS_REGEX =
                Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$"
                        , Pattern.CASE_INSENSITIVE);
        return VALID_EMAIL_ADDRESS_REGEX.matcher(email);
    }

    private User getUser(String name, String lastName, String username, String phoneNumber, String password, String fakePass, String email, SecurityQuestion securityQuestion, byte[] userImage, String FormattedDate) {
        User newUser;
        if (password.length() != 0)
            if (this.userImage != null)
                newUser = new User(name, lastName, username
                        , password, phoneNumber
                        , securityQuestion, FormattedDate, email, userImage);
            else newUser = new User(name, lastName, username
                    , password, phoneNumber
                    , securityQuestion, FormattedDate, email);
        else if (this.userImage != null)
            newUser = new User(name, lastName, username
                    , fakePass, phoneNumber
                    , securityQuestion, FormattedDate, email, userImage);
        else
            newUser = new User(name, lastName, username
                    , fakePass, phoneNumber
                    , securityQuestion, FormattedDate, email);
        return newUser;
    }

    @FXML
    public void pickADate(ActionEvent event) {
        myDate = myDatePicker.getValue();
        String FormattedDate = myDate.format(DateTimeFormatter.ofPattern("MMM-dd-yyyy"));
        System.out.println(FormattedDate);
    }

    public void setProfilePic(ActionEvent event) throws URISyntaxException, IOException {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Upload your profile picture");
        File file = chooser.showOpenDialog(null);
        if (file != null) {
            System.out.println(file.toString());
            profilePicImage = new Image(file.toURI().toString());
            try {
                this.userImage = new FileInputStream(file).readAllBytes();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            profilePic1.setFill(new ImagePattern(profilePicImage));
            profilePic1.setEffect(new DropShadow(+25d, 0d, +2d, Color.DARKGREEN));
        }
    }

    private void getQuestion(ActionEvent actionEvent) {
        this.myQuestion = questions.getValue();
    }

    public void returnToLoginMenu(ActionEvent actionEvent) throws IOException {
        Main.loadAPage(actionEvent
                , "../FXMLs/sample.fxml"
                , "SBUgram - Login menu"
                , root, stage, scene);
    }

    @FXML
    public void saveAnswerAndQuestion(ActionEvent actionEvent) {

        answer = answerTextField.getText();
        question = myQuestion;
        if (answer != null && answer.length() != 0 && question.length() != 0) {
            notification.setText("security question saved successfully!");
            notification.setTextFill(Color.DARKGREEN);
        } else {
            notification.setText("please choose a question and answer it.");
            notification.setTextFill(Color.RED);
        }

    }
}
