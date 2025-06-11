package com.bridge.androidtechnicaltest.db;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "Pupils")
public class Pupil {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "pupil_id")
    private int pupilId;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "country")
    private String country;

    @ColumnInfo(name = "image")
    private String image;

    @ColumnInfo(name = "latitude")
    private Double latitude;

    @ColumnInfo(name = "longitude")
    private Double longitude;

    @ColumnInfo(name = "is_synced")
    public boolean isSynced;

    @ColumnInfo(name = "created_at")
    private long createdAt;


    public Pupil(int pupilId, String name, String country, String image, Double latitude, Double longitude) {
        this.pupilId = pupilId;
        this.name = name;
        this.country = country;
        this.image = image;
        this.latitude = latitude;
        this.longitude = longitude;
        this.isSynced = false;
        this.createdAt = System.currentTimeMillis();
    }

    // for new pupils (without ID)
    @Ignore
    public Pupil(String name, String country, String image, Double latitude, Double longitude) {
        this.pupilId = 0;
        this.name = name;
        this.country = country;
        this.image = image;
        this.latitude = latitude;
        this.longitude = longitude;
        this.isSynced = false;
        this.createdAt = System.currentTimeMillis();
    }

    public int getPupilId() {
        return pupilId;
    }

    public void setPupilId(int pupilId) {
        this.pupilId = pupilId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String value) {
        this.country = value;
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

    public boolean isSynced() {
        return isSynced;
    }

    public void setSynced(boolean synced) {
        isSynced = synced;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Pupil{" +
                "pupilId=" + pupilId +
                ", name='" + name + '\'' +
                ", value='" + country + '\'' +
                ", image='" + image + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}