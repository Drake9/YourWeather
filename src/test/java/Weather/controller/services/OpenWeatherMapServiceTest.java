package Weather.controller.services;

import Weather.model.Place;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class OpenWeatherMapServiceTest {

    @ParameterizedTest
    @ValueSource(strings = {"0", "\"0\"", "200", "404", "null"})
    void getResponseCodeShouldAlwaysReturnInteger(String cod) {

        //given
        Place place = new Place("Poland", "Cracow");
        OpenWeatherMapService openWeatherMapService = new OpenWeatherMapService(place);

        JSONObject jsonObject = new JSONObject("{\"cod\":" + cod + "}");

        //when
        Object responseCode = openWeatherMapService.getResponseCode(jsonObject);

        //then
        assertTrue(responseCode instanceof Integer);
    }

    @Test
    void getLatitudeShouldReturnCorrectValue() {

        //given
        Place place = new Place("Poland", "Cracow");
        OpenWeatherMapService openWeatherMapService = new OpenWeatherMapService(place);
        JSONObject jsonObject = new JSONObject("{\"lat\":50.08, \"lon\":19.92}");

        //when
        Double latitude = openWeatherMapService.getLatitude(jsonObject);

        //then
        assertThat(latitude, equalTo(50.08));
    }

    @Test
    void getLongitudeShouldReturnCorrectValue() {

        //given
        Place place = new Place("Poland", "Cracow");
        OpenWeatherMapService openWeatherMapService = new OpenWeatherMapService(place);
        JSONObject jsonObject = new JSONObject("{\"lat\":50.08, \"lon\":19.92}");

        //when
        Double longitude = openWeatherMapService.getLongitude(jsonObject);

        //then
        assertThat(longitude, equalTo(19.92));
    }
}
