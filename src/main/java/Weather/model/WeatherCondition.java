package Weather.model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class WeatherCondition {

    private final LocalDate date;
    private final Double temperature;
    private final String weatherMain;
    private final String weatherDescription;

    public WeatherCondition(JSONObject json) {

        date = extractDateFromJson(json);
        temperature = extractTemperatureFromJson(json);

        JSONObject weatherDetails = extractWeatherDetailsFromJson(json);

        weatherMain = extractWeatherMainFromJson(weatherDetails);
        weatherDescription = extractWeatherDescriptionFromJson(weatherDetails);
    }

    public String getDateAsString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("E dd.MM", Locale.ENGLISH);
        return date.format(formatter);
    }

    public Double getTemperature() {
        return temperature;
    }

    public String getWeatherMain() {
        return weatherMain;
    }

    public String getWeatherDescription() {
        return weatherDescription;
    }

    private LocalDate extractDateFromJson(JSONObject json) {

        long epoch = json.getLong("dt");
        return Instant.ofEpochSecond(epoch).atZone(ZoneId.systemDefault()).toLocalDate();
    }

    private double extractTemperatureFromJson(JSONObject json) {

        JSONObject temperatures = json.getJSONObject("temp");
        double temperatureToTruncate = temperatures.getDouble("day");

        return BigDecimal.valueOf(temperatureToTruncate).setScale(1, RoundingMode.HALF_UP).doubleValue();
    }

    private JSONObject extractWeatherDetailsFromJson(JSONObject json) {

        JSONArray weather = json.getJSONArray("weather");
        return weather.getJSONObject(0);
    }

    private String extractWeatherMainFromJson(JSONObject json) {

        return json.getString("main");
    }

    private String extractWeatherDescriptionFromJson(JSONObject json) {

        return json.getString("description");
    }
}
