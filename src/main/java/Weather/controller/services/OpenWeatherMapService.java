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

    public OpenWeatherMapService(){
        this.place = new Place();
        httpclient = HttpClients.createDefault();
    }

    public OpenWeatherMapService(Place place) {
        this.place = place;
        httpclient = HttpClients.createDefault();
    }

    public WeatherServiceResult getData() throws Exception {

        WeatherForecast weatherForecast = new WeatherForecast(place);
        WeatherServiceResult weatherServiceResult = new WeatherServiceResult(weatherForecast,
                WeatherServiceRequestStatus.PENDING);

        //get coordinates for second request
        String address1 = getUrlForCoordinatesServiceRequest(place.getCountry(), place.getCity());
        System.out.println("Starting request: " + address1);

        HttpGet httpget = new HttpGet(address1);
        CloseableHttpResponse response = httpclient.execute(httpget);

        //get data from response
        try {
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                String data = EntityUtils.toString(entity);
                JSONObject json = new JSONObject(data);
                weatherServiceResult = getPlaceCoordinatesFromJsonResponse(weatherServiceResult, json);
            } else {
                weatherServiceResult.setWeatherServiceRequestStatus(WeatherServiceRequestStatus.FAILED_BY_UNEXPECTED_ERROR);
            }

        } catch (Exception e) {
            e.printStackTrace();
            weatherServiceResult.setWeatherServiceRequestStatus(WeatherServiceRequestStatus.FAILED_BY_UNEXPECTED_ERROR);
        } finally {
            response.close();
        }

        if (weatherServiceResult.getWeatherServiceRequestStatus() != WeatherServiceRequestStatus.PENDING) {
            return weatherServiceResult;
        }

        //second request being made - weather data
        Place placeWithCoordinates = weatherServiceResult.getWeatherForecast().getPlace();
        String address2 = getUrlForWeatherServiceRequest(placeWithCoordinates.getLongitude(), placeWithCoordinates.getLatitude());
        System.out.println("Starting request: " + address2);

        httpget = new HttpGet(address2);
        response = httpclient.execute(httpget);

        //get data from response
        try {
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                String data = EntityUtils.toString(entity);
                JSONObject jsonObject = new JSONObject(data);
                weatherServiceResult = getWeatherDataFromJsonResponse(weatherServiceResult, jsonObject);
            } else {
                weatherServiceResult.setWeatherServiceRequestStatus(WeatherServiceRequestStatus.FAILED_BY_UNEXPECTED_ERROR);
            }

        } catch (Exception e) {
            e.printStackTrace();
            weatherServiceResult.setWeatherServiceRequestStatus(WeatherServiceRequestStatus.FAILED_BY_UNEXPECTED_ERROR);
        } finally {
            response.close();
        }

        if (weatherServiceResult.getWeatherServiceRequestStatus() == WeatherServiceRequestStatus.PENDING) {
            weatherServiceResult.setWeatherServiceRequestStatus(WeatherServiceRequestStatus.SUCCESS);
        }

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

    private String getUrlForCoordinatesServiceRequest(String country, String city) {

        StringBuilder address = new StringBuilder();
        address.append("http://api.openweathermap.org/data/2.5/weather?q=")
                .append(city)
                .append("," + country)
                .append("&appid=" + ApiKey.getApiKey());

        return address.toString();
    }

    private String getUrlForWeatherServiceRequest(Double longitude, Double latitude) {

        StringBuilder address = new StringBuilder();
        address.append("https://api.openweathermap.org/data/2.5/onecall?lat=")
                .append(latitude.toString())
                .append("&lon=" + longitude.toString())
                .append("&exclude=current,minutely,hourly&units=metric&appid=")
                .append(ApiKey.getApiKey());

        return address.toString();
    }

    protected WeatherServiceResult getPlaceCoordinatesFromJsonResponse(WeatherServiceResult weatherServiceResult,
                                                               JSONObject json) {

        Integer responseCode = getResponseCode(json);

        if (responseCode == 200) {
            JSONObject coordinates = json.getJSONObject("coord");

            Place place = weatherServiceResult.getWeatherForecast().getPlace();
            place.setLongitude(coordinates.getDouble("lon"));
            place.setLatitude(coordinates.getDouble("lat"));

            System.out.println(place.getCity() + " " + place.getLongitude().toString() + " " + place.getLatitude().toString());
        } else if (responseCode == 404) {
            weatherServiceResult.setWeatherServiceRequestStatus(WeatherServiceRequestStatus.FAILED_BY_DATA);
        } else {
            weatherServiceResult.setWeatherServiceRequestStatus(WeatherServiceRequestStatus.FAILED_BY_UNEXPECTED_ERROR);
        }

        return weatherServiceResult;
    }

    protected WeatherServiceResult getWeatherDataFromJsonResponse(WeatherServiceResult weatherServiceResult,
                                                              JSONObject jsonObject) {

        JSONArray days = jsonObject.getJSONArray("daily");

        List<WeatherCondition> weatherConditions = new ArrayList<>();
        for (int i = 0; i < days.length(); i++) {
            weatherConditions.add(new WeatherCondition(days.getJSONObject(i)));
        }

        WeatherForecast weatherForecast = weatherServiceResult.getWeatherForecast();
        weatherForecast.setWeatherConditions(weatherConditions);
        weatherForecast.printWeatherConditions();

        return weatherServiceResult;
    }

    protected Integer getResponseCode(JSONObject json) {

        if(json.has("cod")) {
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
        } else {
            return 0;
        }
    }
}
