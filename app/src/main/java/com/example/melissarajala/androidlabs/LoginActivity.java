package com.example.melissarajala.androidlabs;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.EditText;
import android.view.View;

public class LoginActivity extends Activity {

    protected static final String ACTIVITY_NAME = "LoginActivity";
    Button button;
    EditText emailInput;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    static final String FILE_NAME = "prefFile";
    static final String KEY_EMAIL = "email";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Log.i(ACTIVITY_NAME, "In onCreate()");

        button = (Button) findViewById(R.id.button2);
        emailInput = (EditText) findViewById(R.id.email);

        sharedPref = getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        editor = sharedPref.edit();
        emailInput.setText(sharedPref.getString(KEY_EMAIL, "user@domain.com"));

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                editor.putString(KEY_EMAIL, emailInput.getText().toString());
                editor.commit();

                //call the StartActivity
                Intent intent = new Intent(LoginActivity.this, StartActivity.class);
                startActivity(intent);
            }
        });
    }



    @Override
    public void onResume(){
        super.onResume();
        Log.i(ACTIVITY_NAME, "In onResume()");
    }

    @Override
    public void onStart(){
        super.onStart();
        Log.i(ACTIVITY_NAME, "In onStart()");
    }

    @Override
    public void onPause(){
        super.onPause();
        Log.i(ACTIVITY_NAME, "In onPause()");
    }

    @Override
    public void onStop(){
        super.onStop();
        Log.i(ACTIVITY_NAME, "In onStop()");
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.i(ACTIVITY_NAME, "In onDestroy()");
    }
}

