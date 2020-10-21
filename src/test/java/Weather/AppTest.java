package Weather;

import Weather.model.Place;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

class AppTest {

    @Spy
    private App appSpy;
    private Stage stage = mock(Stage.class);

    @Test
    @ExtendWith(MockitoExtension.class)
    void startingShouldCallLoadFromPersistence() throws Exception {

        //given
        doNothing().when(appSpy).showMainWindow(anyBoolean());

        //when
        appSpy.start(stage);

        //then
        then(appSpy).should().loadFromPersistence();
    }

    @Test
    @ExtendWith(MockitoExtension.class)
    void loadingPlacesFromPersistenceShouldSetThemAndShowMainWindow() throws Exception {

        //given
        ArrayList<Place> places = new ArrayList<>();
        Place place1 = new Place("Poland", "Cracow");
        Place place2 = new Place("England", "London");
        places.add(place1);
        places.add(place2);

        when(appSpy.loadFromPersistence()).thenReturn(places);
        doNothing().when(appSpy).showMainWindow(anyBoolean());
        doNothing().when(appSpy).setPlaces(any());

        ArgumentCaptor<List<Place>> captor = ArgumentCaptor.forClass(List.class);

        //when
        appSpy.start(stage);

        //then
        then(appSpy).should().showMainWindow(true);
        verify(appSpy).setPlaces(captor.capture());
        assertEquals(places, captor.getValue());
    }

    @Test
    @ExtendWith(MockitoExtension.class)
    void loadingNoPlacesFromPersistenceShouldSetThemEmptyAndShowMainWindow() throws Exception {

        //given
        ArrayList<Place> emptyArray = new ArrayList<>();
        ArrayList<Place> emptyStrings = new ArrayList<>();
        Place place1 = new Place("", "");
        Place place2 = new Place("", "");
        emptyStrings.add(place1);
        emptyStrings.add(place2);

        when(appSpy.loadFromPersistence()).thenReturn(emptyArray);
        doNothing().when(appSpy).showMainWindow(anyBoolean());
        doNothing().when(appSpy).setPlaces(any());

        ArgumentCaptor<List<Place>> captor = ArgumentCaptor.forClass(List.class);

        //when
        appSpy.start(stage);

        //then
        then(appSpy).should().showMainWindow(false);
        verify(appSpy).setPlaces(captor.capture());
        assertEquals(emptyStrings, captor.getValue());
    }

    @Test
    @ExtendWith(MockitoExtension.class)
    void shouldSavePlacesToPersistenceWhenStopped() throws Exception {

        //given
        doNothing().when(appSpy).showMainWindow(anyBoolean());

        //when
        appSpy.start(stage);
        appSpy.stop();

        //then
        then(appSpy).should().saveToPersistence(any());
    }
}
