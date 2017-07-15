package com.ashwin.locationdemo;

import android.app.Application;
import android.content.Context;

/**
 * Created by ashwin on 15/7/17.
 */

public class MyApplication extends Application {
    private static MyApplication myApplication;

    public MyApplication() {
        myApplication = this;
    }

    public static Context getContext() {
        return myApplication;
    }
}
