package Weather.controller.services;

import Weather.controller.WeatherServiceResult;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class OpenWeatherMapService extends Service<WeatherServiceResult> {

    private static final String API_KEY = "9b707f5fe12657b09e26eb6478eebf70";
    private String Country;
    private String City;

    public OpenWeatherMapService(String country, String city) {
        Country = country;
        City = city;
    }

    public WeatherServiceResult getWeather() throws Exception {
        String uri = "http://api.openweathermap.org/data/2.5/weather?q="+City+","+Country+"&appid="+API_KEY;

        System.out.println("Starting request: " + uri);

        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpget = new HttpGet(uri);
        CloseableHttpResponse response = httpclient.execute(httpget);

        try {
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                long len = entity.getContentLength();
                if (len != -1 && len < 2048) {
                    System.out.println(EntityUtils.toString(entity));
                } else {
                    // Stream content out
                }
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
                return getWeather();
            }
        };
    }
}
