package com.example.rasitha.RushWay;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.rasitha.RushWay.models.User;

public class CreateAccountActivity extends AppCompatActivity {

private EditText fName;
private EditText lName;
private EditText phone;
private EditText email;
private EditText nic;
private EditText pw1;
private EditText pw2;
private Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        getSupportActionBar().hide();

        fName = (EditText) findViewById(R.id.editTextFname);
        lName = (EditText) findViewById(R.id.editTextLname);
        phone = (EditText) findViewById(R.id.editTextPhone);
        email = (EditText) findViewById(R.id.editTextEmail);
        nic = (EditText) findViewById(R.id.editTextNic);
        pw1 = (EditText) findViewById(R.id.editTextPw1);
        pw2 = (EditText) findViewById(R.id.editTextPw2);
        submitButton = (Button) findViewById(R.id.submitButton);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User user_obj = new User(fName.getText().toString(),lName.getText().toString(),
                        phone.getText().toString(),email.getText().toString(),nic.getText().toString());

                if(pw2.getText().toString().equals(pw1.getText().toString())){

                    user_obj.setPw(pw2.getText().toString());
                }
                else
                {
                    Toast.makeText(CreateAccountActivity.this,"Passwords not matching",Toast.LENGTH_SHORT);
                }
            }
        });
    }


}
