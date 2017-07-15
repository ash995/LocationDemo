package com.ashwin.locationdemo;

import android.view.View;

/**
 * Created by ashwin on 15/7/17.
 */

public interface PermissionCallback {

    public static final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 1;
    public void showSnackBar(int explanation_string, int action_string,
                             View.OnClickListener listener);
}
