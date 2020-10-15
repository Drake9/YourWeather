package Weather.model;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class WeatherConditionTest {

    @Test
    void weatherConditionShouldHaveCorrectParametersAfterCreation() {

        //given
        JSONObject jsonObject = new JSONObject("{\"temp\":{\"day\":7.34,\"night\":5.3},\"dt\":1602669600," +
                "\"weather\":[{\"main\":rain,\"description\":moderate rain}]}");

        //when
        WeatherCondition condition = new WeatherCondition(jsonObject);

        //then
        assertThat(condition.getTemperature(), equalTo(7.3));
        assertThat(condition.getWeatherMain(), equalTo("rain"));
        assertThat(condition.getWeatherDescription(), equalTo("moderate rain"));
        assertThat(condition.getDateAsString(), equalTo("Wed 14.10"));
    }

    @Test
    void noDateKeywordShouldThrowIllegalArgumentException() {

        //given
        JSONObject jsonObject = new JSONObject("{\"temp\":{\"day\":7.34,\"night\":5.3}," +
                "\"weather\":[{\"main\":rain,\"description\":moderate rain}]}");

        //then
        assertThrows(IllegalArgumentException.class, () -> {
            WeatherCondition condition = new WeatherCondition(jsonObject);
        });
    }

    @Test
    void noTemperatureKeywordShouldThrowIllegalArgumentException() {

        //given
        JSONObject jsonObject = new JSONObject("{\"dt\":1602669600," +
                "\"weather\":[{\"main\":rain,\"description\":moderate rain}]}");

        //then
        assertThrows(IllegalArgumentException.class, () -> {
            WeatherCondition condition = new WeatherCondition(jsonObject);
        });
    }

    @Test
    void noDayKeywordShouldThrowIllegalArgumentException() {

        JSONObject jsonObject = new JSONObject("{\"temp\":{\"night\":5.3},\"dt\":1602669600," +
                "\"weather\":[{\"main\":rain,\"description\":moderate rain}]}");

        //then
        assertThrows(IllegalArgumentException.class, () -> {
            WeatherCondition condition = new WeatherCondition(jsonObject);
        });
    }

    @Test
    void noWeatherKeywordShouldThrowIllegalArgumentException() {

        //given
        JSONObject jsonObject = new JSONObject("{\"temp\":{\"day\":7.34,\"night\":5.3},\"dt\":1602669600}");

        //then
        assertThrows(IllegalArgumentException.class, () -> {
            WeatherCondition condition = new WeatherCondition(jsonObject);
        });
    }

    @Test
    void emptyWeatherArrayShouldThrowIllegalArgumentException() {

        //given
        JSONObject jsonObject = new JSONObject("{\"temp\":{\"day\":7.34,\"night\":5.3},\"dt\":1602669600," +
                "\"weather\":[]}");

        //then
        assertThrows(IllegalArgumentException.class, () -> {
            WeatherCondition condition = new WeatherCondition(jsonObject);
        });
    }

    @Test
    void noMainKeywordInJsonShouldThrowIllegalArgumentException() {

        JSONObject jsonObject = new JSONObject("{\"temp\":{\"day\":7.34,\"night\":5.3},\"dt\":1602669600," +
                "\"weather\":[{\"description\":moderate rain}]}");

        //then
        assertThrows(IllegalArgumentException.class, () -> {
            WeatherCondition condition = new WeatherCondition(jsonObject);
        });
    }

    @Test
    void noDescriptionKeywordInJsonShouldThrowIllegalArgumentException() {

        //given
        JSONObject jsonObject = new JSONObject("{\"temp\":{\"day\":7.34,\"night\":5.3},\"dt\":1602669600," +
                "\"weather\":[{\"main\":rain}]}");

        //then
        assertThrows(IllegalArgumentException.class, () -> {
            WeatherCondition condition = new WeatherCondition(jsonObject);
        });
    }
}
