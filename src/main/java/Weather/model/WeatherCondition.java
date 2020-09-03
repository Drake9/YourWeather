package Weather.model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class WeatherCondition {

    private Date date;
    private Double temperature;
    private String weatherMain;
    private String weatherDescription;

    public WeatherCondition(JSONObject json){

        Integer timeStamp = (Integer) json.get("dt");
        this.date = new Date((long)timeStamp*1000);

        JSONObject temperatures = json.getJSONObject("temp");
        Double toBeTruncated = ((Number)temperatures.get("day")).doubleValue();
        toBeTruncated -= 273.15;

        this.temperature = BigDecimal.valueOf(toBeTruncated).setScale(1, RoundingMode.HALF_UP).doubleValue();

        JSONArray weather = json.getJSONArray("weather");
        JSONObject weatherDetails = weather.getJSONObject(0);

        this.weatherMain = (String)weatherDetails.get("main");
        this.weatherDescription = (String)weatherDetails.get("description");
    }

    public String getDateAsString(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("E dd.MM", Locale.ENGLISH);
        return dateFormat.format(date);
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
}
