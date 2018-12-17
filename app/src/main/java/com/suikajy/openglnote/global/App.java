package com.suikajy.openglnote.global;

import android.app.Application;

/**
 * Created by suikajy on 2018.12.14
 */
public class App extends Application {

    private static App instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static App getInstance() {
        return instance;
    }
}
