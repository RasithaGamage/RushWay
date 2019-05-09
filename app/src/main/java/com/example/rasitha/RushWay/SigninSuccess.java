package com.example.rasitha.RushWay;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.rasitha.RushWay.models.Driver;
import com.example.rasitha.RushWay.models.Owner;
import com.example.rasitha.RushWay.models.Passenger;
import com.example.rasitha.RushWay.models.User;
import com.google.common.net.InternetDomainName;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;

public class SigninSuccess extends AppCompatActivity {

    DatabaseReference mDatabase;
    FirebaseAuth mAuth;
    Button b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin_success);
        getSupportActionBar().hide();

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        FirebaseUser user =  mAuth.getCurrentUser();
        b = (Button) findViewById(R.id.submitButton);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent newActivityLoad = new Intent(SigninSuccess.this,Home.class);
                startActivity(newActivityLoad);
            }
        });

        if(getIntent().getSerializableExtra("MY_USER_OBJ") instanceof  Driver){
            Driver obj = (Driver) getIntent().getSerializableExtra("MY_USER_OBJ");
            Toast.makeText(SigninSuccess.this,((Driver) obj).userType,Toast.LENGTH_SHORT).show();
            obj.setUid(user.getUid());
            mDatabase.child("Users").child("Drivers").child(user.getUid()).setValue(obj);
        }

        if(getIntent().getSerializableExtra("MY_USER_OBJ") instanceof Passenger){

            Passenger obj = (Passenger) getIntent().getSerializableExtra("MY_USER_OBJ");
            Toast.makeText(SigninSuccess.this,((Passenger) obj).userType,Toast.LENGTH_SHORT).show();
            obj.setUid(user.getUid());
            mDatabase.child("Users").child("Passengers").child(user.getUid()).setValue(obj);
        }

        if(getIntent().getSerializableExtra("MY_USER_OBJ") instanceof Owner){

            Owner obj = (Owner) getIntent().getSerializableExtra("MY_USER_OBJ");
            Toast.makeText(SigninSuccess.this,((Owner) obj).userType,Toast.LENGTH_SHORT).show();
        }
    }
}
