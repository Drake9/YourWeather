package Weather.view;

public class IconResolver {

    public String getIconForWeather(String weatherName) {
        String lowerCaseWeatherName = weatherName.toLowerCase();

        if(lowerCaseWeatherName.contains("clear")){
            return "src/main/resources/icons/sun.png";
        }else if(lowerCaseWeatherName.contains("clouds")){
            return "src/main/resources/icons/cloudy.png";
        }else if(lowerCaseWeatherName.contains("thunderstorm")){
            return "src/main/resources/icons/storm.png";
        }else if(lowerCaseWeatherName.contains("drizzle")){
            return "src/main/resources/icons/rainy.png";
        }else if(lowerCaseWeatherName.contains("rain")){
            return "src/main/resources/icons/rain.png";
        }else if(lowerCaseWeatherName.contains("snow")){
            return "src/main/resources/icons/snow.png";
        }else{
            return "src/main/resources/icons/windy.png";
        }
    }
}