package com.mahesh.vnotifications.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

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
    final String DOMAIN="http://192.168.2.2/vn/";
    boolean STATUS_FLAG=false;

    public VerifyUser(String username, String password, Context context) {
        this.username = username;
        this.password = password;
        this.context = context;
    }

    public void ValidateUser() {
        MD5Pass();
        new Thread(new Runnable() {
            @Override
            public void run() {
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(DOMAIN+"validate.php");
        try {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("username", username));
            nameValuePairs.add(new BasicNameValuePair("password",password));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpclient.execute(httppost);
            final String Responsesrv = EntityUtils.toString(response.getEntity());
            if(Responsesrv.trim().equals("Valid"))
                STATUS_FLAG=true;
            else
                Log.e("log_tag", Responsesrv);

        } catch (Exception e) {
            Log.e("log_tag", "Error sending data " + e.toString());
        }
            }
        }).start();
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
        BigInteger bigInt = new BigInteger(1,digest);
        String hashtext = bigInt.toString(16);
        while(hashtext.length() < 32 ){
            hashtext = "0"+hashtext;
        }
        password=hashtext;
        //Log.e("log_tag", "MD5: "+hashtext);
    }

    public boolean getStatusFlag(){
        return STATUS_FLAG;
    }

}
