package com.hackthon.here.models;

public class SubOrdersModel {

    private String mobile, Address, ItemName, Location, userId;
    private boolean published, delivered;
    private int quantity;

    public SubOrdersModel() {
    }

    public SubOrdersModel(String mobile, String address, String itemName, String location, String userId, boolean published, boolean delivered, int quantity) {
        this.mobile = mobile;
        Address = address;
        ItemName = itemName;
        Location = location;
        this.userId = userId;
        this.published = published;
        this.delivered = delivered;
        this.quantity = quantity;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isPublished() {
        return published;
    }

    public void setPublished(boolean published) {
        this.published = published;
    }

    public boolean isDelivered() {
        return delivered;
    }

    public void setDelivered(boolean delivered) {
        this.delivered = delivered;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
