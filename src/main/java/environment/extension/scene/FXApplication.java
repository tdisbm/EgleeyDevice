package environment.extension.scene;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;

public class FXApplication extends Application {

    FXMLLoader loader = new FXMLLoader();

    public FXApplication() {
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

    }

    final public FXApplication addController() {
        return this;
    }

    final public void up(String[] args) {
        launch(args);
    }
}
