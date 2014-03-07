package com.mahesh.vnotifications.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

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

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mahesh on 3/3/14.Used to verify user at 1st login
 */
public class VerifyUser {
    int id;
    String username, password, name;
    Context context;
    final String DOMAIN = "http://192.168.2.2/vn/";
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
                        Log.e("log_tag", Responsesrv);
                    } else {
                        Log.e("log_tag", "LOGIN SUCCESS " + Responsesrv);
                        savetoPrefs(Responsesrv);
                        lma.startMainActivity();
                    }

                } catch (Exception e) {
                    Log.e("log_tag", "Error sending data " + e.toString());
                }
            }
        }).start();
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
        } catch (JSONException e) {
            e.printStackTrace();
        }

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

    public boolean getStatusFlag() {
        return STATUS_FLAG;
    }

}
