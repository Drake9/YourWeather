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
    private ViewFactory viewFactory = new ViewFactory(weatherDataManager);

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {

        List<Place> listOfPlaces = loadFromPersistence();

        if(listOfPlaces.size() > 0){
            setPlaces(listOfPlaces);
            showMainWindow(true);
        }else{
            listOfPlaces.add(new Place("", ""));
            listOfPlaces.add(new Place("", ""));
            setPlaces(listOfPlaces);
            showMainWindow(false);
        }
    }


    @Override
    public void stop() throws Exception {
        List<Place> listOfPlaces = new ArrayList<Place>();
        for(WeatherForecast weatherForecast: weatherDataManager.getWeatherForecasts()){
            listOfPlaces.add(weatherForecast.getPlace());
        }
        saveToPersistence(listOfPlaces);
    }

    protected List<Place> loadFromPersistence() {
        return persistenceAccess.loadFromPersistence();
    }

    protected void saveToPersistence(List<Place> listOfPlaces) {
        persistenceAccess.saveToPersistence(listOfPlaces);
    }

    protected void setPlaces(List<Place> listOfPlaces) {
        weatherDataManager.setPlaces(listOfPlaces);
    }

    protected void showMainWindow(Boolean placesAreSet) {
        viewFactory.showMainWindow(placesAreSet);
    }
}