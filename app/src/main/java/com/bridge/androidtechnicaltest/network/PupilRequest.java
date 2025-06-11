package com.bridge.androidtechnicaltest.network;

import com.bridge.androidtechnicaltest.db.Pupil;

public class PupilRequest {
    private String country;
    private String name;
    private String image;
    private Double latitude;
    private Double longitude;

    public PupilRequest() {}

    public PupilRequest(Pupil pupil) {
        this.country = pupil.getCountry();
        this.name = pupil.getName();
        this.image = pupil.getImage();
        this.latitude = pupil.getLatitude();
        this.longitude = pupil.getLongitude();
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}