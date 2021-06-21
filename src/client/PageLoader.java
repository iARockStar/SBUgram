package client;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.nio.file.Paths;

/**
 * actually, i only used this class for the items and that's it.
 * in other parts i just used loadAPage method in the main class.
 */
public class PageLoader {

    /**load FXMLs with a special controller set in the runtime.
     * @param fxml FXML which we tend to load.
     * @param controller controller which controls the page.
     * @throws IOException
     */
    public void load(String fxml, Object controller) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("../FXMLs/" + fxml + ".fxml"));
        fxmlLoader.setController(controller);
        fxmlLoader.load();
    }
}
