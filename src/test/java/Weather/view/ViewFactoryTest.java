package Weather.view;

import Weather.WeatherDataManager;
import Weather.controller.MainWindowController;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;

public class ViewFactoryTest {

    WeatherDataManager weatherDataManager = mock(WeatherDataManager.class);

    ViewFactory viewFactorySpy = Mockito.spy(new ViewFactory(weatherDataManager));

    @Test
    void showMainWindowWithPlacesSetShouldInitializeStageAndLoadWeatherData() {

        //given
        MainWindowController controller = mock(MainWindowController.class);

        when(viewFactorySpy.createMainWindowController()).thenReturn(controller);
        doNothing().when(viewFactorySpy).initializeStage(controller);

        //when
        viewFactorySpy.showMainWindow(true);

        //then
        verify(viewFactorySpy).initializeStage(controller);
        verify(controller).loadWeatherData();
    }

    @Test
    void showMainWindowWithPlacesSetShouldInitializeStageAndNotLoadWeatherData() {

        //given
        MainWindowController controller = mock(MainWindowController.class);

        when(viewFactorySpy.createMainWindowController()).thenReturn(controller);
        doNothing().when(viewFactorySpy).initializeStage(controller);

        //when
        viewFactorySpy.showMainWindow(false);

        //then
        verify(viewFactorySpy).initializeStage(controller);
        verify(controller, never()).loadWeatherData();
    }
}
