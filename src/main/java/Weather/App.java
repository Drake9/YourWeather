package Weather;

import Weather.controller.services.OpenWeatherMapService;
import Weather.view.ViewFactory;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * JavaFX App
 */
public class App extends Application {

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) {

        ViewFactory viewFactory = new ViewFactory();
        viewFactory.showMainWindow();

        OpenWeatherMapService weatherMapService = new OpenWeatherMapService("Poland","Cracow");
        weatherMapService.start();
    }
}