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
        if (longitude >= -180 && longitude <= 180) {
            this.longitude = longitude;
        } else {
            throw new IllegalArgumentException("Longitude must be between -180 and 180, was: " + longitude);
        }
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        if (latitude >= -90 && latitude <= 90) {
            this.latitude = latitude;
        } else {
            throw new IllegalArgumentException("Latitude must be between -90 and 190, was: " + latitude);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Place place = (Place) o;
        return Objects.equals(country, place.country) &&
                Objects.equals(city, place.city) &&
                Objects.equals(longitude, place.longitude) &&
                Objects.equals(latitude, place.latitude);
    }

    @Override
    public int hashCode() {
        return Objects.hash(country, city, longitude, latitude);
    }
}
