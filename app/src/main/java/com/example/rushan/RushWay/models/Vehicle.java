package com.example.rushan.RushWay.models;

public class Vehicle {

    private String busId;
    private BusRoute route;
    private String availableSeats;
    private String currentDriver;
//    private String make;
//    private String model;
//    private String engineNumber;
//    private String insurance;

    public String getAavailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(String capacity) {
        this.availableSeats = capacity;
    }

    public String getCurrentDriver() {
        return currentDriver;
    }

    public void setCurrentDriver(String currentDriver) {
        this.currentDriver = currentDriver;
    }

    public BusRoute getRoute() {
        return route;
    }

    public void setRoute(BusRoute route) {
        this.route = route;
    }

    public String getBusId() {
        return busId;
    }

    public void setBusId(String busId) {
        this.busId = busId;
    }

//    public String getMake() {
//        return make;
//    }
//
//    public void setMake(String make) {
//        this.make = make;
//    }
//
//    public String getModel() {
//        return model;
//    }
//
//    public void setModel(String model) {
//        this.model = model;
//    }
//
//    public String getEngineNumber() {
//        return engineNumber;
//    }
//
//    public void setEngineNumber(String engineNumber) {
//        this.engineNumber = engineNumber;
//    }
//
//    public String getInsurance() {
//        return insurance;
//    }
//
//    public void setInsurance(String insurance) {
//        this.insurance = insurance;
//    }

    public Vehicle() {
    }

    public Vehicle(String busID, BusRoute route, String availableSeats, String currentDriver) {
        this.busId = busID;
        this.route = route;
        this.availableSeats = availableSeats;
        this.currentDriver = currentDriver;
    }
}


