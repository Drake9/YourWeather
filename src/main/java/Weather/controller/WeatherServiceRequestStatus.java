package Weather.controller;

public enum WeatherServiceRequestStatus {
    PENDING,
    SUCCESS,
    FAILED_BY_DATA,
    FAILED_BY_NETWORK,
    FAILED_BY_UNEXPECTED_ERROR
}
