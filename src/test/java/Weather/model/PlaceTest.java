package Weather.model;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PlaceTest {

    private Place place = new Place("Poland", "Cracow");

    @ParameterizedTest
    @ValueSource(doubles = {-181.00, 181.00})
    void shouldThrowExceptionWhenLongitudeIsIncorrect(Double doubles) {

        //then
        assertThrows(IllegalArgumentException.class, () -> place.setLongitude(doubles));
    }

    @ParameterizedTest
    @ValueSource(doubles = {-91.00, 91.00})
    void shouldThrowExceptionWhenLatitudeIsIncorrect(Double doubles) {

        //then
        assertThrows(IllegalArgumentException.class, () -> place.setLatitude(doubles));
    }

    @ParameterizedTest
    @ValueSource(doubles = {-180.00, 180.00, 0.00, 10})
    void shouldNotThrowExceptionWhenLongitudeIsCorrect(Double doubles) {

        //when
        place.setLongitude(doubles);

        //then
        assertEquals(place.getLongitude(), doubles);
    }

    @ParameterizedTest
    @ValueSource(doubles = {-90.00, 90.00, 0.00, 10})
    void shouldNotThrowExceptionWhenLatitudeIsCorrect(Double doubles) {

        //when
        place.setLatitude(doubles);

        //then
        assertEquals(place.getLatitude(), doubles);
    }
}
