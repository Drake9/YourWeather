package Weather.model;

import java.util.Objects;

public class Place {

    private String country;
    private String city;
    private Double longitude;
    private Double latitude;

    public Place() {
    }

    public Place(String country, String city) {
        this.country = country;
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Place place = (Place) o;
        return Objects.equals(country, place.country) &&
                Objects.equals(city, place.city);
    }

    @Override
    public int hashCode() {
        return Objects.hash(country, city);
    }
}
