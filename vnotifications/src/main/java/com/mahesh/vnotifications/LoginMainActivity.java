package com.mahesh.vnotifications;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Mahesh on 2/28/14.
 */
public class LoginMainActivity extends ActionBarActivity implements View.OnClickListener {

    Button loginButton;
    EditText userPassword,userUsername;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginButton= (Button) findViewById(R.id.button);
        userPassword= (EditText) findViewById(R.id.editTextPassword);
        userUsername= (EditText) findViewById(R.id.editTextUsername);

        loginButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        loginAction();
    }

    private void loginAction(){
        if((userUsername.getText().toString()).equals("admin") )
            if(userPassword.getText().toString().equals("admin123"))
            {
                startActivity(new Intent(this,HomeActivity.class));
                finish();
            }
            else
                Toast.makeText(this,"Invalid Password",Toast.LENGTH_LONG).show();
        else
            Toast.makeText(this,"Invalid Username",Toast.LENGTH_LONG).show();
    }

}
