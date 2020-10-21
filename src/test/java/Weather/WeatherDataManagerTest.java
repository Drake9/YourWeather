package Weather;

import Weather.model.Place;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class WeatherDataManagerTest {

    @Test
    void weatherForecastsShouldHaveCorrectPlacesAfterSetPlaces() {

        //given
        WeatherDataManager weatherDataManager = new WeatherDataManager();
        Place place1 = new Place("Poland", "Kraków");
        Place place2 = new Place("Egypt", "Cairo");
        ArrayList<Place> places = new ArrayList<>();
        places.add(place1);
        places.add(place2);

        //when
        weatherDataManager.setPlaces(places);

        //then
        assertThat(weatherDataManager.getCity(0), equalTo(place1.getCity()));
        assertThat(weatherDataManager.getCountry(0), equalTo(place1.getCountry()));
        assertThat(weatherDataManager.getCity(1), equalTo(place2.getCity()));
        assertThat(weatherDataManager.getCountry(1), equalTo(place2.getCountry()));
    }
}
