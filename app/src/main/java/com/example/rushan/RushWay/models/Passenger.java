package com.example.rushan.RushWay.models;

import java.io.Serializable;

public class Passenger extends User   implements Serializable {

    final public String userType = "passenger";
    public Passenger(){

    }
    public Passenger(String fName, String lName, String eMail, String phone, String nic) {
        super(fName, lName, eMail, phone, nic);
    }
}
