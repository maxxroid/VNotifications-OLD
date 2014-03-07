package com.mahesh.vnotifications;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mahesh.vnotifications.utils.VerifyUser;

/**
 * Created by Mahesh on 2/28/14.
 */
public class LoginMainActivity extends ActionBarActivity implements View.OnClickListener {

    Button loginButton;
    EditText userPassword, userUsername;


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
    }

    @Override
    public void onClick(View view) {
        loginAction();
    }

    private void loginAction() {
        VerifyUser vu=new VerifyUser(userUsername.getText().toString(),userPassword.getText().toString(),this,this);
        vu.ValidateUser();
    }

    public void startMainActivity(){
        startActivity(new Intent(this, HomeActivity.class));
        finish();
    }
}
