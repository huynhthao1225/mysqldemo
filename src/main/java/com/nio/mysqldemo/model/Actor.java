package com.nio.mysqldemo.model;

import java.sql.Timestamp;

public class Actor {
    private int ID;
    private String FirstName;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public Timestamp getLastUpdate() {
        return LastUpdate;
    }

    public void setLastUpdate(Timestamp lastUpdate) {
        LastUpdate = lastUpdate;
    }

    private String LastName;
    private Timestamp LastUpdate;
}




