package com.ashwin.locationdemo;

import android.app.Activity;
import android.location.Location;


import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;


/**
 * Created by ashwin on 15/7/17.
 */

public class LocationUtil {
    private static FusedLocationProviderClient mFusedLocationProviderClient;
    private static Activity myActivity;
    private static Location mLocation;

    public static void init(Activity activity) {
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(MyApplication.getContext());
        myActivity = activity;
    }

    //Suppresses the missing permission request warning, but we should make sure that we
    //reach this method only after we have the required permission from the user
    @SuppressWarnings("MissingPermission")
    public static void getLocation()  {
        mFusedLocationProviderClient.getLastLocation()
                .addOnSuccessListener(myActivity, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        LocationCallback mLocationCallback
                                = (LocationCallback) myActivity;
                        mLocationCallback.locationCallback(location);
                    }
                });
    }
}
