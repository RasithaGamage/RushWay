package com.example.rushan.RushWay;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.rushan.RushWay.models.Driver;
import com.example.rushan.RushWay.models.Passenger;
import com.example.rushan.RushWay.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateAccount2 extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseDatabase mDatabase ;
    DatabaseReference ref;
    Button b ;
    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account2);
        getSupportActionBar().hide();

        spinner = (Spinner) findViewById(R.id.spinnerSelectType);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.spinner_Content, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        mDatabase= FirebaseDatabase.getInstance();
        ref = mDatabase.getReference();

        mAuth =  FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();
        b = (Button) findViewById(R.id.submitButton);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                ref.addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        dataSnapshot.child("Users").child("User:"+user.getUid()).getChildren();
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                    }
//                });

//user chose Driver
                if(spinner.getSelectedItem().toString().equals("Bus Driver")){
                    User x = (User) getIntent().getSerializableExtra("MY_USER_OBJ");
                    Driver newDriver = new Driver(x.getfName(),x.getlName(),x.geteMail(),x.getPhone(),x.getNic());
                    newDriver.setUid(x.getUid());
                    newDriver.setPw(x.getPw());
                    newDriver.setCurrentLocation(x.getCurrentLocation());
                    moveToNextActivity(newDriver,CreateAccountDriver.class);
                }
////user chose Owner
//                else if(spinner.getSelectedItem().toString().equals("Bus Owner")){
//                    User x = (User) getIntent().getSerializableExtra("MY_USER_OBJ");
//                    Owner newOwner = new Owner(x.getfName(),x.getlName(),x.geteMail(),x.getPhone(),x.getNic());
//                    newOwner.setUid(x.getUid());
//                    newOwner.setPw(x.getPw());
//                    newOwner.setCurrentLocation(x.getCurrentLocation());
//                    moveToNextActivity(newOwner,CreateAccountDriver.class);
//                }
//user chose Passenger
                else if(spinner.getSelectedItem().toString().equals("Passenger")){
                    User x = (User) getIntent().getSerializableExtra("MY_USER_OBJ");
                    Passenger newPassenger = new Passenger(x.getfName(),x.getlName(),x.geteMail(),x.getPhone(),x.getNic());
                    newPassenger.setUid(x.getUid());
                    newPassenger.setPw(x.getPw());
                    newPassenger.setCurrentLocation(x.getCurrentLocation());
                    moveToNextActivity(newPassenger,SigninSuccess.class);
                }
//ref.child("Users").child("User:"+user.getUid()).child("").setValue("");
                else if(spinner.getSelectedItem().toString().isEmpty()){
                    Toast.makeText(CreateAccount2.this,"Please select user type",Toast.LENGTH_SHORT).show();
                }


            }
        });
    }

private void moveToNextActivity(User obj, Class nextActivity){

    Intent newActivityLoad = new Intent(CreateAccount2.this, nextActivity);
    newActivityLoad.putExtra("MY_USER_OBJ",obj );
    startActivity(newActivityLoad);


}


}
