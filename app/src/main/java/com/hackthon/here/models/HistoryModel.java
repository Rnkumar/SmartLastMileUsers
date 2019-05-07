package com.hackthon.here.models;

public class HistoryModel {

    private String itemName;
    private String dateOfDelivery;
    private String cost;
    private String location;

    public HistoryModel() { }

    public HistoryModel(String itemName, String dateOfDelivery, String cost, String location) {
        this.itemName = itemName;
        this.dateOfDelivery = dateOfDelivery;
        this.cost = cost;
        this.location = location;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getDateOfDelivery() {
        return dateOfDelivery;
    }

    public void setDateOfDelivery(String dateOfDelivery) {
        this.dateOfDelivery = dateOfDelivery;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
