package com.example.rasitha.RushWay;

import android.app.Dialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Home extends AppCompatActivity {

    Button btnLogin;
    ImageButton btnLeaderBoard;
    Button btnSignIn;
    EditText editTextEmail,editTextPw;

    private static final String TAG = "Home";
    private static final int ERROR_DIALOG_REQUEST =9001;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        btnLogin = (Button) findViewById(R.id.btnSnap);
        btnLeaderBoard = (ImageButton) findViewById(R.id.btnLeaderBoard);
        btnSignIn = (Button) findViewById(R.id.btnSignIn);
        editTextEmail = (EditText) findViewById(R.id.uidEditText);
        editTextPw = (EditText) findViewById(R.id.pwdEditText);

        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();

        btnLogin.setOnClickListener(new View.OnClickListener(){
                                                  @Override
                                                  public void onClick(View v){
                                                        checkLogin();
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

    private void checkLogin() {

        mAuth.signInWithEmailAndPassword(editTextEmail.getText().toString(), editTextPw.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            Intent newActivityLoad = new Intent(Home.this,MapActivityNew.class);
                            startActivity(newActivityLoad);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(Home.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }

                    }
                });


    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent newActivityLoad = new Intent(Home.this,MapActivityNew.class);
            startActivity(newActivityLoad);
        }
    }

    @Override
    public void onBackPressed() {


    }


}
