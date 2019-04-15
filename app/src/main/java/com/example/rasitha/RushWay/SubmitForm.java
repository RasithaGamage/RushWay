package com.example.rasitha.RushWay;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.libraries.places.compat.Place;
import com.google.android.libraries.places.compat.ui.PlacePicker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class SubmitForm extends AppCompatActivity {

    int PLACE_PICKER_REQUEST = 1;
    private RecyclerView myRecyclerView = null;
    private RecyclerView.LayoutManager myLayoutManager= null;
    private RecyclerView.Adapter myAdapter= null;

    private ArrayList<String> myDataset= null;

    private final int CAMERA_RESULT =101 ;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_form);

        ////////////C A M E R A /////////////////////////////////

        if(ContextCompat.checkSelfPermission(SubmitForm.this, Manifest.permission.CAMERA)== PackageManager.PERMISSION_DENIED){
            dispatchTakenPictureIntent();
        }
        else {
            if(shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)){
                Toast.makeText(getApplicationContext(), "Permission Needed", Toast.LENGTH_SHORT).show();
            }

            requestPermissions(new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE},CAMERA_RESULT);
        }

        ////////////////////////////////////////////////////////////



        Spinner spinner = (Spinner) findViewById(R.id.spinner1);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.planets_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        String[] myResArray = getResources().getStringArray(R.array.planets_array);
        List<String> myResArrayList = Arrays.asList(myResArray);
        ArrayList<String> items = new ArrayList<String>(myResArrayList);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                Object item = adapterView.getItemAtPosition(position);
                if (item != null) {
                   /* Toast.makeText(SubmitForm.this, item.toString(),
                            Toast.LENGTH_SHORT).show();*/
                }
               /* Toast.makeText(SubmitForm.this, "Selected",
                        Toast.LENGTH_SHORT).show();*/

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // TODO Auto-generated method stub

            }

        });

        Button btnMap = (Button) findViewById(R.id.btnPickMap);

        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*startActivity(new Intent(SubmitForm.this,MapPopUp.class));*/

                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

                try {
                    Intent intent = builder.build((Activity) getApplicationContext());

                    startActivityForResult(intent,PLACE_PICKER_REQUEST);

                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });

        myDataset = new ArrayList<>();
        for(int i=0;i<10;i++){
            myDataset.add("New TItle # "+ i);
        }

        myRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        myRecyclerView.setHasFixedSize(true);
        myLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        myRecyclerView.setLayoutManager(myLayoutManager);
        myAdapter = new MainAdapter(myDataset);
        myRecyclerView.setAdapter(myAdapter);



        Button btnSubmit = (Button) findViewById(R.id.btnSubmit);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent  newIntent = new Intent(SubmitForm.this,Home.class);
                startActivity(newIntent);

                Toast.makeText(SubmitForm.this, "Successfully Submited", Toast.LENGTH_LONG).show();
            }
        });
    }


    ///////////////////////////C A M E R A ///////////////////////////
    private void dispatchTakenPictureIntent() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(intent.resolveActivity(getPackageManager()) != null){
            startActivityForResult(intent,CAMERA_RESULT);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String[] permissions, int[] grantResults){

        if(requestCode == CAMERA_RESULT){
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED && grantResults[1]==PackageManager.PERMISSION_GRANTED){

                dispatchTakenPictureIntent();
            }
            else {
                Toast.makeText(getApplicationContext(),"Permission Needed",Toast.LENGTH_SHORT).show();
            }
        }
        else {
            super.onRequestPermissionsResult(requestCode,permissions,grantResults);
        }
    }


    //////////////////////////////////////////////////////////////////


    EditText editTextaddress= null ;
    TextView coordiates = null;

    ImageView title = null;


    @Override
    protected  void onActivityResult(int requestCode,int resultCode,Intent data){
        if(requestCode==PLACE_PICKER_REQUEST){
            if(resultCode==RESULT_OK){

                Place place = PlacePicker.getPlace(data,this);
                String address = String.format("Place : %s",place.getAddress());
                Double latitude = place.getLatLng().latitude;
                Double longitude = place.getLatLng().longitude;
                /*Toast.makeText(SubmitForm.this, address,Toast.LENGTH_SHORT).show();*/
                editTextaddress = (EditText) findViewById(R.id.editTextaddress);
                editTextaddress.setText(address);

                coordiates = (TextView) findViewById(R.id.editTextCoordinates);
                coordiates.setText(latitude+", "+longitude);

            }
        }
        //////////////////////// C A M E R A ///////////////////////////////
        title = (ImageView) findViewById(R.id.title);

        if(resultCode == RESULT_OK){
            if(requestCode == CAMERA_RESULT){
                Bundle extra = data.getExtras();
                Bitmap bitmap =  (Bitmap) extra.get("data");
                title.setImageBitmap(bitmap);
            }
        }
        //////////////////////////////////////////////////////////////////
    }






}
