package com.mahesh.vnotifications;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
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
        setContentView(R.layout.activity_login);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle("");
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
        VerifyUser vu=new VerifyUser(userUsername.getText().toString(),userPassword.getText().toString(),this,this);
        vu.ValidateUser();
    }

    public void startMainActivity(){
        dialog.dismiss();
        startActivity(new Intent(this, HomeActivity.class));
        finish();
    }

    public void showErrorMsg(){
        dialog.dismiss();
    }




}
