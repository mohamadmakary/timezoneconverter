package com.example.timezoneconverter;
public class CityTime {
    private String cityName;
    private String currentTime;
    private String hoursDifference;


    public CityTime(String cityName, String currentTime, String hoursDifference) {
        this.cityName = cityName;
        this.currentTime = currentTime;
        this.hoursDifference = hoursDifference;
    }


    public String getCityName() {
        return cityName;
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public String getHoursDifference() {
        return hoursDifference;
    }
}
