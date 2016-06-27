import environment.Kernel;
import environment.unit.Extension;
import javafx.application.Application;
import javafx.stage.Stage;
import scene.SceneSwitcher;

public class Main extends Application {

    public static void main(String[] args) throws Exception {
        new Kernel().up();
    }

    @Override
    public void start(Stage stage) throws Exception {
        Kernel kernel = new Kernel();

        kernel.up();
    }
}