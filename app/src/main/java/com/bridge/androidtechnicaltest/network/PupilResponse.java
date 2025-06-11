package com.bridge.androidtechnicaltest.network;

import com.bridge.androidtechnicaltest.db.Pupil;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PupilResponse {
    @SerializedName("items")
    private List<Pupil> items;

    @SerializedName("pageNumber")
    private int pageNumber;

    @SerializedName("itemCount")
    private int itemCount;

    @SerializedName("totalPages")
    private int totalPages;

    public List<Pupil> getItems() {
        return items;
    }

    public void setItems(List<Pupil> items) {
        this.items = items;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getItemCount() {
        return itemCount;
    }

    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
}