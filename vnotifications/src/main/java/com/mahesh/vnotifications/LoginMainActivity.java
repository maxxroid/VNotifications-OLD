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
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.crashlytics.android.Crashlytics;
import com.mahesh.vnotifications.utils.DBAdapter;
import com.mahesh.vnotifications.utils.SystemBarTintManager;
import com.mahesh.vnotifications.utils.VerifyUser;

/**
 * Created by Mahesh on 2/28/14.
 */
public class LoginMainActivity extends ActionBarActivity implements View.OnClickListener {

    private static final String PROPERTY_APP_VERSION = "appVersion";
    Context context;

    Button loginButton;
    EditText userPassword, userUsername;
    ProgressDialog dialog;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Crashlytics.start(this);

        context = getApplicationContext();
        final SharedPreferences prefs = getGCMPreferences(context);
        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        if (registeredVersion == currentVersion) {
            startActivity(new Intent(this, HomeActivity.class));
            finish();
        } else {
            ActionBar actionBar = getSupportActionBar();
            actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
            actionBar.setDisplayShowTitleEnabled(true);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            actionBar.setTitle("");
            tintManager.setTintResource(R.drawable.ab_bottom_solid_apptheme);
            setContentView(R.layout.activity_login);
            loginButton = (Button) findViewById(R.id.button);
            userPassword = (EditText) findViewById(R.id.editTextPassword);
            userUsername = (EditText) findViewById(R.id.editTextUsername);
            DBAdapter da = new DBAdapter(this);
            da.open();
            da.close();
            loginButton.setOnClickListener(this);

        }

    }

    @Override
    public void onClick(View view) {
        loginAction();
    }

    private void loginAction() {
        dialog = ProgressDialog.show(this, "Logging in", "Please wait ...", true);
        if (isNetworkAvailable()) {
            if (isUserPassValid()) {
                VerifyUser vu = new VerifyUser(userUsername.getText().toString(), userPassword.getText().toString(), this, this);
                vu.ValidateUser();
            } else
                showErrorMsg();

        } else
            showNoNetworkMsg();
    }

    private boolean isUserPassValid() {
        if (userUsername.getText().toString().trim().length() > 4 && userPassword.getText().toString().trim().length() > 3)
            return true;
        else
            return false;
    }

    public void startMainActivity() {
        dialog.dismiss();

        startActivity(new Intent(this, HomeActivity.class));
        finish();
    }


    public void showErrorMsg() {
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

    public void showNoNetworkMsg() {
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

    public void showServerErrorMsg() {
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

    private SharedPreferences getGCMPreferences(Context context) {
        // This sample app persists the registration ID in shared preferences, but
        // how you store the regID in your app is up to you.
        return context.getSharedPreferences(LoginMainActivity.class.getSimpleName(),
                Context.MODE_PRIVATE);
    }

    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }
}
