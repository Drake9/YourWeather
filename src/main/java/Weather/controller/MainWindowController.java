package Weather.controller;

import Weather.WeatherDataManager;
import Weather.view.IconResolver;
import Weather.view.ViewFactory;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.FileInputStream;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ResourceBundle;


public class MainWindowController extends BaseController implements Initializable {

    IconResolver iconResolver = new IconResolver();

    @FXML
    private TextField userCountry;

    @FXML
    private TextField userCity;

    @FXML
    private TextField holidayCountry;

    @FXML
    private TextField holidayCity;

    @FXML
    private ImageView userWeather1;

    @FXML
    private ImageView userWeather2;

    @FXML
    private ImageView userWeather3;

    @FXML
    private ImageView userWeather4;

    @FXML
    private ImageView userWeather5;

    @FXML
    private Label day1;

    @FXML
    private Label temperature1;

    @FXML
    private Label weather1;


    public MainWindowController(WeatherDataManager weatherDataManager, ViewFactory viewFactory, String fxmlName) {
        super(weatherDataManager, viewFactory, fxmlName);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //removed because of not working;
    }

    public void loadWeatherData() {
        String iconLocation = "";
        try {
            for (int i = 1; i <= 7; i++) {
                Scene scene = day1.getScene();
                ImageView imageView = (ImageView) scene.lookup("#weatherImage" + i);
                iconLocation = iconResolver.getIconForWeather(weatherDataManager.getWeatherForDay(i-1));
                imageView.setImage(new Image(new FileInputStream(iconLocation)));

                Label label = (Label) scene.lookup("#day" + i);
                label.setText(weatherDataManager.getWeatherCondition(i-1).getDateAsString());
                label = (Label) scene.lookup("#temperature" + i);
                label.setText(weatherDataManager.getWeatherCondition(i-1).getTemperature() + "\u00B0"+"C");
                label = (Label) scene.lookup("#weather" + i);
                label.setText(weatherDataManager.getWeatherCondition(i-1).getWeatherDescription());
            }

        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
