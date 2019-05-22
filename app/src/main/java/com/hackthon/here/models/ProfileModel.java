package com.hackthon.here.models;

public class ProfileModel {

    private String name, address, email, mobile, location;
    private  int bonuspoints;

    public ProfileModel() {
    }

    public int getBonuspoints() {
        return bonuspoints;
    }

    public void setBonuspoints(int bonuspoints) {
        this.bonuspoints = bonuspoints;
    }

    public ProfileModel(String name, String address, String email, String mobile, String location, int bonuspoints) {
        this.name = name;
        this.address = address;
        this.email = email;
        this.mobile = mobile;
        this.location = location;
        this.bonuspoints = bonuspoints;
    }

    public ProfileModel(String name, String address, String email, String mobile, String location) {
        this.name = name;
        this.address = address;
        this.email = email;
        this.mobile = mobile;
        this.location = location;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }


    @Override
    public String toString() {
        return "ProfileModel["+getName()+","+getMobile()+","+getEmail()+","+getAddress()+"]";
    }
}
