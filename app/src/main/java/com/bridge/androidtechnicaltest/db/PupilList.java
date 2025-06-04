package com.bridge.androidtechnicaltest.db;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PupilList {

    @SerializedName("items")
    List<Pupil> pupilList;

    public PupilList(List<Pupil> pupilList) {
        this.pupilList = pupilList;
    }


    public List<Pupil> getPupilList() {
        return pupilList;
    }

    public void setPupilList(List<Pupil> pupilList) {
        this.pupilList = pupilList;
    }

    @Override
    public String toString() {
        return "PupilList{" +
                "pupilList=" + pupilList +
                '}';
    }
}
