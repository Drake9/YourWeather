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

import java.io.IOException;
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

        //get coordinates for second request
        String address1 = getUrlForCoordinatesServiceRequest(place.getCountry(), place.getCity());
        System.out.println("Starting request: " + address1);

        HttpGet httpget = new HttpGet(address1);
        CloseableHttpResponse response = httpclient.execute(httpget);

        //get data from response
        try {
            weatherServiceResult = getPlaceCoordinatesFromResponse(weatherServiceResult, response);
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
            weatherServiceResult = getWeatherDataFromResponse(weatherServiceResult, response);
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

    protected WeatherServiceResult getPlaceCoordinatesFromResponse(WeatherServiceResult weatherServiceResult,
                                                              CloseableHttpResponse response) throws IOException {

        HttpEntity entity = response.getEntity();

        if (entity != null) {
            String data = EntityUtils.toString(entity);

            JSONObject json = new JSONObject(data);
            Integer responseCode = getResponseCode(json);

            if (responseCode == 200) {
                Place place = weatherServiceResult.getWeatherForecast().getPlace();
                JSONObject coordinates = json.getJSONObject("coord");
                place.setLongitude(getLongitude(coordinates));
                place.setLatitude(getLatitude(coordinates));
                System.out.println(place.getCity() + " " + place.getLongitude().toString() + " " + place.getLatitude().toString());
            } else if (responseCode == 404) {
                weatherServiceResult.setWeatherServiceRequestStatus(WeatherServiceRequestStatus.FAILED_BY_DATA);
            } else {
                weatherServiceResult.setWeatherServiceRequestStatus(WeatherServiceRequestStatus.FAILED_BY_UNEXPECTED_ERROR);
            }
        } else {
            weatherServiceResult.setWeatherServiceRequestStatus(WeatherServiceRequestStatus.FAILED_BY_UNEXPECTED_ERROR);
        }

        return weatherServiceResult;
    }

    protected WeatherServiceResult getWeatherDataFromResponse(WeatherServiceResult weatherServiceResult,
                                                              CloseableHttpResponse response) throws IOException {
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            String data = EntityUtils.toString(entity);
            JSONObject jsonObject = new JSONObject(data);
            JSONArray days = jsonObject.getJSONArray("daily");

            List<WeatherCondition> weatherConditions = new ArrayList<>();
            for (int i = 0; i < days.length(); i++) {
                weatherConditions.add(new WeatherCondition(days.getJSONObject(i)));
            }

            WeatherForecast weatherForecast = weatherServiceResult.getWeatherForecast();
            weatherForecast.setWeatherConditions(weatherConditions);
            weatherForecast.printWeatherConditions();
        } else {
            weatherServiceResult.setWeatherServiceRequestStatus(WeatherServiceRequestStatus.FAILED_BY_UNEXPECTED_ERROR);
        }

        return weatherServiceResult;
    }

    protected Double getLongitude(JSONObject coordinates) {
        return coordinates.getDouble("lon");
    }

    protected Double getLatitude(JSONObject coordinates) {
        return coordinates.getDouble("lat");
    }

    protected Integer getResponseCode(JSONObject json) {

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
