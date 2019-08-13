package com.htwk.jseiffer.terror;

import java.time.LocalDate;

public class Terror {
    private String id;
    private LocalDate date;
    private String country;
    private String city;
    private int fatalities;
    private int injured;

    public Terror(String id, LocalDate date, String country, String city, int fatalities, int injured) {
        this.id = id;
        this.date = date;
        this.country = country;
        this.city = city;
        this.fatalities = fatalities;
        this.injured = injured;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getFatalities() {
        return fatalities;
    }

    public void setFatalities(int fatalities) {
        this.fatalities = fatalities;
    }

    public int getInjured() {
        return injured;
    }

    public void setInjured(int injured) {
        this.injured = injured;
    }

    @Override
    public String toString() {
        return  "id=" + id +
                ", date=" + date +
                ", country='" + country + '\'' +
                ", city='" + city + '\'' +
                ", fatalities=" + fatalities +
                ", injured=" + injured;
    }
}
