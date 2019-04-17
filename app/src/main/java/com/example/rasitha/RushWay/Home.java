package com.example.rasitha.RushWay;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import java.sql.Connection;

public class Home extends AppCompatActivity {

    Button btnSnap ;
    ImageButton btnLeaderBoard;
    Button btnSignIn;

    private static final String TAG = "Home";
    private static final int ERROR_DIALOG_REQUEST =9001;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        btnSnap = (Button) findViewById(R.id.btnSnap);
        btnLeaderBoard = (ImageButton) findViewById(R.id.btnLeaderBoard);
        btnSignIn = (Button) findViewById(R.id.btnSignIn);

        btnSnap.setOnClickListener(new View.OnClickListener(){
                                                  @Override
                                                  public void onClick(View v){
                                                      Intent newActivityLoad = new Intent(Home.this,MapActivityNew.class);
                                                      startActivity(newActivityLoad);
                                                  }
                                              }
    );

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newActivityLoad = new Intent(Home.this,CreateAccountActivity.class);
                startActivity(newActivityLoad);

            }
        });

        if (isServicesOK()){
            init();
        }


        btnLeaderBoard.setOnClickListener(new View.OnClickListener(){
                                       @Override
                                       public void onClick(View v){
                                           Intent newActivityLoad = new Intent(Home.this,LeaderBoardActivity.class);
                                           startActivity(newActivityLoad);
                                       }
                                   }
        );
    }

    private void init() {
        Toast.makeText(Home.this, "OKKKK",Toast.LENGTH_SHORT).show();
    }

    public boolean isServicesOK(){
        Log.d(TAG,"isServicesOK : checking google services version");
        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(Home.this);

        if(available == ConnectionResult.SUCCESS){
            Log.d(TAG,"isServicesOK : Google Play Services is working");
            return true;
        }
        else if(GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            Log.d(TAG,"isServicesOK : an Error occurred but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(Home.this,available,ERROR_DIALOG_REQUEST);
            dialog.show();
        }
        else {
            Toast.makeText(this,"You cant make map requests",Toast.LENGTH_SHORT).show();
        }
        return false;
    }



}
