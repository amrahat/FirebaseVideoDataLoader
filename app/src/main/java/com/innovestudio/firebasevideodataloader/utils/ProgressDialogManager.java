package com.innovestudio.firebasevideodataloader.utils;

import android.app.Activity;
import android.app.ProgressDialog;

import com.innovestudio.firebasevideodataloader.R;

/**
 * Created by AMRahat on 3/11/2017.
 */

public class ProgressDialogManager {
    private Activity activity;
    private ProgressDialog progressDialog;
    public ProgressDialogManager(Activity activity) {
        this.activity = activity;
        progressDialog = new ProgressDialog(activity);
    }

    public void showLoader(String message){
        progressDialog.setMessage(message);
        progressDialog.show();
    }
    public void hideLoader(){
        progressDialog.dismiss();
    }
}
