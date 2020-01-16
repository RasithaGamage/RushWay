package com.example.rushan.RushWay.models;

public class Ad {

    private String branch ;
    private String adId;
    private String company ;
    RWLocation coordinates ;
    private String lat ;
    private String lon ;
    private String image ;

    public Ad(String branch, String adId, String company, RWLocation coordinates, String lat, String lon, String image, int priority_level, int status) {
        this.branch = branch;
        this.adId = adId;
        this.company = company;
        this.coordinates = coordinates;
        this.lat = lat;
        this.lon = lon;
        this.image = image;
        this.priority_level = priority_level;
        this.status = status;
    }

    public Ad(){}

    private int priority_level ;
    private int status ;

    public String getAdId() {
        return adId;
    }

    public void setAdId(String adId) {
        this.adId = adId;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public RWLocation getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(RWLocation coordinates) {
        this.coordinates = coordinates;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getPriority_level() {
        return priority_level;
    }

    public void setPriority_level(int priority_level) {
        this.priority_level = priority_level;
    }

    public int isStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }


}
