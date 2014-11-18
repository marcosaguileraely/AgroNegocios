package com.cool4code.doncampoapp.helpers;

/**
 * Created by COOL4CODE TEAM @cool4code on 9/28/14.
 * Indeed DonCampo Team
 * David Alméciga @dagrinchi
 * Marcos Aguilera @marcode_ely
 */


public class AuthModel {
    private int Id;
    private String Token;
    private String tokenType;
    private int expiresIn;
    private String userName;


    public AuthModel(int Id, String Token, String tokenType, int expiresIn, String userName) {
        this.Id = Id;
        this.Token = Token;
        this.tokenType = tokenType;
        this.expiresIn = expiresIn;
        this.userName = userName;
    }

    public int getId(){
        return Id;
    }

    public String getToken(){
        return Token;
    }

    public String getTokenType(){
        return tokenType;
    }

    public int getExpiresIn(){
        return expiresIn;
    }

    public String getUserName(){
        return userName;
    }
}
