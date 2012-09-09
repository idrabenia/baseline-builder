package idrobenya.baseline;

import idrobenya.baseline.model.config.Config;
import idrobenya.baseline.ui.main.MainScreen;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.stage.Window;

/**
 *
 */
public class Main extends Application {
    private static Window mainWindow;

    public static Window getMainWindow() {
        return mainWindow;
    }

    public static void main(String[] args) {
        Config.getInstance();
        Application.launch(Main.class, args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        mainWindow = stage;

        new MainScreen(stage);
        stage.setTitle("Baseline Maker");
        stage.setResizable(false);
        Platform.setImplicitExit(true);

        stage.show();
    }

}
