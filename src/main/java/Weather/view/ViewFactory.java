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

    public ViewFactory(WeatherDataManager weatherDataManager){
        this.weatherDataManager = weatherDataManager;
    }

    public void showMainWindow(boolean placesAreSet){

        MainWindowController controller = new MainWindowController(weatherDataManager,this, "/view/MainWindow.fxml");
        initializeStage(controller);

        controller.clearView();
        if(placesAreSet) {
            controller.loadWeatherData();
        }

    }

    private void initializeStage(BaseController controller){

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
        stage.show();
    }

    public void closeStage(Stage stage){
        stage.close();
    }
}
