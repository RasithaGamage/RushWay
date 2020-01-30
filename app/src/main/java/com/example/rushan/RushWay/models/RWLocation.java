package com.example.rushan.RushWay.models;

public class RWLocation {

    private Long lat;
    private Long lon;
    public RWLocation() {
    }

    public RWLocation(Long lat, Long lon) {
        this.lat = lat;
        this.lon = lon;
    }

    public Long getLat() {
        return lat;
    }

    public void setLat(Long lat) {
        this.lat = lat;
    }

    public Long getLon() {
        return lon;
    }

    public void setLon(Long lon) {
        this.lon = lon;
    }
}
