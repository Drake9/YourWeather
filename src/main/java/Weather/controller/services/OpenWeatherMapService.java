package Weather.controller.services;

import Weather.WeatherDataManager;
import Weather.controller.WeatherServiceResult;
import Weather.model.WeatherCondition;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.Initializable;
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
    private WeatherDataManager weatherDataManager;
    private String country;
    private String city;
    private Double latitude;
    private Double longitude;

    public OpenWeatherMapService(WeatherDataManager weatherDataManager) {
        this.weatherDataManager = weatherDataManager;
        this.country = "Poland";
        this.city = "Rabka-Zdroj";
    }

    public WeatherServiceResult getData() throws Exception {
        String address1 = "http://api.openweathermap.org/data/2.5/weather?q="+city+","+country+"&appid="+API_KEY;
        System.out.println("Starting request: " + address1);

        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpget = new HttpGet(address1);
        CloseableHttpResponse response = httpclient.execute(httpget);

        //get coordinates for second request
        try {
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                String data = EntityUtils.toString(entity);
                getCoordinates(data);
                System.out.println(city+" "+longitude.toString()+" "+latitude.toString());
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

                weatherDataManager.setWeatherConditions(weatherConditions);
                weatherDataManager.printWeatherConditions();
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

    private void getCoordinates(String text){
        JSONObject json = new JSONObject(text);
        JSONObject coordinates= json.getJSONObject("coord");
        this.longitude = (Double) coordinates.get("lon");
        this.latitude = (Double) coordinates.get("lat");
        this.city = (String) json.get("name");
    }
}
