import environment.Kernel;
import javafx.application.Application;
import javafx.stage.Stage;

public class AppRun extends Application {

    public static void main(String[] args) throws Exception {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        new Kernel().up();
    }
}