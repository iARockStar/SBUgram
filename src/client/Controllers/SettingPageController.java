package client.Controllers;

import client.Client;
import client.Main;
import client.thisUser;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import other.CommandSender;
import other.CommandType;
import other.SecurityQuestion;
import other.User;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SettingPageController extends mainPage {

    @FXML
    private JFXTextField fakePassField;
    @FXML
    private JFXTextField password;
    @FXML
    private JFXTextField nameField;
    @FXML
    private JFXTextField lastNameField;
    @FXML
    private JFXTextField emailField;
    @FXML
    private JFXDatePicker dateField;
    @FXML
    private JFXTextField numberField;
    @FXML
    private Label warningLabel;
    @FXML
    private Circle profilePicCircle;
    @FXML
    private Image profilePicImage;
    private byte[] userImage;


    public void loadProfile(MouseEvent mouseEvent) throws IOException {
        Main.loadAPageMouse(mouseEvent
                , "../FXMLs/MyProfile.fxml"
                , "SBUgram - Your Profile"
        );
    }


    public void show(ActionEvent actionEvent) {
        if (!fakePassField.isVisible()) {
            fakePassField.setVisible(true);
            password.setVisible(false);
            fakePassField.setText(password.getText());

        } else {
            fakePassField.setVisible(false);
            password.setVisible(true);
            password.setText(fakePassField.getText());
        }
    }

    public void submit(ActionEvent actionEvent) {
        String name = this.nameField.getText();
        if (name.length() != 0)
            thisUser.getUser().setName(name);
        String lastName = this.lastNameField.getText();
        if (lastName.length() != 0)
            thisUser.getUser().setLastName(lastName);
        String phoneNumber = this.numberField.getText();
        if (validPhoneNumber(phoneNumber))
            thisUser.getUser().setPhoneNumber(phoneNumber);
        String password = this.password.getText();

        String fakePass = this.fakePassField.getText();
        if (password.length() != 0 && password.length() >= fakePass.length())
            thisUser.getUser().setPassword(password);
        else if (fakePass.length() != 0)
            thisUser.getUser().setPassword(fakePass);
        String email = this.emailField.getText();
        if (email.length() != 0)
            if (checkValidEmail(email).find())
                thisUser.getUser().setEmail(email);
            else {
                warningLabel.setText("please enter a correct email address please");
                return;
            }
        byte[] userImage = this.userImage;
        if (userImage.length != 0)
            thisUser.getUser().setProfileImage(userImage);
        String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–{}:;',?/*~$^+=<>]).{8,20}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher2 = pattern.matcher(password);
        Matcher matcher1 = pattern.matcher(fakePass);

        if (!matcher2.find() && !matcher1.find()) {
            warningLabel.setText("password not acceptable.");
            return;
        }
        try {
            update(
                    actionEvent,thisUser.getUser()
            );
        } catch (IOException e) {
            e.printStackTrace();
        }

    }



    private boolean validPhoneNumber(String phoneNumber) {
        try {
            Long.parseLong(phoneNumber);
        } catch (Exception e) {
            warningLabel.setText("wrong number");
            return false;
        }
        return true;
    }

    private Matcher checkValidEmail(String email) {
        Pattern VALID_EMAIL_ADDRESS_REGEX =
                Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$"
                        , Pattern.CASE_INSENSITIVE);
        return VALID_EMAIL_ADDRESS_REGEX.matcher(email);
    }

    public void setProfilePic() {
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
            profilePicCircle.setFill(new ImagePattern(profilePicImage));
            profilePicCircle.setEffect(new DropShadow(+25d, 0d, +2d, Color.DARKGREEN));
        }
    }

    private LocalDate myDate = null;

    @FXML
    public void pickADate() {
        if(dateField != null)
        myDate = dateField.getValue();
    }

    private void update(ActionEvent event,User user) throws IOException {
        if(myDate != null) {
            String FormattedDate = myDate.format(DateTimeFormatter.ofPattern("MMM-dd-yyyy"));
            thisUser.getUser().setDatePicker(FormattedDate);
        }
        CommandSender commandSender = new CommandSender(CommandType.SETTING, user);
        Client.getObjectOutputStream().writeObject(commandSender);
        Client.getObjectOutputStream().flush();
        Main.loadAPage(event
                , "../FXMLs/MyProfile.fxml"
                , "SBUgram - Your Profile"
        );
    }
}
