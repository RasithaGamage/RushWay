package com.example.rasitha.RushWay.models;

public class Vehicle {

    private String numberPlate;
    private String route;

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public String getRouteNo() {
        return routeNo;
    }

    public void setRouteNo(String routeNo) {
        this.routeNo = routeNo;
    }

    private String routeNo;

    public String getNumberPlate() {
        return numberPlate;
    }

    public void setNumberPlate(String numberPlate) {
        this.numberPlate = numberPlate;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getEngineNumber() {
        return engineNumber;
    }

    public void setEngineNumber(String engineNumber) {
        this.engineNumber = engineNumber;
    }

    public String getInsurance() {
        return insurance;
    }

    public void setInsurance(String insurance) {
        this.insurance = insurance;
    }

    private String make;
    private String model;
    private String engineNumber;
    private String insurance;

    public Vehicle() {
    }

    public Vehicle(String numberPlate, String make, String model, String engineNumber, String insurance) {
        this.numberPlate = numberPlate;
        this.make = make;
        this.model = model;
        this.engineNumber = engineNumber;
        this.insurance = insurance;
    }


}