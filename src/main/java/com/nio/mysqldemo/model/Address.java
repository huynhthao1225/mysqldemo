package com.nio.mysqldemo.model;

import java.sql.Timestamp;

public class Address {
    // Address is data structure of MySql Address table

        private int ID;
        private String Address;
        private String Address2;
        private String District;
        private String CityID;
        private String PostalCode;
        private String Phone;
        private byte[] Location;
        private Timestamp LastUpdate;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getAddress2() {
        return Address2;
    }

    public void setAddress2(String address2) {
        Address2 = address2;
    }

    public String getDistrict() {
        return District;
    }

    public void setDistrict(String district) {
        District = district;
    }

    public String getCityID() {
        return CityID;
    }

    public void setCityID(String cityID) {
        CityID = cityID;
    }

    public String getPostalCode() {
        return PostalCode;
    }

    public void setPostalCode(String postalCode) {
        PostalCode = postalCode;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public byte[] getLocation() {
        return Location;
    }

    public void setLocation(byte[] location) {
        Location = location;
    }

    public Timestamp getLastUpdate() {
        return LastUpdate;
    }

    public void setLastUpdate(Timestamp lastUpdate) {
        LastUpdate = lastUpdate;
    }
}
