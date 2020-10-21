package Weather.view;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class IconResolverTest {

    IconResolver iconResolver = new IconResolver();

    @Test
    void getIconForWeatherShouldNotReturnNull() {

        //given
        String iconLocation = iconResolver.getIconForWeather("unexpected weather");

        //then
        assertTrue(iconLocation != null);
    }

    @Test
    void getIconForWeatherShouldNotReturnEmptyString() {

        //given
        String iconLocation = iconResolver.getIconForWeather("unexpected weather");

        //then
        assertTrue(iconLocation != "");
    }

    @ParameterizedTest
    @ValueSource(strings = {"clear", "clouds", "thunderstorm", "drizzle", "rain", "snow", "windy",
            "unexpected"})
    void getIconForWeatherShouldReturnCorrectPathToFile(String weather) {

        //given
        String iconLocation = iconResolver.getIconForWeather(weather);

        //when
        File file = new File(iconLocation);

        //then
        assertTrue(file.exists());
    }
}
