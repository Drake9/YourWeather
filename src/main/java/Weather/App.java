package Weather;

import Weather.controller.persistence.PersistenceAccess;
import Weather.model.Place;
import Weather.model.WeatherForecast;
import Weather.view.ViewFactory;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

/**
 * JavaFX App
 */
public class App extends Application {

    private WeatherDataManager weatherDataManager = new WeatherDataManager();
    private PersistenceAccess persistenceAccess = new PersistenceAccess();

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {

        ViewFactory viewFactory = new ViewFactory(weatherDataManager);

        List<Place> listOfPlaces = persistenceAccess.loadFromPersistence();

        if(listOfPlaces.size() > 0){
            weatherDataManager.setPlaces(listOfPlaces);
            viewFactory.showMainWindow(true);
        }else{
            listOfPlaces.add(new Place("", ""));
            listOfPlaces.add(new Place("", ""));
            weatherDataManager.setPlaces(listOfPlaces);
            viewFactory.showMainWindow(false);
        }
    }


    @Override
    public void stop() throws Exception {
        List<Place> listOfPlaces = new ArrayList<Place>();
        for(WeatherForecast weatherForecast: weatherDataManager.getWeatherForecasts()){
            listOfPlaces.add(weatherForecast.getPlace());
        }
        persistenceAccess.saveToPersistence(listOfPlaces);
    }
}