package Weather.model;

import Weather.controller.WeatherServiceRequestStatus;

public class WeatherServiceResult {

    private WeatherForecast weatherForecast;
    private WeatherServiceRequestStatus weatherServiceRequestStatus;

    public WeatherServiceResult(WeatherForecast weatherForecast, WeatherServiceRequestStatus weatherServiceRequestStatus) {
        this.weatherForecast = weatherForecast;
        this.weatherServiceRequestStatus = weatherServiceRequestStatus;
    }

    public WeatherForecast getWeatherForecast() {
        return weatherForecast;
    }

    public void setWeatherForecast(WeatherForecast weatherForecast) {
        this.weatherForecast = weatherForecast;
    }

    public WeatherServiceRequestStatus getWeatherServiceRequestStatus() {
        return weatherServiceRequestStatus;
    }

    public void setWeatherServiceRequestStatus(WeatherServiceRequestStatus weatherServiceRequestStatus) {
        this.weatherServiceRequestStatus = weatherServiceRequestStatus;
    }
}
