import environment.Kernel;
import javafx.application.Application;
import javafx.stage.Stage;
import scene.SceneSwitcher;

public class Main extends Application {

    public static void main(String[] args) throws Exception {
        //SceneSwitcher switcher = new SceneSwitcher(stage);

        Kernel kernel = new Kernel();

        kernel.up();
        //launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        SceneSwitcher switcher = new SceneSwitcher(stage);

        Kernel kernel = new Kernel();

        kernel.up();
    }
}