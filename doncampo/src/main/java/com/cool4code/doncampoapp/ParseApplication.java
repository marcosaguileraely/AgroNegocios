package com.cool4code.doncampoapp;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseUser;

/**
 * Created by cool4code team on 7/8/14.
 * Paola Vanegas
 * Alejandro Zarate Orjuela
 * David Almeciga
 * Marcos A. Aguilera Ely
 */

public class ParseApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        // Add your initialization code here
        Parse.initialize(this, "Hy4HilhJuJCkZFCNLVZMSmZB6Zihvg1pbASEBqAz", "LnWKpocESxfmvRLskZ27lg20Y4AnC982Eh02DEZk");

        ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();
        // If you would like all objects to be private by default, remove this line.
        defaultACL.setPublicReadAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);
    }
}
