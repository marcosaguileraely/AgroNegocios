package com.cool4code.doncampoapp.helpers;

/**
 * Created by marcosantonioaguilerely on 11/1/14.
 */
public class MyStockModel {
    int objectId, stockId, product_id, Qty, unit_id, pricePerUnit, user_identification, user_phone;
    String product_name, unit_name, expiresAt, user_email, user_name, created;
    double geo_lat, geo_long;

    public MyStockModel(int objectId, int stockId, int product_id, int qty, int unit_id, int pricePerUnit, int user_identification, int user_phone, String product_name, String unit_name, String expiresAt, String user_email, String user_name, String created, double geo_lat, double geo_long) {
        this.objectId = objectId;
        this.stockId = stockId;
        this.product_id = product_id;
        this.Qty = qty;
        this.unit_id = unit_id;
        this.pricePerUnit = pricePerUnit;
        this.user_identification = user_identification;
        this.user_phone = user_phone;
        this.product_name = product_name;
        this.unit_name = unit_name;
        this.expiresAt = expiresAt;
        this.user_email = user_email;
        this.user_name = user_name;
        this.created = created;
        this.geo_lat = geo_lat;
        this.geo_long = geo_long;
    }

    public int getObjectId() {
        return objectId;
    }

    public void setObjectId(int objectId) {
        this.objectId = objectId;
    }

    public int getStockId() {
        return stockId;
    }

    public void setStockId(int stockId) {
        this.stockId = stockId;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public int getQty() {
        return Qty;
    }

    public void setQty(int qty) {
        this.Qty = qty;
    }

    public int getUnit_id() {
        return unit_id;
    }

    public void setUnit_id(int unit_id) {
        this.unit_id = unit_id;
    }

    public int getPricePerUnit() {
        return pricePerUnit;
    }

    public void setPricePerUnit(int pricePerUnit) {
        this.pricePerUnit = pricePerUnit;
    }

    public int getUser_identification() {
        return user_identification;
    }

    public void setUser_identification(int user_identification) {
        this.user_identification = user_identification;
    }

    public int getUser_phone() {
        return user_phone;
    }

    public void setUser_phone(int user_phone) {
        this.user_phone = user_phone;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getUnit_name() {
        return unit_name;
    }

    public void setUnit_name(String unit_name) {
        this.unit_name = unit_name;
    }

    public String getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(String expiresAt) {
        this.expiresAt = expiresAt;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public double getGeo_lat() {
        return geo_lat;
    }

    public void setGeo_lat(double geo_lat) {
        this.geo_lat = geo_lat;
    }

    public double getGeo_long() {
        return geo_long;
    }

    public void setGeo_long(double geo_long) {
        this.geo_long = geo_long;
    }
}
