package client.Controllers;

import client.Client;
import client.Main;
import client.thisUser;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import other.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <h>Signup in SBUgram!</h>
 * This controller handles all the occurrences in the signup menu.
 * <p>
 * with the help of the methods of this class a User signs up
 * and connects to the server.
 */
public class SignupController implements Initializable {

    @FXML
    private PasswordField password;
    @FXML
    private PasswordField re_writePassField;
    @FXML
    private TextField fakePassField;
    @FXML
    private TextField fakeReWritePass;
    @FXML
    private DatePicker myDatePicker;
    @FXML
    private Label warningLabel;
    @FXML
    private TextField name;
    @FXML
    private TextField lastName;
    @FXML
    private TextField phoneNumber;
    @FXML
    private TextField answerTextField;
    @FXML
    ChoiceBox<String> questions;
    @FXML
    private TextField email;
    @FXML
    private TextField username;
    @FXML
    private Circle profilePic1;
    @FXML
    Label notification;
    String question;
    String answer;
    private Image profilePicImage;
    private LocalDate myDate;
    private byte[] userImage;
    /*
     * choices are for the security question
     * which the user must choose one.
     */
    private final String[] choices = {"What is your mother's maiden name?"
            , "What is the name of your first pet?"
            , "What was your first car?"
            , "What elementary school did you attend?"
            , "What is the name of the town where you were born?"};
    private String myQuestion;

    /**
     * this method is called before all of the other methods
     * and sets the default profilePic for the user.
     * it also prepares questions for the security question section.
     */
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

    /**
     * most important method in this class which handles the user
     * signing in when he/she selects the signUp button.
     * it saves the information of the user and sends it to the server.
     *
     * @param event event which we tend to control.
     * @throws IOException
     */
    @FXML
    public void signup(ActionEvent event) throws IOException {
        if (!Client.isServerUp()) {
            Client.connectToServer();
            Client.setServerUp(true);
        }
        String name = this.name.getText();
        String lastName = this.lastName.getText();
        String username = this.username.getText();
        String phoneNumber = this.phoneNumber.getText();
        if (!validPhoneNumber(phoneNumber)) return;
        String password = this.password.getText();
        String fakePass = this.fakePassField.getText();
        String reWritePassField = this.re_writePassField.getText();
        String fakeReWritePass = this.fakeReWritePass.getText();
        String email = this.email.getText();
        SecurityQuestion securityQuestion = new SecurityQuestion(question, answer);
        byte[] userImage = this.userImage;
        Matcher matcher = checkValidEmail(email);
        if (email.length() == 0 || matcher.find()) {
            if (name.length() == 0
                    || username.length() == 0
                    || lastName.length() == 0
                    || phoneNumber.length() == 0
                    || (password.length() == 0 && fakePass.length() == 0)
                    || (reWritePassField.length() == 0 && fakeReWritePass.length() == 0)) {
                warningLabel.setText("Please fill in the information fields please!");
                return;
            }
            String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–{}:;',?/*~$^+=<>]).{8,20}$";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher2 = pattern.matcher(password);
            Matcher matcher1 = pattern.matcher(fakePass);

            if (!matcher2.find() && !matcher1.find()) {
                warningLabel.setText("password not acceptable.");
                return;
            }
            if (reWritePassField.equals(password) || fakeReWritePass.equals(fakePass)) {
                loadMainPageIfValid(
                        event, name, lastName, username,
                        phoneNumber, password, fakePass,
                        email, securityQuestion, userImage
                );
            } else {
                warningLabel.setText("password field is not the same as it's reWritten version");
            }
        } else {
            warningLabel.setText("please enter a correct email address please");
        }
    }

    /**
     * checks if the phoneNumber which the user entered has the correct pattern.
     * for instance it doesn't allow characters or a String or generally
     * anything except integers.
     * @param phoneNumber the String which we check its validation.
     * @return if the phoneNumber is valid it returns true.
     */
    private boolean validPhoneNumber(String phoneNumber) {
        try {
            Long.parseLong(phoneNumber);
        } catch (Exception e) {
            warningLabel.setText("write a correct phone number please");
            return false;
        }
        return true;
    }

    /**
     * this method makes a new user and returns it for sending it to the server
     *
     * @param name             name of the user
     * @param lastName         lastName of the user
     * @param username         username of the user
     * @param phoneNumber      phoneNumber of the user
     * @param password         password of the user
     * @param fakePass         just if the user enters the password in the shown passField
     * @param email            email of the user which is optional
     * @param securityQuestion this field is optional, too.
     * @param userImage        profilePic for the user which has a default pic, too.
     * @param FormattedDate    birthDate of the user.
     * @return return type is a newUser which is ready to be sent to the server.
     */
    private User getUser(String name, String lastName, String username, String phoneNumber, String password, String fakePass, String email, SecurityQuestion securityQuestion, byte[] userImage, String FormattedDate) {
        User newUser;
        if (password.length() != 0 && password.length() >= fakePass.length())
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

    /**
     * this method checks if the email entered has a valid pattern.
     * its valid if its like this : something@something.thing
     *
     * @param email the String which it's validation is checked.
     * @return return type declares if the email is valid or not.
     */
    private Matcher checkValidEmail(String email) {
        Pattern VALID_EMAIL_ADDRESS_REGEX =
                Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$"
                        , Pattern.CASE_INSENSITIVE);
        return VALID_EMAIL_ADDRESS_REGEX.matcher(email);
    }

    /**
     * in the end, if everything is correct and in it's place, this method is called
     * and the main page loads.
     */
    private void loadMainPageIfValid(ActionEvent event, String name, String lastName, String username, String phoneNumber, String password, String fakePass, String email, SecurityQuestion securityQuestion, byte[] userImage) throws IOException {
        String FormattedDate;
        if (myDate != null) {
            FormattedDate = myDate.format(DateTimeFormatter.ofPattern("MMM-dd-yyyy"));
        } else {
            warningLabel.setText("please choose your birth date");
            return;
        }
        User newUser =
                getUser(name, lastName, username, phoneNumber, password, fakePass, email, securityQuestion, userImage, FormattedDate);
        thisUser.setUser(newUser);
        if (file != null)
            thisUser.getUser().setProfilePicAddress(file.toString());
        CommandSender commandSender = new CommandSender(CommandType.SIGNUP, newUser);
        ApprovedType isAllowed = ApprovedType.NOT_APPROVED;
        try {
            Client.getObjectOutputStream().reset();
            Client.getObjectOutputStream().writeObject(commandSender);
            Client.getObjectOutputStream().flush();
            isAllowed = (ApprovedType) Client.getObjectInputStream().readObject();
        } catch (IOException | ClassNotFoundException ioException) {
            ioException.printStackTrace();
        }
        if (isAllowed == ApprovedType.APPROVED)
            Main.loadAPage(event
                    , "../FXMLs/MainPage.fxml"
                    , "SBUgram - Home page"
            );
        else {
            warningLabel.setText(
                    "Username Already in use.Please choose a different username"
            );
        }
    }

    /**
     * this method is actually a trick for showing the password
     * when the user clicks on the showPass check box.
     * the normal textField is shown when the user chooses the
     * checkbox.
     */
    @FXML
    public void show() {
        if (!fakePassField.isVisible()) {
            fakePassField.setVisible(true);
            fakeReWritePass.setVisible(true);
            re_writePassField.setVisible(false);
            password.setVisible(false);
            fakeReWritePass.setText(re_writePassField.getText());
            fakePassField.setText(password.getText());

        } else {
            fakePassField.setVisible(false);
            fakeReWritePass.setVisible(false);
            password.setVisible(true);
            re_writePassField.setVisible(true);
            password.setText(fakePassField.getText());
            re_writePassField.setText(fakeReWritePass.getText());
        }
    }


    /**
     * sets a date when the datePicker is clicked.
     */
    @FXML
    public void pickADate() {
        if (myDatePicker != null)
            myDate = myDatePicker.getValue();
    }

    private File file;

    /**
     * if the user tends to use a profile picture,
     * this method is called and a pic is chosen from his,her computer files!
     */
    public void setProfilePic() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Upload your profile picture");
        file = chooser.showOpenDialog(null);
        if (file != null) {
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

    /**
     * security question is set when this method is called.
     */
    private void getQuestion(ActionEvent actionEvent) {
        this.myQuestion = questions.getValue();
    }

    /**
     * there is a button in the app to return to main menu and here is it's method.
     */
    public void returnToLoginMenu(ActionEvent actionEvent) throws IOException {
        Main.loadAPage(actionEvent
                , "../FXMLs/sample.fxml"
                , "SBUgram - Login menu"
        );
    }

    /**
     * choosing a security question is optional but when it is chosen ,
     * this method saves the question and the answer of the user.
     */
    @FXML
    public void saveAnswerAndQuestion() {
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
