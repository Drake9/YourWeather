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

    public WeatherCondition(JSONObject json) throws IllegalArgumentException{

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

        if (json.has("dt")) {
            long epoch = json.getLong("dt");
            return Instant.ofEpochSecond(epoch).atZone(ZoneId.systemDefault()).toLocalDate();
        } else {
            throw new IllegalArgumentException("\"dt\" keyword not found in json: " + json.toString());
        }
    }

    private double extractTemperatureFromJson(JSONObject json) {

        if (json.has("temp")) {
            JSONObject temperatures = json.getJSONObject("temp");

            if (temperatures.has("day")) {
                double temperatureToTruncate = temperatures.getDouble("day");
                return BigDecimal.valueOf(temperatureToTruncate).setScale(1, RoundingMode.HALF_UP).doubleValue();
            } else {
                throw new IllegalArgumentException("\"day\" keyword not found in json: " + temperatures.toString());
            }

        } else {
            throw new IllegalArgumentException("\"temp\" keyword not found in json: " + json.toString());
        }
    }

    private JSONObject extractWeatherDetailsFromJson(JSONObject json) {

        if (json.has("weather")) {
            JSONArray weather = json.getJSONArray("weather");

            if (weather.length() != 0) {
                return weather.getJSONObject(0);
            } else {
                throw new IllegalArgumentException("\"weather\" array is empty in json: " + json.toString());
            }

        } else {
            throw new IllegalArgumentException("\"weather\" keyword not found in json: " + json.toString());
        }
    }

    private String extractWeatherMainFromJson(JSONObject json) {

        if (json.has("main")) {
            return json.getString("main");
        } else {
            throw new IllegalArgumentException("\"main\" keyword not found in json: " + json.toString());
        }
    }

    private String extractWeatherDescriptionFromJson(JSONObject json) {

        if (json.has("description")) {
            return json.getString("description");
        } else {
            throw new IllegalArgumentException("\"description\" keyword not found in json: " + json.toString());
        }
    }
}
