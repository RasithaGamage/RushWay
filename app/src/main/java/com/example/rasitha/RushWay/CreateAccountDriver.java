package com.example.rasitha.RushWay;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.rasitha.RushWay.models.Driver;
import com.example.rasitha.RushWay.models.User;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateAccountDriver extends AppCompatActivity {

    Button submitButton ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account_driver);
        getSupportActionBar().hide();
        FirebaseApp.initializeApp(this);

        submitButton = (Button) findViewById(R.id.submitButton);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();
        final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Driver user_obj = (Driver) getIntent().getSerializableExtra("MY_USER_OBJ");
                Intent newActivityLoad = new Intent(CreateAccountDriver.this, SigninSuccess.class);
                newActivityLoad.putExtra("MY_USER_OBJ", getIntent().getSerializableExtra("MY_USER_OBJ") );
                startActivity(newActivityLoad);
            }
        });

    }
}
