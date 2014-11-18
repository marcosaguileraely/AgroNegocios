package com.cool4code.doncampoapp.helpers;

/**
 * Created by marcosantonioaguilerely on 11/9/14.
 */
public class MyOrdersModel {
    String Product_Name, Unit_Name, ExpiresAt, Address, Town, Geo_State, Country;
    String Email, Name, Identification, Phone;
    int Stock_Qty, Id_Order;
    double PricePerUnit;

    public MyOrdersModel(String product_Name, String unit_Name, String expiresAt, String address, String town, String geo_State, String country, String email, String name, String identification, String phone, int stock_Qty, int id_Order, double pricePerUnit) {
        Product_Name = product_Name;
        Unit_Name = unit_Name;
        ExpiresAt = expiresAt;
        Address = address;
        Town = town;
        Geo_State = geo_State;
        Country = country;
        Email = email;
        Name = name;
        Identification = identification;
        Phone = phone;
        Stock_Qty = stock_Qty;
        Id_Order = id_Order;
        PricePerUnit = pricePerUnit;
    }

    public int getId_Order() {
        return Id_Order;
    }

    public void setId_Order(int id_Order) {
        Id_Order = id_Order;
    }

    public String getProduct_Name() {
        return Product_Name;
    }

    public void setProduct_Name(String product_Name) {
        Product_Name = product_Name;
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

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getTown() {
        return Town;
    }

    public void setTown(String town) {
        Town = town;
    }

    public String getGeo_State() {
        return Geo_State;
    }

    public void setGeo_State(String geo_State) {
        Geo_State = geo_State;
    }

    public String getCountry() {
        return Country;
    }

    public void setCountry(String country) {
        Country = country;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getIdentification() {
        return Identification;
    }

    public void setIdentification(String identification) {
        Identification = identification;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public int getStock_Qty() {
        return Stock_Qty;
    }

    public void setStock_Qty(int stock_Qty) {
        Stock_Qty = stock_Qty;
    }

    public double getPricePerUnit() {
        return PricePerUnit;
    }

    public void setPricePerUnit(double pricePerUnit) {
        PricePerUnit = pricePerUnit;
    }
}
