package Weather.controller.services;

import Weather.controller.WeatherServiceRequestStatus;
import Weather.model.Place;
import Weather.model.WeatherCondition;
import Weather.model.WeatherForecast;
import Weather.model.WeatherServiceResult;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class OpenWeatherMapService extends Service<WeatherServiceResult> {

    final private Place place;
    private CloseableHttpClient httpclient;

    public OpenWeatherMapService(Place place) {
        this.place = place;
        httpclient = HttpClients.createDefault();
    }

    public WeatherServiceResult getData() throws Exception {

        WeatherForecast weatherForecast = new WeatherForecast(place);
        WeatherServiceResult weatherServiceResult = new WeatherServiceResult(weatherForecast,
                WeatherServiceRequestStatus.PENDING);
        Double longitude = 0.0;
        Double latitude = 0.0;

        StringBuilder address1 = new StringBuilder();
        address1.append("http://api.openweathermap.org/data/2.5/weather?q=")
                .append(place.getCity())
                .append("," + place.getCountry())
                .append("&appid=" + ApiKey.getApiKey());
        System.out.println("Starting request: " + address1);

        HttpGet httpget = new HttpGet(address1.toString());
        CloseableHttpResponse response = httpclient.execute(httpget);

        //get coordinates for second request
        try {
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                String data = EntityUtils.toString(entity);

                JSONObject json = new JSONObject(data);
                Integer responseCode = getResponseCode(json);

                if (responseCode == 404) {
                    weatherServiceResult.setWeatherServiceRequestStatus(WeatherServiceRequestStatus.FAILED_BY_DATA);
                    return weatherServiceResult;
                } else if (responseCode == 200) {
                    JSONObject coordinates = json.getJSONObject("coord");
                    longitude = getLongitude(coordinates);
                    latitude = getLatitude(coordinates);
                    System.out.println(weatherForecast.getCity() + " " + longitude.toString() + " " + latitude.toString());
                } else {
                    weatherServiceResult.setWeatherServiceRequestStatus(WeatherServiceRequestStatus.FAILED_BY_UNEXPECTED_ERROR);
                    return weatherServiceResult;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            weatherServiceResult.setWeatherServiceRequestStatus(WeatherServiceRequestStatus.FAILED_BY_UNEXPECTED_ERROR);
            return weatherServiceResult;
        } finally {
            response.close();
        }

        StringBuilder address2 = new StringBuilder();
        address2.append("https://api.openweathermap.org/data/2.5/onecall?lat=")
                .append(latitude.toString())
                .append("&lon=" + longitude.toString())
                .append("&exclude=current,minutely,hourly&units=metric&appid=")
                .append(ApiKey.getApiKey());
        System.out.println("Starting request: " + address2);

        httpget = new HttpGet(address2.toString());
        response = httpclient.execute(httpget);

        //second request being made
        try {
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                String data = EntityUtils.toString(entity);
                JSONObject jsonObject = new JSONObject(data);
                JSONArray days = jsonObject.getJSONArray("daily");

                List<WeatherCondition> weatherConditions = new ArrayList<>();
                for (int i = 0; i < days.length(); i++) {
                    weatherConditions.add(new WeatherCondition(days.getJSONObject(i)));
                }

                weatherForecast.setWeatherConditions(weatherConditions);
                weatherForecast.printWeatherConditions();
                weatherServiceResult.setWeatherForecast(weatherForecast);
            }
        } catch (Exception e) {
            e.printStackTrace();
            weatherServiceResult.setWeatherServiceRequestStatus(WeatherServiceRequestStatus.FAILED_BY_UNEXPECTED_ERROR);
            return weatherServiceResult;
        } finally {
            response.close();
        }

        weatherServiceResult.setWeatherServiceRequestStatus(WeatherServiceRequestStatus.SUCCESS);
        return weatherServiceResult;
    }

    @Override
    protected Task<WeatherServiceResult> createTask() {
        return new Task<>() {
            @Override
            protected WeatherServiceResult call() throws Exception {
                return getData();
            }
        };
    }

    private Double getLongitude(JSONObject coordinates) {
        return coordinates.getDouble("lon");
    }

    private Double getLatitude(JSONObject coordinates) {
        return coordinates.getDouble("lat");
    }

    private Integer getResponseCode(JSONObject json) {

        Object responseCode = json.get("cod");

        //ensuring that response code will be Integer
        //because API sometimes uses String

        if (responseCode instanceof String) {
            return Integer.parseInt((String) responseCode);
        } else if (responseCode instanceof Integer) {
            return (Integer) responseCode;
        } else {
            return 0;
        }
    }
}
