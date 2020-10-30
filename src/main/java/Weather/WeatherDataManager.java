package Weather;

import Weather.model.Place;
import Weather.model.WeatherCondition;
import Weather.model.WeatherForecast;

import java.util.ArrayList;
import java.util.List;

public class WeatherDataManager {

    private ArrayList<WeatherForecast> weatherForecasts;

    public WeatherDataManager() {
        weatherForecasts = new ArrayList<>();
    }

    public void setPlaces(List<Place> places) {
        for (Place place : places) {
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

    public Place getPlace(int placeNumber) {
        return weatherForecasts.get(placeNumber).getPlace();
    }

    public String getWeatherForDay(int placeNumber, int dayNumber) {
        return weatherForecasts.get(placeNumber).getWeatherCondition(dayNumber).getWeatherMain();
    }

    public WeatherCondition getWeatherCondition(int placeNumber, int dayNumber) {
        return weatherForecasts.get(placeNumber).getWeatherCondition(dayNumber);
    }

    public void setWeatherForecast(int placeNumber, WeatherForecast weatherForecast) {
        weatherForecasts.set(placeNumber, weatherForecast);
    }

    public ArrayList<WeatherForecast> getWeatherForecasts() {
        return this.weatherForecasts;
    }
}
