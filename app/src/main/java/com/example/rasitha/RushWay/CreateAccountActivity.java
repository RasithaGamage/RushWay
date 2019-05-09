package com.example.rasitha.RushWay;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rasitha.RushWay.models.Driver;
import com.example.rasitha.RushWay.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateAccountActivity extends AppCompatActivity {

private EditText fName,lName,phone,email,nic,pw1,pw2;
private Button submitButton;
private TextView errorText;
private User user_obj;
private FirebaseAuth mAuth;

private static final String TAG = "CreateAccountActivity";
    private DatabaseReference mDatabase;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        getSupportActionBar().hide();
        FirebaseApp.initializeApp(this);

        mAuth = FirebaseAuth.getInstance();

        fName = (EditText) findViewById(R.id.editTextFname);
        lName = (EditText) findViewById(R.id.editTextLname);
        phone = (EditText) findViewById(R.id.editTextPhone);
        email = (EditText) findViewById(R.id.editTextEmail);
        nic = (EditText) findViewById(R.id.editTextNic);
        pw1 = (EditText) findViewById(R.id.editTextPw1);
        pw2 = (EditText) findViewById(R.id.editTextPw2);
        submitButton = (Button) findViewById(R.id.submitButton);
        errorText = (TextView) findViewById(R.id.errorText);

        mDatabase= FirebaseDatabase.getInstance().getReference();

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user_obj = new User(fName.getText().toString(),lName.getText().toString(),
                        email.getText().toString(),phone.getText().toString(),nic.getText().toString());

                user_obj.setPw(pw2.getText().toString());
                createAccount();

            }
        });
    }


private void createAccount(){

    if(pw2.getText().toString().equals(pw1.getText().toString())){

    }
    else
    {
        Toast.makeText(CreateAccountActivity.this,"Passwords not matching",Toast.LENGTH_SHORT).show();
    }

        mAuth.createUserWithEmailAndPassword(email.getText().toString(), pw1.getText().toString())
                .addOnCompleteListener(CreateAccountActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");

                            FirebaseUser user = mAuth.getCurrentUser();

                            while(user.getUid()== null){

                            }

                            Intent newActivityLoad = new Intent(CreateAccountActivity.this,CreateAccount2.class);
                            newActivityLoad.putExtra("MY_USER_OBJ",user_obj );
                            startActivity(newActivityLoad);


                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
//                                        Toast.makeText(CreateAccountActivity.this, "Authentication failed.",
//                                                Toast.LENGTH_SHORT).show();
                            String s  = task.getException().toString();
                            errorText.setText(s.substring(s.indexOf("[")+1,s.indexOf("]")-1));
                        }
                    }
                });
}

//    public boolean isInternetOn() {
//
//        // get Connectivity Manager object to check connection
//        ConnectivityManager connec =
//                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
//
//        // Check for network connections
//        if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
//                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
//                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
//                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED) {
//
//
//            return true;
//
//        } else if (
//                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
//                        connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED) {
//
//
//            return false;
//        }
//        return false;
//    }


}
