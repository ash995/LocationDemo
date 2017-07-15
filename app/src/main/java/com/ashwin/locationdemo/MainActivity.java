package com.ashwin.locationdemo;

import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class MainActivity extends AppCompatActivity implements
        PermissionCallback,
        LocationCallback{

    private FusedLocationProviderClient mFusedLocationProviderClient;
    private TextView mLatitudeHolder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLatitudeHolder = (TextView) findViewById(R.id.latitudeHolder);
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());
    }

    @Override
    protected void onResume() {
        super.onResume();
        Permissions.init(this);
        LocationUtil.init(this);
        //This makes sure that the runtime permission checking is only done for devices
        //running on versions greater than or equal to LOLLIPOP
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            if (!Permissions.checkPermissions())
                Permissions.requestPermission();
            else
                LocationUtil.getLocation();
        }
        else {
            LocationUtil.getLocation();
        }
    }
    

    @Override
    public void showSnackBar(int explanation_string, int action_string,
                              View.OnClickListener listener) {

        //Anything can be passed in the view parameter, but if we pass it a CoordinatorLayout,
        //then we get some extra functionalities with the Snackbar. For eg, the user can swipe
        //the snackbar to ignore it, and some the views on the screen are also automatically adjusted
        //to show the snackbar, hence it is recommended that you always pass CoordinatorLayout to it
        //Just wrap your current layout in Coordinator Layout, and just pass the id of that here.

        //You should use the inflated view as the first argument, instead of just the id of the view
        //A mistake which I made earlier
        Snackbar.make(findViewById(R.id.myCoordinatorLayout),
                getString(explanation_string), //Sets the string to be displayed on the snackbar
                Snackbar.LENGTH_INDEFINITE) //Sets the duration for which the snackbar needs to be displayed
        .setAction(getString(action_string), listener) //Sets the action on the snackbar, aloong with the text for the action button
        .show(); //Finally display the snackbar
    }



    //This is the callback which gets called when the user presses a button (DENY OR ACCEPT)
    //when requesting for given permission
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        //We use the request code to identify the permission for which we requested
        switch(requestCode) {
            case MY_PERMISSIONS_REQUEST_FINE_LOCATION:
                //If the user rejects the request for all the permissions then the
                //grantResults array is of length 0
                if(grantResults.length>0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    //PERMISSION GRANTED
                    LocationUtil.getLocation();
                }
                else {
                    //PERMISSION DENIED

                    // Notify the user via a SnackBar that they have rejected a core permission for the
                    // app, which makes the Activity useless. In a real app, core permissions would
                    // typically be best requested during a welcome-screen flow.

                    // Additionally, it is important to remember that a permission might have been
                    // rejected without asking the user for permission (device policy or "Never ask
                    // again" prompts). Therefore, a user interface affordance is typically implemented
                    // when permissions are denied. Otherwise, your app could appear unresponsive to
                    // touches or interactions which have required permissions.

                }
        }
    }

    @Override
    public void locationCallback(Location location) {
        if(location==null){
            Toast.makeText(MainActivity.this, "Location not found", Toast.LENGTH_LONG)
                    .show();
        }
        else {
            mLatitudeHolder.setText(location.getLatitude() + "");
        }
    }
}
