package com.hackthon.here.models;

public class OrdersModel {

    public String orderId, itemname, cost, dateofDelivery, deliveryLocation;

    public OrdersModel() {
    }

    public String getDeliveryLocation() {
        return deliveryLocation;
    }

    public void setDeliveryLocation(String deliveryLocation) {
        this.deliveryLocation = deliveryLocation;
    }

    public OrdersModel(String orderId, String itemname, String cost, String dateofDelivery, String deliveryLocation) {
        this.orderId = orderId;
        this.itemname = itemname;
        this.cost = cost;
        this.dateofDelivery = dateofDelivery;
        this.deliveryLocation = deliveryLocation;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getItemname() {
        return itemname;
    }

    public void setItemname(String itemname) {
        this.itemname = itemname;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getDateofDelivery() {
        return dateofDelivery;
    }

    public void setDateofDelivery(String dateofDelivery) {
        this.dateofDelivery = dateofDelivery;
    }
}
