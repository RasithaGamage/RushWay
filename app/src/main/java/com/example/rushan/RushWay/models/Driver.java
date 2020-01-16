package com.example.rushan.RushWay.models;

import java.io.Serializable;

public class Driver  extends User  implements Serializable {

    Vehicle bus;
    String licenseNo;
    final public String userType = "driver";

    public Driver() {

    }
    public Driver(String fName, String lName, String eMail, String phone, String nic) {
        super(fName, lName, eMail, phone, nic);
    }

    public Vehicle getBus() {
        return bus;
    }

    public void setBus(Vehicle bus) {
        this.bus = bus;
    }

    public String getLicenseNo() {
        return licenseNo;
    }

    public void setLicenseNo(String licenseNo) {
        this.licenseNo = licenseNo;
    }

}
