package Weather.controller;

import Weather.WeatherDataManager;
import Weather.controller.services.OpenWeatherMapService;
import Weather.model.WeatherForecast;
import Weather.model.WeatherServiceResult;
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
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ResourceBundle;


public class MainWindowController extends BaseController implements Initializable {

    private IconResolver iconResolver = new IconResolver();
    private static final int NUMBER_OF_DAYS_DISPLAYED = 7;

    @FXML
    private TextField countryName1;

    @FXML
    private TextField cityName1;

    @FXML
    private TextField countryName2;

    @FXML
    private TextField cityName2;

    @FXML
    private ImageView weatherImage11;

    @FXML
    private ImageView weatherImage12;

    @FXML
    private ImageView weatherImage13;

    @FXML
    private ImageView weatherImage14;

    @FXML
    private ImageView weatherImage15;

    @FXML
    private ImageView weatherImage16;

    @FXML
    private ImageView weatherImage17;

    @FXML
    private Label day11;

    @FXML
    private Label temperature11;

    @FXML
    private Label weather11;

    @FXML
    private Label day12;

    @FXML
    private Label temperature12;

    @FXML
    private Label weather12;

    @FXML
    private Label day13;

    @FXML
    private Label temperature13;

    @FXML
    private Label weather13;

    @FXML
    private Label day14;

    @FXML
    private Label temperature14;

    @FXML
    private Label weather14;

    @FXML
    private Label day15;

    @FXML
    private Label temperature15;

    @FXML
    private Label weather15;

    @FXML
    private Label day16;

    @FXML
    private Label temperature16;

    @FXML
    private Label weather16;

    @FXML
    private Label day17;

    @FXML
    private Label temperature17;

    @FXML
    private Label weather17;

    @FXML
    private Label errorLabel1;

    @FXML
    private Label errorLabel2;

    @FXML
    private ImageView weatherImage21;

    @FXML
    private ImageView weatherImage22;

    @FXML
    private ImageView weatherImage23;

    @FXML
    private ImageView weatherImage24;

    @FXML
    private ImageView weatherImage25;

    @FXML
    private ImageView weatherImage26;

    @FXML
    private ImageView weatherImage27;

    @FXML
    private Label day21;

    @FXML
    private Label temperature21;

    @FXML
    private Label weather21;

    @FXML
    private Label day22;

    @FXML
    private Label temperature22;

    @FXML
    private Label weather22;

    @FXML
    private Label day23;

    @FXML
    private Label temperature23;

    @FXML
    private Label weather23;

    @FXML
    private Label day24;

    @FXML
    private Label temperature24;

    @FXML
    private Label weather24;

    @FXML
    private Label day25;

    @FXML
    private Label temperature25;

    @FXML
    private Label weather25;

    @FXML
    private Label day26;

    @FXML
    private Label temperature26;

    @FXML
    private Label weather26;

    @FXML
    private Label day27;

    @FXML
    private Label temperature27;

    @FXML
    private Label weather27;


    public MainWindowController(WeatherDataManager weatherDataManager, ViewFactory viewFactory, String fxmlName) {
        super(weatherDataManager, viewFactory, fxmlName);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        countryName1.setText(weatherDataManager.getCountry(0));
        countryName2.setText(weatherDataManager.getCountry(1));
        cityName1.setText(weatherDataManager.getCity(0));
        cityName2.setText(weatherDataManager.getCity(1));
    }

    @FXML
    void refreshWeatherDataButtonAction() {

        loadWeatherData();
    }

    // left (user) column: 1 ||||| right (holiday) column: 2
    private boolean checkColumnWeatherData(int columnNumber) {
        Scene scene = day11.getScene();

        TextField countryField = (TextField) scene.lookup("#countryName" + columnNumber);
        String country = countryField.getText();

        TextField cityField = (TextField) scene.lookup("#cityName" + columnNumber);
        String city = cityField.getText();

        Label errorLabel = (Label) scene.lookup("#errorLabel" + columnNumber);

        if (country.equals("") && city.equals("")) {
            errorLabel.setText(SystemMessages.NO_CITY_AND_COUNTRY);
            return false;
        } else if (country.equals("")) {
            errorLabel.setText(SystemMessages.NO_COUNTRY);
            return false;
        } else if (city.equals("")) {
            errorLabel.setText(SystemMessages.NO_CITY);
            return false;
        } else {
            errorLabel.setText("");
            weatherDataManager.setCountry(columnNumber - 1, country);
            weatherDataManager.setCity(columnNumber - 1, city);
            return true;
        }
    }

    public void loadWeatherData() {
        if (checkColumnWeatherData(1)) {
            loadColumnWeatherData(1);
        }

        if (checkColumnWeatherData(2)) {
            loadColumnWeatherData(2);
        }
    }

    // left (user) column: 1 ||||| right (holiday) column: 2
    private void loadColumnWeatherData(int columnNumber) {

        Scene scene = day11.getScene();
        Label label = (Label) scene.lookup("#errorLabel" + columnNumber);
        OpenWeatherMapService weatherService =
                new OpenWeatherMapService(weatherDataManager.getPlace(columnNumber - 1));
        weatherService.start();

        weatherService.setOnSucceeded(event -> {

            WeatherServiceResult weatherServiceResult = weatherService.getValue();
            WeatherForecast weatherForecast = weatherServiceResult.getWeatherForecast();
            WeatherServiceRequestStatus weatherServiceRequestStatus = weatherServiceResult.getWeatherServiceRequestStatus();

            switch (weatherServiceRequestStatus) {
                case SUCCESS:
                    weatherDataManager.setWeatherForecast(columnNumber - 1, weatherForecast);
                    showColumnWeatherData(columnNumber);
                    break;
                case FAILED_BY_DATA:
                    label.setText(SystemMessages.FAILED_BY_DATA);
                    break;
                case FAILED_BY_NETWORK:
                    label.setText(SystemMessages.FAILED_BY_NETWORK);
                    break;
                case PENDING:
                    label.setText(SystemMessages.PENDING);
                    break;
                default:
                    label.setText(SystemMessages.UNKNOWN_ERROR);
                    break;
            }

        });
    }

    // left (user) column: 1 |||| right (holiday) column: 2

    private void showColumnWeatherData(int columnNumber) {

        String iconLocation;
        Scene scene = day11.getScene();
        TextField country = (TextField) scene.lookup("#countryName" + columnNumber);
        country.setText(weatherDataManager.getCountry(columnNumber - 1));
        TextField city = (TextField) scene.lookup("#cityName" + columnNumber);
        city.setText(weatherDataManager.getCity(columnNumber - 1));

        for (int i = 1; i <= NUMBER_OF_DAYS_DISPLAYED; i++) {
            try {

                ImageView imageView = (ImageView) scene.lookup("#weatherImage" + columnNumber + i);
                iconLocation = iconResolver.getIconForWeather(weatherDataManager.getWeatherForDay(columnNumber - 1,
                        i - 1));
                imageView.setImage(new Image(new FileInputStream(iconLocation)));

            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
                Label label = (Label) scene.lookup("#errorLabel" + columnNumber);
                label.setText(SystemMessages.IMAGE_FILE_NOT_FOUND);
            }

            Label label = (Label) scene.lookup("#day" + columnNumber + i);
            label.setText(weatherDataManager.getWeatherCondition(columnNumber - 1, i - 1).getDateAsString());
            label = (Label) scene.lookup("#temperature" + columnNumber + i);
            label.setText(weatherDataManager.getWeatherCondition(columnNumber - 1, i - 1).getTemperature() + "\u00B0" +
                    "C");
            label = (Label) scene.lookup("#weather" + columnNumber + i);
            label.setText(weatherDataManager.getWeatherCondition(columnNumber - 1, i - 1).getWeatherDescription());
        }
    }

    public void clearView() {

        Scene scene = day11.getScene();
        for (int i = 1; i <= NUMBER_OF_DAYS_DISPLAYED; i++) {
            ImageView imageView = (ImageView) scene.lookup("#weatherImage1" + i);
            imageView.setImage(null);
            imageView = (ImageView) scene.lookup("#weatherImage2" + i);
            imageView.setImage(null);

            clearLabel(scene, "#day1" + i);
            clearLabel(scene, "#temperature1" + i);
            clearLabel(scene, "#weather1" + i);
            clearLabel(scene, "#day2" + i);
            clearLabel(scene, "#temperature2" + i);
            clearLabel(scene, "#weather2" + i);
        }
    }

    private void clearLabel(Scene scene, String id) {

        Label label;
        label = (Label) scene.lookup(id);
        label.setText("");
    }
}
