package com.ashwin.locationdemo;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;

import static com.ashwin.locationdemo.PermissionCallback.MY_PERMISSIONS_REQUEST_FINE_LOCATION;

/**
 * Created by ashwin on 15/7/17.
 */

public class Permissions {

    private static PermissionCallback mActivityInstance;

    public static void init(PermissionCallback activity) {
        mActivityInstance = activity;
    }

    //This is how we check for permissions in Android
    //This step is only required for dangerous permissions in Android (for apps targeting
    //above API level 22 some permissions have to be granted by the user at runtime,
    // and for devices running on lower than API level 22, the permissions
    //have to granted during installation,
    public static boolean checkPermissions() {
        //If the permission has beeen granted, PackageManager.PERMISSION_GRANTED is returned,
        //Otherwise PackageManager.PERMISSION_DENIED is returned
        int permissionCode = ContextCompat.checkSelfPermission(MyApplication.getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION);

        if(permissionCode == PackageManager.PERMISSION_GRANTED)
            return true;
        else
            return false;
    }


    //This is how we request for permissions in android (only required for devices running
    //on android version above API level 22)
    public static void requestPermission() {

        //shouldShowRequestPermissionRationale returns true when previously the user had denied
        //our request for the permission, so when the user tries to use the requested permission
        //again, we can explain to the user as to why we need the permission
        //So we do that with the help of a snackbar
        // Provide an additional rationale to the user. This would happen if the user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if(ActivityCompat.shouldShowRequestPermissionRationale((Activity) mActivityInstance,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            mActivityInstance.showSnackBar(R.string.location_rationale_string,
                    R.string.location_request_action_string,
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //After the user has looked at the explanation in the snackbar
                            //Provide him with an action to give us the requested permission
                            ActivityCompat.requestPermissions((Activity) mActivityInstance,
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
            ActivityCompat.requestPermissions((Activity) mActivityInstance,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_FINE_LOCATION);
        }
    }
}
