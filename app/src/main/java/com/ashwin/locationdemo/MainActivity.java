package com.ashwin.locationdemo;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class MainActivity extends AppCompatActivity implements PermissionCallback{

    private FusedLocationProviderClient mFusedLocationProviderClient;
    private Location mLocation;
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
        //This makes sure that the runtime permission checking is only done for devices
        //running on versions greater than or equal to LOLLIPOP
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            if (!Permissions.checkPermissions())
                Permissions.requestPermission();
            else
                getLastLocation();
        }
        else {
            getLastLocation();
        }
    }


    //This is how we request for permissions in android (only required for devices running
    //on android version above API level 22)
    private void requestPermission() {

        //shouldShowRequestPermissionRationale returns true when previously the user had denied
        //our request for the permission, so when the user tries to use the requested permission
        //again, we can explain to the user as to why we need the permission
        //So we do that with the help of a snackbar
        // Provide an additional rationale to the user. This would happen if the user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if(ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            showSnackBar(R.string.location_rationale_string,
                    R.string.location_request_action_string,
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //After the user has looked at the explanation in the snackbar
                            //Provide him with an action to give us the requested permission
                            ActivityCompat.requestPermissions(MainActivity.this,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                    MY_PERMISSIONS_REQUEST_FINE_LOCATION);
                        }
                    });
        }
        else {
            //This automatically creates a dialog box (which we cannot customize) asking the
            //user to grant us the requested permission
            //THE USER CAN THEN ACCEPT OR DENY THE REQUEST
            //which we handle in the onRequestPermissionsResults callback
            //First argument is the activity instance
            //Second is the list of permissions  which we need in a string array
            //Third is the resultcode which we can use to identify our requested permission
            //in the callback
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_FINE_LOCATION);
        }
    }

    //Suppresses the missing permission request warning, but we should make sure that we
    //reach this method only after we have the required permission from the user
    @SuppressWarnings("MissingPermission")
    private void getLastLocation() {
        mFusedLocationProviderClient.getLastLocation()
                .addOnSuccessListener(MainActivity.this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if(location!=null) {
                            mLatitudeHolder.setText("" + location.getLatitude());
                        }
                        else {
                            Toast.makeText(MainActivity.this, "Location not fetched",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
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






    //This is how we check for permissions in Android
    //This step is only required for dangerous permissions in Android (for apps targeting
    //above API level 22 some permissions have to be granted by the user at runtime,
    // and for devices running on lower than API level 22, the permissions
    //have to granted during installation,
    private boolean checkPermissions() {
        //If the permission has beeen granted, PackageManager.PERMISSION_GRANTED is returned,
        //Otherwise PackageManager.PERMISSION_DENIED is returned
        int permissionCode = ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION);

        if(permissionCode == PackageManager.PERMISSION_GRANTED)
            return true;
        else
            return false;
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
                    getLastLocation();
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
}
