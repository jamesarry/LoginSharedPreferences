package com.eloneth.loginsharedpreferences;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.PostResponseAsyncTask;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    final String TAG = this.getClass().getName();

    Button btnLogin;
    EditText etEmail, etPassword;
    CheckBox cbRemember;

    SharedPreferences pref;
    SharedPreferences.Editor editor;

    boolean checkFlag;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        btnLogin = (Button)findViewById(R.id.btnLogin);
        etEmail = (EditText)findViewById(R.id.etEmail);
        etPassword = (EditText)findViewById(R.id.etPassword);
        cbRemember = (CheckBox)findViewById(R.id.cbRemember);
        cbRemember.setOnCheckedChangeListener(this);
        checkFlag = cbRemember.isChecked();
        Log.d(TAG, "checkFlag: " + checkFlag);


         //Pass listener class to the btn
        btnLogin.setOnClickListener(this);

        //Sharepreferences allow us to store data(key and value) using the Editor inbuilt class
        pref = getSharedPreferences("login.conf", Context.MODE_PRIVATE);
        editor = pref.edit();

    }

          //When Login Button is clicked, perform the below action
    @Override
    public void onClick(View v) {

        //Create a hash map to hold your db username and password
        HashMap data = new HashMap();
        data.put("txtUsername", etEmail.getText().toString());
        data.put("txtPassword", etPassword.getText().toString());


        //Initialize PostResponseAsyncTask and pass in AsyncResponse() and hashMap
        PostResponseAsyncTask task = new PostResponseAsyncTask(MainActivity.this, data, new AsyncResponse() {
            @Override
            public void processFinish(String s) {
                Log.d(TAG, s);
                if(s.contains("success")){//if it is successful, display SubActivity page

                      if(checkFlag){
                          // if(checkFlag){//If checkbox is true i.e is checked, perform below action
                          //Get user email and pw and save in shareFreference
                          editor.putString("username", etEmail.getText().toString());
                          editor.putString("password", etPassword.getText().toString());
                          editor.apply();
                      }
                    Intent in = new Intent(MainActivity.this, SubActivity.class);
                    startActivity(in);
                }else{
                    showAlert();//Calling alert method when login is not successful
                }

            }


        });

        //Connecting the folder where you created your database info that is in htdocs in xampp to your app. The name of the folder is customer. 10.0.02.2 for emulator. 10.0.03.2 for Genimotion
        task.execute("http://10.0.02.2/customer/index.php");//It will redirect to index.php page. To text it on a browser use ("http://10.0.02.2:8080/xampp/") or replace xampp with name of the folder in htdocs


    }

         //An Alert Method
    public void showAlert() {
        MainActivity.this.runOnUiThread(new Runnable() {
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(
                        MainActivity.this);
                builder.setTitle("Login Error.");
                builder.setMessage("User not Found. Pls try again")
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int id) {
                                    }
                                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }//End of alert box


    //For the checkbox listener
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
          checkFlag = isChecked;
        Log.d(TAG, "checkFlag: " + checkFlag);
    }
}