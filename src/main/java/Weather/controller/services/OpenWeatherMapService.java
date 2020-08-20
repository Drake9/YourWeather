package Weather.controller.services;

import Weather.controller.WeatherServiceResult;
import Weather.model.WeatherCondition;
import Weather.model.WeatherForecast;
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

public class OpenWeatherMapService extends Service<WeatherServiceResult> {

    private static final String API_KEY = "9b707f5fe12657b09e26eb6478eebf70";
    private WeatherForecast weatherForecast;
    private Double latitude;
    private Double longitude;
    private Integer responseCode;

    public OpenWeatherMapService(WeatherForecast weatherForecast) {
        this.weatherForecast = weatherForecast;
    }

    public WeatherServiceResult getData() throws Exception {
        String address1 =
                "http://api.openweathermap.org/data/2.5/weather?q="+weatherForecast.getCity()+","+weatherForecast.getCountry()+
                        "&appid="+API_KEY;
        System.out.println("Starting request: " + address1);

        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpget = new HttpGet(address1);
        CloseableHttpResponse response = httpclient.execute(httpget);

        //get coordinates for second request
        try {
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                String data = EntityUtils.toString(entity);

                JSONObject json = new JSONObject(data);
                getResponseCode(json);

                if(responseCode == 404){
                    return WeatherServiceResult.FAILED_BY_DATA;
                } else if (responseCode == 200){
                    getCoordinates(json);
                    System.out.println(weatherForecast.getCity() + " " + longitude.toString() + " " + latitude.toString());
                } else {
                    return WeatherServiceResult.FAILED_BY_UNEXPECTED_ERROR;
                }
            }
        } catch (Exception e){
            e.printStackTrace();
            return WeatherServiceResult.FAILED_BY_UNEXPECTED_ERROR;
        }  finally {
            response.close();
        }

        String address2 =
                "https://api.openweathermap.org/data/2.5/onecall?lat="+latitude.toString()+"&lon="+longitude.toString()+
                "&exclude=current,minutely,hourly&appid=9b707f5fe12657b09e26eb6478eebf70";
        System.out.println("Starting request: " + address2);

        httpget = new HttpGet(address2);
        response = httpclient.execute(httpget);

        //second request being made
        try {
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                String data = EntityUtils.toString(entity);
                JSONObject jsonObject = new JSONObject(data);
                JSONArray days = jsonObject.getJSONArray("daily");

                ArrayList<WeatherCondition> weatherConditions = new ArrayList<WeatherCondition>();
                for(int i=0; i<days.length(); i++){
                    weatherConditions.add(new WeatherCondition(days.getJSONObject(i)));
                }

                weatherForecast.setWeatherConditions(weatherConditions);
                weatherForecast.printWeatherConditions();
            }
        } catch (Exception e){
            e.printStackTrace();
            return WeatherServiceResult.FAILED_BY_UNEXPECTED_ERROR;
        }  finally {
            response.close();
        }

        return WeatherServiceResult.SUCCESS;
    }

    @Override
    protected Task<WeatherServiceResult> createTask() {
        return new Task<WeatherServiceResult>() {
            @Override
            protected WeatherServiceResult call() throws Exception {
                return getData();
            }
        };
    }

    private void getCoordinates(JSONObject json){

        JSONObject coordinates= json.getJSONObject("coord");
        this.longitude = (Double) coordinates.get("lon");
        this.latitude = (Double) coordinates.get("lat");
        this.weatherForecast.setCity( (String) json.get("name"));
    }

    private void getResponseCode(JSONObject json){

        Object object = json.get("cod");

        if(object instanceof String){
            responseCode = Integer.parseInt((String)object);
        }else if (object instanceof Integer){
            responseCode = (Integer) object;
        } else {
            responseCode = 909;
        }
    }
}
