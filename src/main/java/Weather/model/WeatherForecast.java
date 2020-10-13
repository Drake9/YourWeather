package Weather.model;

import java.util.List;

public class WeatherForecast {

    private Place place;
    private List<WeatherCondition> weatherConditions;

    public WeatherForecast(Place place) {
        this.place = place;
    }

    public String getCountry() {
        return this.place.getCountry();
    }

    public String getCity() {
        return this.place.getCity();
    }

    public void setCountry(String country) {
        this.place.setCountry(country);
    }

    public void setCity(String city) {
        this.place.setCity(city);
    }

    public Place getPlace(){
        return this.place;
    }

    public void setWeatherConditions(List<WeatherCondition> weatherConditions) {
        this.weatherConditions = weatherConditions;
    }

    public WeatherCondition getWeatherCondition(int i) {
        return weatherConditions.get(i);
    }

    public void printWeatherConditions(){

        for(int i = 0; i < weatherConditions.size(); i++) {
            System.out.println("<<Day "+i+" "+weatherConditions.get(i).getDateAsString()+" >> temp: "+weatherConditions.get(i).getTemperature()+
                    "\u00B0"+"C weather: "+weatherConditions.get(i).getWeatherMain()+" "+weatherConditions.get(i).getWeatherDescription());

        }
    }
}
