package com.cool4code.doncampoapp.helpers;

/**
 * Created by COOL4CODE TEAM @cool4code on 9/28/14.
 * Indeed DonCampo Team
 * David Alméciga @dagrinchi
 * Marcos Aguilera @marcode_ely
 */


public class SpinnerObject {
    private  int databaseId;
    private String databaseValue;

    public SpinnerObject(int databaseId, String string, String databaseValue) {
        this.databaseId = databaseId;
        this.databaseValue = databaseValue;
    }

    public int getId () {
        return databaseId;
    }

    public String getValue () {
        return databaseValue;
    }

    @Override
    public String toString () {
        return databaseValue;
    }
}
