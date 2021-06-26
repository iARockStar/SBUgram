package client;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import other.CommandSender;
import other.CommandType;

import java.io.IOException;

/**
 * <h1>SBUgram</h1>
 * SBUgram application is for sharing your day-to-day
 * tweets and pics with your friends and family!
 * @author iliya Akhoondi
 * @version 1.0
 * @since 2021-6-4
 */

public class Main extends Application {
    /**
     * main method is for launching the application and
     * is called before all other methods.
     * this method helps clientSide and graphics connect to Server.
     * @param args param for the launch method
     */

    public static void main(String[] args) {
        Client.connectToServer();
        Client.setServerUp(true);
        launch(args);
    }

    /**
     * start method is called when the launch method in main is called.
     * <p>
     * This method loads first page of the app
     * which is the LoginController.
     * it also provides calling logout method when closing the app.
     *
     * @param primaryStage sets the stage.
     * @throws Exception handles any exception.
     */

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("../FXMLs/sample.fxml"));
        primaryStage.setTitle("SBUgram - Login menu");
        primaryStage.setScene(new Scene(root));
        primaryStage.initStyle(StageStyle.DECORATED);
        primaryStage.setResizable(false);
        primaryStage.show();

        Image image = new Image("images/Screenshot (122).png");
        primaryStage.getIcons().addAll(image);


        primaryStage.setOnCloseRequest(event -> {
            event.consume();
            logout(primaryStage);
        });

    }

    /**
     * this method helps the whole app to load pages.
     * it gives an event and loads the page related to the event.
     *
     * @param event event which we want to control its occurrence.
     * @param FXMLPath param which contains fxmlPath.
     * @param controllerName the name of the controller of the loading page.
     * @throws IOException
     */

    public static void loadAPage(ActionEvent event, String FXMLPath, String controllerName) throws IOException {
        Parent root = FXMLLoader.load(Main.class.getResource(FXMLPath));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle(controllerName);
        stage.show();
    }

    /**
     * same method as the loadAPage method but has
     * the event as a mouse event.
     *
     * @param event event which we want to control its occurrence.
     * @param FXMLPath param which contains fxmlPath.
     * @param controllerName the name of the controller of the loading page.
     * @throws IOException
     */
    public static void loadAPageMouse(MouseEvent event, String FXMLPath, String controllerName) throws IOException {
        Parent root = FXMLLoader.load(Main.class.getResource(FXMLPath));

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle(controllerName);

        stage.show();
    }


    /**
     * logout method is called whenever the user wants to close the app.
     * it alerts the user that the app is closing so that
     * the user decides whether he/she wants to exit.
     *
     * @param stage closing this stage for closing the app.
     */
    public void logout(Stage stage) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout");
        alert.setHeaderText("you are about to close the app");
        alert.setContentText("Are you sure? ");
        if (alert.showAndWait().get() == ButtonType.OK) {
            Client.setServerUp(false);
            stage.close();
            try {
                Client.objectOutputStream.writeObject(new CommandSender(CommandType.LOGOUT, thisUser.getUser(), thisUser.getUser()));
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }


}
