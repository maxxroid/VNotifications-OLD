package com.mahesh.vnotifications;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.mahesh.vnotifications.utils.Config;
import com.mahesh.vnotifications.utils.SystemBarTintManager;
import com.mahesh.vnotifications.utils.VerifyUser;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Mahesh on 2/28/14.
 */
public class LoginMainActivity extends ActionBarActivity implements View.OnClickListener {

    Context context;

    Button loginButton;
    EditText userPassword, userUsername;
    ProgressDialog dialog;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        SystemBarTintManager tintManager=new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        actionBar.setTitle("");
        tintManager.setTintResource(R.drawable.ab_bottom_solid_apptheme);
        setContentView(R.layout.activity_login);
        loginButton = (Button) findViewById(R.id.button);
        userPassword = (EditText) findViewById(R.id.editTextPassword);
        userUsername = (EditText) findViewById(R.id.editTextUsername);

        loginButton.setOnClickListener(this);
        context = getApplicationContext();


    }

    @Override
    public void onClick(View view) {
        loginAction();
    }

    private void loginAction() {
        dialog = ProgressDialog.show(this, "Logging in", "Please wait ...", true);
        if(isNetworkAvailable()) {
            if (isUserPassValid()) {
                VerifyUser vu = new VerifyUser(userUsername.getText().toString(), userPassword.getText().toString(), this, this);
                vu.ValidateUser();
            } else
                showErrorMsg();

        }
        else
            showNoNetworkMsg();
    }

    private boolean isUserPassValid() {
        if(userUsername.getText().toString().trim().length()>4 && userPassword.getText().toString().trim().length()>3)
            return true;
        else
             return false;
    }

    public void startMainActivity(){
        dialog.dismiss();
        startActivity(new Intent(this, HomeActivity.class));
        finish();
    }


    public void showErrorMsg(){
        dialog.dismiss();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
        AlertDialog alertDialog = new AlertDialog.Builder(LoginMainActivity.this).create();
        alertDialog.setTitle("Error 101");
        alertDialog.setMessage("Invalid Username/Password");
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // TODO Add your code for the button here.
            }
        });

        alertDialog.setIcon(R.drawable.ic_launcher);
        alertDialog.show();
            }
        });
    }
    public void showNoNetworkMsg(){
        dialog.dismiss();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog alertDialog = new AlertDialog.Builder(LoginMainActivity.this).create();
                alertDialog.setTitle("Error 100");
                alertDialog.setMessage("No Internet Connection.\nPlease Try Again Later");
                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });

                alertDialog.setIcon(R.drawable.ic_launcher);
                alertDialog.show();
            }
        });
    }

    public void showServerErrorMsg(){
        dialog.dismiss();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog alertDialog = new AlertDialog.Builder(LoginMainActivity.this).create();
                alertDialog.setTitle("Error 102");
                alertDialog.setMessage("Unable to connect.\nPlease Try Again Later");
                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                alertDialog.setIcon(R.drawable.ic_launcher);
                alertDialog.show();
            }
        });

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
