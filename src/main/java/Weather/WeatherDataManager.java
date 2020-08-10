package Weather;

import Weather.model.WeatherCondition;
import Weather.view.IconResolver;

import java.util.ArrayList;

public class WeatherDataManager {

    private IconResolver iconResolver;
    private ArrayList<WeatherCondition> weatherConditions;

    public WeatherDataManager() {

        iconResolver = new IconResolver();
    }

    public void setWeatherConditions(ArrayList<WeatherCondition> weatherConditions) {
        this.weatherConditions = weatherConditions;
    }

    public void printWeatherConditions(){

        for(int i = 0; i < weatherConditions.size(); i++) {
            System.out.println("<<Day "+i+" "+weatherConditions.get(i).getDateAsString()+" >> temp: "+weatherConditions.get(i).getTemperature()+
                    "\u00B0"+"C weather: "+weatherConditions.get(i).getWeatherMain()+" "+weatherConditions.get(i).getWeatherDescription());

        }
    }

    public String getWeatherForDay(int dayNumber){
        return weatherConditions.get(dayNumber).getWeatherMain();
    }

    public WeatherCondition getWeatherCondition(int number){
        return weatherConditions.get(number);
    }
}
