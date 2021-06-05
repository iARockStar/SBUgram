package client;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.nio.file.Paths;


public class PageLoader {

    //load fxmls with a special controller set in the runtime
    public void load(String fxml, Object controller) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("../FXMLs/" + fxml + ".fxml"));
        fxmlLoader.setController(controller);
        fxmlLoader.load();
    }
}
