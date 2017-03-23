package com.example.linux1.appcohol;

import android.app.Application;
import android.util.Log;

import com.parse.Parse;

/**
 * Esta clase se encarga de la comunicación con Heroku
 */

public class ParseApplication extends Application {
    @Override
    public void onCreate(){
        super.onCreate();

        Log.v("ParseApplication","onCreateCalled");
        Parse.initialize(new Parse.Configuration.Builder(getApplicationContext())
                .applicationId("myAppId")
                .clientKey("empty")
                .server("https://appcohol1.herokuapp.com/parse/")
                .build());
    }
}