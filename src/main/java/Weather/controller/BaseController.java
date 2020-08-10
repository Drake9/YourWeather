package Weather.controller;

import Weather.WeatherDataManager;
import Weather.view.ViewFactory;

public abstract class BaseController {

    protected WeatherDataManager weatherDataManager;
    protected ViewFactory viewFactory;
    private String fxmlName;

    public BaseController(WeatherDataManager weatherDataManager,ViewFactory viewFactory, String fxmlName) {
        this.weatherDataManager = weatherDataManager;
        this.viewFactory = viewFactory;
        this.fxmlName = fxmlName;
    }

    public String getFxmlName(){
        return fxmlName;
    }
}


