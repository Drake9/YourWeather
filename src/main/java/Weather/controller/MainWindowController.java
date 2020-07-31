package Weather.controller;

import Weather.view.ViewFactory;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;


public class MainWindowController extends BaseController{

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

    public MainWindowController(ViewFactory viewFactory, String fxmlName) {
        super(viewFactory, fxmlName);
    }
}
