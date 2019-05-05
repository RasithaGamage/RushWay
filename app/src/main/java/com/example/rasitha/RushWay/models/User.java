package com.example.rasitha.RushWay.models;


import java.io.Serializable;

public class User  implements Serializable {
    private String fName;
    private String lName;
    private String eMail;
    private String phone;
    private String nic;
    private String pw;
    private String uid;
    private RWLocation currentLocation;
    public User(){}
    public User(String fName, String lName, String eMail, String phone, String nic) {
        this.fName = fName;
        this.lName = lName;
        this.eMail = eMail;
        this.phone = phone;
        this.nic = nic;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getlName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public String geteMail() {
        return eMail;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNic() {
        return nic;
    }

    public void setNic(String nic) {
        this.nic = nic;
    }

    public RWLocation getCurrentLocation() {
        return currentLocation;
    }
    public void setCurrentLocation(RWLocation currentLocation) {
        this.currentLocation = currentLocation;
    }

    public String getPw() {
        return pw;
    }
    public void setPw(String pw) {
        this.pw = pw;
    }

    public String getUid() {
        return uid;
    }
    public void setUid(String uid) {
        this.uid = uid;
    }
}
