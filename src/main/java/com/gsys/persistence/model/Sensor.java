package com.gsys.persistence.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class Sensor {

    @Id
    private String id;
    private String city;
    private String country;
    private Boolean active;
    private Date dateCreated;
    private Date dateUpdate;


    public Sensor(){
    }

    public Sensor(String id, String city, String country, Boolean active, Date dateCreated, Date dateUpdate) {
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

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Date getDateUpdate() {
        return dateUpdate;
    }

    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }
}
