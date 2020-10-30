package Weather.view;

import Weather.WeatherDataManager;
import Weather.controller.*;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

import java.io.IOException;

public class ViewFactory {

    private WeatherDataManager weatherDataManager;
    private final static String cssFileName = "/css/themeDark.css";

    public ViewFactory(WeatherDataManager weatherDataManager) {
        this.weatherDataManager = weatherDataManager;
    }

    public void showMainWindow(boolean placesAreSet){

        MainWindowController controller = createMainWindowController();
        initializeStage(controller);

        controller.clearView();
        if (placesAreSet) {
            controller.loadWeatherData();
        }

    }

    protected void initializeStage(BaseController controller){

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(controller.getFxmlName()));
        fxmlLoader.setController(controller);
        Parent parent;

        try{
            parent = fxmlLoader.load();
        }
        catch (IOException error){
            error.printStackTrace();
            return;
        }

        Scene scene = new Scene(parent);
        Stage stage = new Stage();
        stage.setScene(scene);
        scene.getStylesheets().clear();
        scene.getStylesheets().add(getClass().getResource(cssFileName).toExternalForm());

        stage.show();
    }

    public void closeStage(Stage stage){
        stage.close();
    }

    protected MainWindowController createMainWindowController() {
        return new MainWindowController(weatherDataManager, this, "/view/MainWindow.fxml");
    }
}
