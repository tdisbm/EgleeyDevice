import environment.Kernel;
import javafx.application.Application;
import javafx.stage.Stage;
import scene.SceneSwitcher;

public class Main extends Application {

    public static void main(String[] args) throws Exception {
        //SceneSwitcher switcher = new SceneSwitcher(stage);

        Kernel kernel = new Kernel();

        kernel.ini();
        //launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        SceneSwitcher switcher = new SceneSwitcher(stage);

        Kernel kernel = new Kernel();

        kernel.ini();
//        SceneResolver sceneResolver = new SceneResolver();
//
//        sceneResolver
//            .addResource(new File("config/scenes.yml"))
//            .addResource(new File("config/scenes2.yml"));
//
//        sceneResolver.resolve();
//        sceneResolver.getMainScene();
//
//        switcher.load("/view/hello.fxml").show();
    }
}