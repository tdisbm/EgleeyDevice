package scene;


import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class SceneSwitcher
{
    private FXMLLoader loader = new FXMLLoader();

    private Stage stage;

    private Scene scene;

    private Parent root;

    /**
     * @param stage Stage
     */
    public SceneSwitcher(Stage stage)
    {
        this.stage = stage;
        this.scene = null;
    }

    /**
     * @param fxmlPath String
     * @return Scene
     * @throws Exception
     */
    public Stage load(String fxmlPath) throws Exception {
        this.loader.setRoot(null);
        this.root = this.loader.load(getClass().getResourceAsStream(fxmlPath));
        this.setSceneRoot(root);
        this.stage.setScene(this.scene);

        return this.stage;
    }

    /**
     * @param stage Stage
     * @return this
     */
    public SceneSwitcher setStage(Stage stage)
    {
        this.stage = stage;

        return this;
    }

    /**
     * @return Stage
     */
    public Stage getStage()
    {
        return this.stage;
    }

    /**
     * @param root Parent
     */
    private SceneSwitcher setSceneRoot(Parent root)
    {
        if (null == this.scene) {
            this.scene = new Scene(root);
        } else {
            this.scene.setRoot(root);
        }

        return this;
    }
}
