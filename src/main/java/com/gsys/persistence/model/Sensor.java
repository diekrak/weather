package com.gsys.persistence.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDate;
import java.util.Date;

@Entity
public class Sensor {

    @Id
    private String id;
    private String city;
    private String country;
    private boolean active;
    private LocalDate dateCreated;
    private LocalDate dateUpdate;


    public Sensor(){
    }

    public Sensor(String id, String city, String country, Boolean active, LocalDate dateCreated, LocalDate dateUpdate) {
        this.id = id;
        this.city = city;
        this.country = country;
        this.active = active;
        this.dateCreated = dateCreated;
        this.dateUpdate = dateUpdate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public LocalDate getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDate dateCreated) {
        this.dateCreated = dateCreated;
    }

    public LocalDate getDateUpdate() {
        return dateUpdate;
    }

    public void setDateUpdate(LocalDate dateUpdate) {
        this.dateUpdate = dateUpdate;
    }
}
