package Weather.controller.services;

import Weather.controller.WeatherServiceRequestStatus;
import Weather.model.Place;
import Weather.model.WeatherCondition;
import Weather.model.WeatherForecast;
import Weather.model.WeatherServiceResult;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import org.mockito.Spy;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

public class OpenWeatherMapServiceTest {

    private Place place = new Place("Poland", "Cracow");
    private  OpenWeatherMapService openWeatherMapService = new OpenWeatherMapService(place);
    @Spy
    private OpenWeatherMapService openWeatherMapServiceSpy = Mockito.spy(OpenWeatherMapService.class);

    @ParameterizedTest
    @ValueSource(strings = {"0", "\"0\"", "200", "404", "null"})
    void getResponseCodeShouldAlwaysReturnInteger(String cod) {

        //given
        JSONObject jsonObject = new JSONObject("{\"cod\":" + cod + "}");

        //when
        Object responseCode = openWeatherMapService.getResponseCode(jsonObject);

        //then
        assertTrue(responseCode instanceof Integer);
    }

    @Test
    void responseCode404ShouldReturnFailedByData() {

        //given
        WeatherForecast weatherForecast = new WeatherForecast(place);
        WeatherServiceResult weatherServiceResult = new WeatherServiceResult(weatherForecast,
                WeatherServiceRequestStatus.PENDING);
        JSONObject jsonObject = new JSONObject();

        when(openWeatherMapServiceSpy.getResponseCode(jsonObject)).thenReturn(404);

        //when
        weatherServiceResult = openWeatherMapServiceSpy.getPlaceCoordinatesFromJsonResponse(weatherServiceResult,
                jsonObject);

        //then
        assertThat(weatherServiceResult.getWeatherServiceRequestStatus(),
                equalTo(WeatherServiceRequestStatus.FAILED_BY_DATA));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 100, 400})
    void unknownResponseCodeShouldReturnFailedByUnexpectedError(Integer ints) {

        //given
        WeatherForecast weatherForecast = new WeatherForecast(place);
        WeatherServiceResult weatherServiceResult = new WeatherServiceResult(weatherForecast,
                WeatherServiceRequestStatus.PENDING);
        JSONObject jsonObject = new JSONObject();

        when(openWeatherMapServiceSpy.getResponseCode(jsonObject)).thenReturn(ints);

        //when
        weatherServiceResult = openWeatherMapServiceSpy.getPlaceCoordinatesFromJsonResponse(weatherServiceResult,
                jsonObject);

        //then
        assertThat(weatherServiceResult.getWeatherServiceRequestStatus(),
                equalTo(WeatherServiceRequestStatus.FAILED_BY_UNEXPECTED_ERROR));
    }

    @Test
    void responseCode200ShouldSetCoordinatesAndNotReturnError() {

        //given
        WeatherForecast weatherForecast = new WeatherForecast(place);
        WeatherServiceResult weatherServiceResult = new WeatherServiceResult(weatherForecast,
                WeatherServiceRequestStatus.PENDING);
        JSONObject jsonObject = new JSONObject("{\"coord\":{\"lon\":1.00,\"lat\":2.00},\"cod\":200}");

        //when
        weatherServiceResult = openWeatherMapService.getPlaceCoordinatesFromJsonResponse(weatherServiceResult,
                jsonObject);

        //then
        assertEquals(weatherServiceResult.getWeatherServiceRequestStatus(), WeatherServiceRequestStatus.PENDING);
        assertEquals(weatherServiceResult.getWeatherForecast().getPlace().getLongitude(), 1.00);
        assertEquals(weatherServiceResult.getWeatherForecast().getPlace().getLatitude(), 2.00);
    }

    @Test
    void getWeatherDataFromJsonResponseShouldReturnResultWithForecast() {

        //given
        WeatherForecast weatherForecast = new WeatherForecast(place);
        WeatherServiceResult weatherServiceResult = new WeatherServiceResult(weatherForecast,
                WeatherServiceRequestStatus.PENDING);

        String day1 = "{\"temp\":{\"day\":7.34,\"night\":5.3},\"dt\":1602669600,\"weather\":[{\"main\":rain," +
                "\"description\":moderate rain}]}";
        String day2 = "{\"temp\":{\"day\":8.34,\"night\":5.3},\"dt\":1602669600,\"weather\":[{\"main\":rain," +
                "\"description\":heavy rain}]}";
        String day3 = "{\"temp\":{\"day\":9.34,\"night\":5.3},\"dt\":1602669600,\"weather\":[{\"main\":rain," +
                "\"description\":light rain}]}";

        String allDays = day1 + "," + day2 + "," + day3;

        JSONObject data = new JSONObject("{\"daily\":[" + allDays + "]}");

        WeatherCondition condition1 = new WeatherCondition(new JSONObject(day1));
        WeatherCondition condition2 = new WeatherCondition(new JSONObject(day2));
        WeatherCondition condition3 = new WeatherCondition(new JSONObject(day3));

        //when
        weatherServiceResult = openWeatherMapServiceSpy.getWeatherDataFromJsonResponse(weatherServiceResult, data);

        //then
        assertEquals(weatherServiceResult.getWeatherForecast().getWeatherCondition(0), condition1);
        assertEquals(weatherServiceResult.getWeatherForecast().getWeatherCondition(1), condition2);
        assertEquals(weatherServiceResult.getWeatherForecast().getWeatherCondition(2), condition3);
    }
}
