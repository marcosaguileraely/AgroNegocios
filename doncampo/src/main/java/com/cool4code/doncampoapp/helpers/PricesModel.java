package com.cool4code.doncampoapp.helpers;

/**
 * Created by marcosantonioaguilerely on 10/23/14.
 */
public class PricesModel {
    int objectId, Id, PriceMaxPerUnit, PriceMinPerUnit, PriceAvgPerUnit;
    String  Product_Code, Product_Name, Unit_Code, Unit_Name, Location, Created, Updated;

    public int getObjectId() {
        return objectId;
    }

    public void setObjectId(int objectId) {
        this.objectId = objectId;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getPriceMaxPerUnit() {
        return PriceMaxPerUnit;
    }

    public void setPriceMaxPerUnit(int priceMaxPerUnit) {
        PriceMaxPerUnit = priceMaxPerUnit;
    }

    public int getPriceMinPerUnit() {
        return PriceMinPerUnit;
    }

    public void setPriceMinPerUnit(int priceMinPerUnit) {
        PriceMinPerUnit = priceMinPerUnit;
    }

    public int getPriceAvgPerUnit() {
        return PriceAvgPerUnit;
    }

    public void setPriceAvgPerUnit(int priceAvgPerUnit) {
        PriceAvgPerUnit = priceAvgPerUnit;
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

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
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

    @Override
    public String toString() {
        return this.Product_Name;
        /*return "PricesModel{" +
                "objectId=" + objectId +
                ", Id=" + Id +
                ", PriceMaxPerUnit=" + PriceMaxPerUnit +
                ", PriceMinPerUnit=" + PriceMinPerUnit +
                ", PriceAvgPerUnit=" + PriceAvgPerUnit +
                ", Product_Code='" + Product_Code + '\'' +
                ", Product_Name='" + Product_Name + '\'' +
                ", Unit_Code='" + Unit_Code + '\'' +
                ", Unit_Name='" + Unit_Name + '\'' +
                ", Location='" + Location + '\'' +
                ", Created='" + Created + '\'' +
                ", Updated='" + Updated + '\'' +
                '}';*/
    }

    public PricesModel(String product_Name, String Location, String Unit_Name, int PriceAvgPerUnit ) {
        super();
        this.Product_Name = product_Name;
        this.Location = Location;
        this.Unit_Name = Unit_Name;
        this.PriceAvgPerUnit = PriceAvgPerUnit;
    }
}
