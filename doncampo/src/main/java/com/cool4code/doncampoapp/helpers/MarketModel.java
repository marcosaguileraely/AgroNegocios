package com.cool4code.doncampoapp.helpers;

/**
 * Created by marcosantonioaguilerely on 11/5/14.
 */
public class MarketModel {
    int Id, product_id, unit_id, Qty, PricePerUnit;
    String Product_Code, Product_Name, Unit_Code, Unit_Name, ExpiresAt, GeoPoint_Address, GeoPoint_Town, GeoPoint_State, GeoPoint_Country;
    String Email, User_Name, User_Identification, User_Phone, Created, Updated;
    double Latitude, Longitude;

    public MarketModel(int id, int product_id, int unit_id, int qty, int pricePerUnit, String product_Code, String product_Name, String unit_Code, String unit_Name, String expiresAt, String geoPoint_Address, String geoPoint_Town, String geoPoint_State, String geoPoint_Country, String email, String user_Name, String user_Identification, String user_Phone, String created, String updated, double latitude, double longitude) {
        Id = id;
        this.product_id = product_id;
        this.unit_id = unit_id;
        Qty = qty;
        PricePerUnit = pricePerUnit;
        Product_Code = product_Code;
        Product_Name = product_Name;
        Unit_Code = unit_Code;
        Unit_Name = unit_Name;
        ExpiresAt = expiresAt;
        GeoPoint_Address = geoPoint_Address;
        GeoPoint_Town = geoPoint_Town;
        GeoPoint_State = geoPoint_State;
        GeoPoint_Country = geoPoint_Country;
        Email = email;
        User_Name = user_Name;
        User_Identification = user_Identification;
        User_Phone = user_Phone;
        Created = created;
        Updated = updated;
        Latitude = latitude;
        Longitude = longitude;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public int getUnit_id() {
        return unit_id;
    }

    public void setUnit_id(int unit_id) {
        this.unit_id = unit_id;
    }

    public int getQty() {
        return Qty;
    }

    public void setQty(int qty) {
        Qty = qty;
    }

    public int getPricePerUnit() {
        return PricePerUnit;
    }

    public void setPricePerUnit(int pricePerUnit) {
        PricePerUnit = pricePerUnit;
    }

    public String getProduct_Code() {
        return Product_Code;
    }

    public void setProduct_Code(String product_Code) {
        Product_Code = product_Code;
    }

    public String getProduct_Name() {
        return Product_Name;
    }

    public void setProduct_Name(String product_Name) {
        Product_Name = product_Name;
    }

    public String getUnit_Code() {
        return Unit_Code;
    }

    public void setUnit_Code(String unit_Code) {
        Unit_Code = unit_Code;
    }

    public String getUnit_Name() {
        return Unit_Name;
    }

    public void setUnit_Name(String unit_Name) {
        Unit_Name = unit_Name;
    }

    public String getExpiresAt() {
        return ExpiresAt;
    }

    public void setExpiresAt(String expiresAt) {
        ExpiresAt = expiresAt;
    }

    public String getGeoPoint_Address() {
        return GeoPoint_Address;
    }

    public void setGeoPoint_Address(String geoPoint_Address) {
        GeoPoint_Address = geoPoint_Address;
    }

    public String getGeoPoint_Town() {
        return GeoPoint_Town;
    }

    public void setGeoPoint_Town(String geoPoint_Town) {
        GeoPoint_Town = geoPoint_Town;
    }

    public String getGeoPoint_State() {
        return GeoPoint_State;
    }

    public void setGeoPoint_State(String geoPoint_State) {
        GeoPoint_State = geoPoint_State;
    }

    public String getGeoPoint_Country() {
        return GeoPoint_Country;
    }

    public void setGeoPoint_Country(String geoPoint_Country) {
        GeoPoint_Country = geoPoint_Country;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getUser_Name() {
        return User_Name;
    }

    public void setUser_Name(String user_Name) {
        User_Name = user_Name;
    }

    public String getUser_Identification() {
        return User_Identification;
    }

    public void setUser_Identification(String user_Identification) {
        User_Identification = user_Identification;
    }

    public String getUser_Phone() {
        return User_Phone;
    }

    public void setUser_Phone(String user_Phone) {
        User_Phone = user_Phone;
    }

    public String getCreated() {
        return Created;
    }

    public void setCreated(String created) {
        Created = created;
    }

    public String getUpdated() {
        return Updated;
    }

    public void setUpdated(String updated) {
        Updated = updated;
    }

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }
}
