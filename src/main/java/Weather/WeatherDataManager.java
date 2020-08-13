package Weather;

import Weather.model.Place;
import Weather.model.WeatherCondition;
import Weather.model.WeatherForecast;
import Weather.view.IconResolver;

import java.util.ArrayList;
import java.util.List;

public class WeatherDataManager {

    private IconResolver iconResolver;
    private ArrayList<WeatherForecast> weatherForecasts;

    public WeatherDataManager() {
        iconResolver = new IconResolver();
        weatherForecasts = new ArrayList<WeatherForecast>();
    }

    public void setPlaces(List<Place> places){
        for(Place place:places){
            weatherForecasts.add(new WeatherForecast(place));
        }
    }

    public String getCity(int placeNumber) {
        return weatherForecasts.get(placeNumber).getCity();
    }

    public void setCity(int placeNumber, String city) {
        this.weatherForecasts.get(placeNumber).setCity(city);
    }

    public String getCountry(int placeNumber) {
        return weatherForecasts.get(placeNumber).getCountry();
    }

    public void setCountry(int placeNumber, String country) {
        this.weatherForecasts.get(placeNumber).setCountry(country);
    }

    public String getWeatherForDay(int placeNumber, int dayNumber){
        return weatherForecasts.get(placeNumber).getWeatherCondition(dayNumber).getWeatherMain();
    }

    public WeatherCondition getWeatherCondition(int placeNumber, int dayNumber){
        return weatherForecasts.get(placeNumber).getWeatherCondition(dayNumber);
    }

    public WeatherForecast getWeatherForecast(int placeNumber){
        return weatherForecasts.get(placeNumber);
    }

    public ArrayList<WeatherForecast> getWeatherForecasts(){
        return this.weatherForecasts;
    }
}
