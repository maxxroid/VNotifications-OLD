package com.mahesh.vnotifications.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.mahesh.vnotifications.LoginMainActivity;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mahesh on 3/3/14.Used to verify user at 1st login
 */
public class VerifyUser {

    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    GoogleCloudMessaging gcm;
    /**
     * Substitute you own sender ID here. This is the project number you got
     * from the API Console, as described in "Getting Started."
     */
    String SENDER_ID = Config.GOOGLE_SENDER_ID;
    String regid, rollno;

    /**
     * Tag used on log messages.
     */
    static final String TAG = Config.TAG;

    String username, password;
    Context context;
    final String DOMAIN = Config.DOMAIN_URL;
    boolean STATUS_FLAG = false;
    private LoginMainActivity lma;



    public VerifyUser(String username, String password, Context context, LoginMainActivity lma) {
        this.username = username;
        this.password = password;
        this.context = context;
        this.lma = lma;
    }

    public void ValidateUser() {
        MD5Pass();
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(DOMAIN + "validate.php");
                try {
                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                    nameValuePairs.add(new BasicNameValuePair("username", username));
                    nameValuePairs.add(new BasicNameValuePair("password", password));
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                    HttpResponse response = httpclient.execute(httppost);
                    final String Responsesrv = EntityUtils.toString(response.getEntity());
                    if (Responsesrv.trim().equals("Invalid")) {
                        //Log.e("log_tag", Responsesrv);
                        lma.showErrorMsg();
                    } else {
                        savetoPrefs(Responsesrv);
                        if (validSavetoPref()) {
                            Log.e("log_tag", "LOGIN SUCCESS ");
                            if (checkPlayServices()) {
                                gcm = GoogleCloudMessaging.getInstance(context);
                                regid = getRegistrationId(context);

                                if (regid.isEmpty()) {
                                    registerInBackground();
                                }
                                lma.startMainActivity();
                            } else {
                                Log.i(TAG, "No valid Google Play Services APK found.");
                                lma.showServerErrorMsg();
                            }
                        }
                    }
                } catch (Exception e) {
                    Log.e("log_tag", "Error sending data " + e.toString());
                    e.printStackTrace();
                    lma.showServerErrorMsg();
                }
            }
        }).start();
    }



    private void registerInBackground() {
        String msg = "";
        try {
            if (gcm == null) {
                gcm = GoogleCloudMessaging.getInstance(context);
            }
            regid = gcm.register(SENDER_ID);
            msg = "Device registered, registration ID=" + regid;

            // You should send the registration ID to your server over HTTP,
            // so it can use GCM/HTTP or CCS to send messages to your app.
            // The request to your server should be authenticated if your app
            // is using accounts.
            sendRegistrationIdToBackend();

            // For this demo: we don't need to send it because the device
            // will send upstream messages to a server that echo back the
            // message using the 'from' address in the message.

            // Persist the regID - no need to register again.
            storeRegistrationId(context, regid);
        } catch (IOException ex) {
            msg = "Error :" + ex.getMessage();

            lma.showServerErrorMsg();
            // If there is an error, don't just keep trying to register.
            // Require the user to click a button again, or perform
            // exponential back-off.
        }
        Log.e("GCM_RESULT", msg);
    }

    private void sendRegistrationIdToBackend() {
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(DOMAIN + "gcmregister.php");
        try {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("rollno", rollno));
            nameValuePairs.add(new BasicNameValuePair("gcmreg", regid));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpclient.execute(httppost);
            final String Responsesrv = EntityUtils.toString(response.getEntity());
            //Log.e("GCM Response", Responsesrv);
        } catch (Exception e) {
            Log.e("log_tag", "Error sending data " + e.toString());
            lma.showServerErrorMsg();
        }
    }

    private void savetoPrefs(String responsesrv) {
        JSONArray jArray = null;
        try {
            jArray = new JSONArray(responsesrv);
            JSONObject json = jArray.getJSONObject(0);
            SharedPreferences prefs = context.getSharedPreferences("user_account_info", 0);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("user_name", json.getString("username"));
            editor.putString("name", json.getString("name"));
            editor.putString("rollno", json.getString("rollno"));
            editor.putString("year", json.getString("year"));
            editor.putString("department", json.getString("department"));
            editor.putString("division", json.getString("division"));
            editor.putString("batch", json.getString("batch"));
            editor.putString("group_id", json.getString("group_id"));
            editor.commit();
            rollno = json.getString("rollno").trim();
            STATUS_FLAG=true;
        } catch (JSONException e) {
            e.printStackTrace();
            lma.showServerErrorMsg();
        }
    }

    private boolean validSavetoPref() {
        return STATUS_FLAG;
    }

    private void MD5Pass() {
        String plaintext = password;
        MessageDigest m = null;
        try {
            m = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        m.reset();
        m.update(plaintext.getBytes());
        byte[] digest = m.digest();
        BigInteger bigInt = new BigInteger(1, digest);
        String hashtext = bigInt.toString(16);
        while (hashtext.length() < 32) {
            hashtext = "0" + hashtext;
        }
        password = hashtext;
        //Log.e("log_tag", "MD5: "+hashtext);
    }

    private String getRegistrationId(Context context) {
        final SharedPreferences prefs = getGCMPreferences(context);
        String registrationId = prefs.getString(PROPERTY_REG_ID, "");
        if (registrationId.isEmpty()) {
            Log.i(TAG, "Registration not found.");
            return "";
        }
        // Check if app was updated; if so, it must clear the registration ID
        // since the existing regID is not guaranteed to work with the new
        // app version.
        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {
            Log.i(TAG, "App version changed.");
            return "";
        }
        return registrationId;
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, lma,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(Config.TAG, "This device is not supported.");
            }
            return false;
        }
        return true;
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

    private SharedPreferences getGCMPreferences(Context context) {
        // This sample app persists the registration ID in shared preferences, but
        // how you store the regID in your app is up to you.
        return context.getSharedPreferences(LoginMainActivity.class.getSimpleName(),
                Context.MODE_PRIVATE);
    }

    private void storeRegistrationId(Context context, String regId) {
        final SharedPreferences prefs = getGCMPreferences(context);
        int appVersion = getAppVersion(context);
        Log.i(TAG, "Saving regId on app version " + appVersion);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_REG_ID, regId);
        editor.putInt(PROPERTY_APP_VERSION, appVersion);
        editor.commit();
    }
}
