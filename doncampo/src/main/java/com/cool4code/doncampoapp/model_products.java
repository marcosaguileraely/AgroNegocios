package com.cool4code.doncampoapp;

/**
 * Created by cool4code team on 7/8/14.
 * Paola Vanegas
 * Alejandro Zarate Orjuela
 * David Almeciga
 * Marcos A. Aguilera Ely
 */
public class model_products {
    private String ObjectId;
    private String code;
    private String name;
    private String createdAt;

    public String getObjectId(){
        return ObjectId;
    }

    public void setObjectId(String ObjectId){
        this.ObjectId= ObjectId;
    }

    public String getCode(){
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreatedAt(){
        return createdAt;
    }

    public void setCreatedAt(String createdAt){
        this.createdAt= createdAt;
    }
}
