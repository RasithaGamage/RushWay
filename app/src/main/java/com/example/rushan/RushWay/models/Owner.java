package com.example.rushan.RushWay.models;

import java.io.Serializable;

public class Owner extends User  implements Serializable {

    final public String userType = "owner";
    public Owner(){

    }
    public Owner(String fName, String lName, String eMail, String phone, String nic) {
        super(fName, lName, eMail, phone, nic);
    }
}
