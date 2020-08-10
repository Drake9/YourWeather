package Weather;

import Weather.controller.WeatherServiceResult;
import Weather.controller.services.OpenWeatherMapService;
import Weather.model.WeatherCondition;
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

    private WeatherDataManager weatherDataManager = new WeatherDataManager();

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) {

        ViewFactory viewFactory = new ViewFactory(weatherDataManager);

        OpenWeatherMapService weatherService = new OpenWeatherMapService(weatherDataManager);
        weatherService.start();


        weatherService.setOnSucceeded(event -> {

            WeatherServiceResult weatherServiceResult = weatherService.getValue();

            switch (weatherServiceResult) {
                case SUCCESS:
                    viewFactory.showMainWindow();
                    break;
                default:
                    System.out.println("unexpected failure");
                    break;
            }

        });
    }
}