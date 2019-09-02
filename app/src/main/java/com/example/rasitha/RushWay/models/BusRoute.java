package com.example.rasitha.RushWay.models;

public class BusRoute{

    private String destination1;
    private String destination2;
    private String no;


    public BusRoute() {

    }

    public BusRoute(String destination1, String destination2, String no) {
        this.destination1 = destination1;
        this.destination2 = destination2;
        this.no = no;
    }

    public String getDestination1() {
        return destination1;
    }

    public void setDestination1(String destination1) {
        this.destination1 = destination1;
    }

    public String getDestination2() {
        return destination2;
    }

    public void setDestination2(String destination2) {
        this.destination2 = destination2;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

}