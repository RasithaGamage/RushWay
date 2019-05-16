package com.example.rasitha.RushWay;

import android.Manifest;
import android.animation.ValueAnimator;
import android.app.ActionBar;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rasitha.RushWay.directionHelpers.FetchURL;
import com.example.rasitha.RushWay.directionHelpers.TaskLoadedCallback;
import com.example.rasitha.RushWay.models.Driver;
import com.example.rasitha.RushWay.models.PlaceInfo;
import com.example.rasitha.RushWay.models.RWLocation;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.RuntimeRemoteException;
import com.google.android.libraries.places.compat.AutocompletePrediction;
import com.google.android.libraries.places.compat.GeoDataClient;
import com.google.android.libraries.places.compat.Place;
import com.google.android.libraries.places.compat.PlaceBuffer;
import com.google.android.libraries.places.compat.PlaceBufferResponse;
import com.google.android.libraries.places.compat.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;

public class MapActivityNew extends AppCompatActivity implements OnMapReadyCallback,GoogleApiClient.OnConnectionFailedListener,TaskLoadedCallback {

    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 15f;

    private static final String TAG = "MapActivityNew";

    private AutoCompleteTextView mSearchText ;
    private ImageView mGps,mInfo;
    private Spinner mSpinner;


    private static GoogleMap mMap;
    private Boolean mLocationPermissionGranted = false;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private PlaceAutocompleteAdapter mPlaceAutocompleteAdapter;
    private GeoDataClient mGoogleApiClient;
    private PlaceInfo mPlace;
    private Marker mMarker;
    private Polyline mCurrentPolyline;
    private Location mCurrentLocation;
    private DatabaseReference mDatabase;
    private LocationRequest mLocationRequest;
    private FirebaseAuth mAuth;
    private static String userType;
    private static FirebaseUser fUser;

    private DrawerLayout dl;
    private ActionBarDrawerToggle t;
    private NavigationView nv;

    private static Handler handler;




    private long UPDATE_INTERVAL = 10 * 1000;  /* 10 secs */
    private long FASTEST_INTERVAL = 2000; /* 2 sec */



    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(
            new LatLng(-40,-168),new LatLng(71,136)
    );

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Toast.makeText(this, "Map is Ready", Toast.LENGTH_SHORT).show();
        mMap = googleMap;

        if (mLocationPermissionGranted) {
            getLDeviceLocation();
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
            //"Show My location"(GPS icon) button removed, because our custom search bar will block its view
           // mMap.getUiSettings()

            init();

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_new);
        // getSupportActionBar().hide();

        dl = (DrawerLayout)findViewById(R.id.activity_main);
        t = new ActionBarDrawerToggle(this, dl,R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        dl.addDrawerListener(t);
        t.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        nv = (NavigationView)findViewById(R.id.nv);
        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch(id)
                {
                    case R.id.account:
                        Toast.makeText(MapActivityNew.this, "My Account",Toast.LENGTH_SHORT).show();break;
                    case R.id.settings:
                        Toast.makeText(MapActivityNew.this, "Settings",Toast.LENGTH_SHORT).show();break;
                    case R.id.mycart:
                        Toast.makeText(MapActivityNew.this, "My Cart",Toast.LENGTH_SHORT).show();break;
                    case R.id.logout:
                    {
                        Toast.makeText(MapActivityNew.this, "Logged out",Toast.LENGTH_SHORT).show();
                        //Sign out
                         FirebaseAuth.getInstance().signOut();
                         userType= null;
                        Intent newActivityLoad = new Intent(MapActivityNew.this,Home.class);
                        startActivity(newActivityLoad);
                         break;

                    }
                    default:
                        return true;
                }


                return true;

            }
        });


        mSpinner = (Spinner) findViewById(R.id.spinner1);
        List<String> list = new ArrayList<String>();
        list.add("Select location on map");
        list.add("Select location from saved places");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,  android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(R.layout.spinner_item_layout);
        mSpinner.setAdapter(adapter);

        mSearchText = (AutoCompleteTextView) findViewById(R.id.inputSearch) ;
        mGps = (ImageView) findViewById(R.id.ic_gps) ;
        mInfo= (ImageView) findViewById(R.id.place_info);

        mDatabase= FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        getLocationPermission();
        FirebaseApp.initializeApp(this);

        fUser = mAuth.getCurrentUser();

        userType = null;

        if(mAuth.getCurrentUser()!= null) {

            Log.d(TAG,"UID: "+fUser.getUid());
            String nodePaths[] =  {"Users/Owners","Users/Drivers","Users/Passengers"};

                if(userType == null){
                    for(String path: nodePaths){
                    final DatabaseReference userRef1 = FirebaseDatabase.getInstance().getReference().child(path);
                    Query queryRef = userRef1.orderByChild("uid").equalTo(fUser.getUid());
                    queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            for(DataSnapshot userResult : dataSnapshot.getChildren() ){
                                if(userResult.child("uid").getValue().equals(fUser.getUid())){
                                    userType = (String) userResult.child("userType").getValue();
                                    Log.d(TAG,"userType 1 : "+userType);

                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Log.d(TAG,"databaseError : "+databaseError.getMessage());
                        }
                    });
                }
            }

        }

        startLocationUpdates();
        updateBusLocations();

        if(handler == null ){
            handler = new Handler();
        }
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG,"getNearbyBuses() in postDelayed is running");
                getNearbyBuses();
            }
        }, 3000);
    }

    private void startLocationUpdates() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        LocationSettingsRequest locationSettingsRequest = builder.build();

        SettingsClient settingsClient = LocationServices.getSettingsClient(this);
        settingsClient.checkLocationSettings(locationSettingsRequest);

        getFusedLocationProviderClient(this).requestLocationUpdates(mLocationRequest, new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        // do work here

                        if (locationResult.getLastLocation()!=null && mAuth.getCurrentUser()!= null && userType!= null)
                        {
                            RWLocation loc = new RWLocation(locationResult.getLastLocation().getLatitude(),locationResult.getLastLocation().getLongitude());
                            Log.d(TAG,"userType 2: "+userType);
                            switch (userType){
                                case "driver":
                                {
                                    mDatabase.child("Users").child("Drivers").child(mAuth.getCurrentUser().getUid()).child("currentLocation").setValue(loc);
                                    break;
                                }
                                case "passenger":
                                {
                                    mDatabase.child("Users").child("Passengers").child(mAuth.getCurrentUser().getUid()).child("currentLocation").setValue(loc);
                                    break;
                                }
                                case "owner":
                                {
                                    mDatabase.child("Users").child("Owners").child(mAuth.getCurrentUser().getUid()).child("currentLocation").setValue(loc);
                                    break;
                                }
                            }
                        }
                    }
                },
                Looper.myLooper());
    }


    @Override
    public void onTaskDone(Object... values) {
        if(mCurrentPolyline!=null){
            mCurrentPolyline.remove();
        }
        mCurrentPolyline = mMap.addPolyline((PolylineOptions) values[0]);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(t.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }

    private void geolocate() {
        Log.d(TAG,"geoLocate: geoLocating");
        String searchString = mSearchText.getText().toString();
        Geocoder geocoder = new Geocoder(MapActivityNew.this);
        List<Address> list = new ArrayList<>();

        try{
            list = geocoder.getFromLocationName(searchString,1);
        }
        catch (IOException e) {
            Log.e(TAG,"geoLocate: IOException"+ e.getMessage());
        }

        if(list.size()>0){
            Address address = list.get(0);

            Log.d(TAG,"geoLocate: found a location : "+ address.toString());
            //
            moveCamera(new LatLng(address.getLatitude(),address.getLongitude()),DEFAULT_ZOOM,address.getAddressLine(0));
        }
    }

    private void init(){
        Log.d(TAG,"init: initializing");

        mGoogleApiClient = Places.getGeoDataClient(this,null);
        mSearchText.setOnItemClickListener(mAutoCompleteClickListener);

        mPlaceAutocompleteAdapter = new PlaceAutocompleteAdapter( this,mGoogleApiClient,LAT_LNG_BOUNDS,null);
        mSearchText.setAdapter(mPlaceAutocompleteAdapter);
        mSearchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {

                if(actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || keyEvent.getAction() == KeyEvent.ACTION_DOWN
                        || keyEvent.getAction() == KeyEvent.KEYCODE_ENTER){

                    //execute method for searching
                    geolocate();
                }

                return false;
            }

        });

        mGps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG,"onClick: Clicked gps icon");
                getLDeviceLocation();
            }
        });

        mInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                   if(mMarker.isInfoWindowShown()){
                       mMarker.hideInfoWindow();
                   }
                   else
                   {
                       mMarker.showInfoWindow();
                   }
                }
                catch (NullPointerException ex){

                }
            }
        });

        hideSoftKeyboard();
    }

    private void initMap(){
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(MapActivityNew.this);
    }

    private void getLocationPermission(){
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION};

        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){


            if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                mLocationPermissionGranted =true;
                initMap();
            }
            else{
                ActivityCompat.requestPermissions(this,
                        permissions,
                        LOCATION_PERMISSION_REQUEST_CODE);
            }


        }
        else{
            ActivityCompat.requestPermissions(this,
                    permissions,
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }


    private void getLDeviceLocation(){
        Log.d(TAG,"getDeviceLocation: getting the devices current location");
        mFusedLocationProviderClient = getFusedLocationProviderClient(this);

        try{
            if(mLocationPermissionGranted){
                Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()){
                            Log.d(TAG,"onComplete: found location !");
                            Location currentLocation = (Location) task.getResult();
                            mCurrentLocation = currentLocation;
                            moveCamera( new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude()),DEFAULT_ZOOM,"My location");
                        }
                        else{
                            Log.d(TAG,"onComplete: current location is null");
                            Toast.makeText(MapActivityNew.this,"Unable to get Current location",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

        }catch (SecurityException e){
            Log.e(TAG,"getLDeviceLocation: SecurityException: "+e.getMessage());
        }

    }

    private void moveCamera(LatLng latLng, float zoom,PlaceInfo placeinfo){
        Log.d(TAG,"moveCamer: moving the camera to: lat:"+latLng.latitude+" long:"+latLng.longitude);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,zoom));

        mMap.clear();
        if(placeinfo !=null){
            try{
                String snippet = "Address" + placeinfo.getAddress() +"\n"
                        +"Phone Number" + placeinfo.getPhonenumber() +"\n"
                        +"Website" + placeinfo.getWebSite() +"\n"
                        +"Rating" + placeinfo.getRating() +"\n";

                MarkerOptions options  = new MarkerOptions()
                        .position(latLng)
                        .title(placeinfo.getName())
                        .snippet(snippet);
                mMarker = mMap.addMarker(options);

                MarkerOptions options1 = new MarkerOptions()
                        .position(new LatLng(mCurrentLocation.getLatitude(),mCurrentLocation.getLongitude()))
                        .title("My Location");
                mMap.addMarker(options1);

                showDirections(new LatLng(mCurrentLocation.getLatitude(),mCurrentLocation.getLongitude())
                        ,latLng
                        , "driving");

            }
            catch (NullPointerException ex){
                Log.e(TAG,"moveCamera: NullPointerException "+ex.getMessage());
            }

        }else
        {
            mMap.addMarker(new MarkerOptions().position(latLng));
        }
        hideSoftKeyboard();
    }

    private void moveCamera(LatLng latLng, float zoom,String title){
        Log.d(TAG,"moveCamer: moving the camera to: lat:"+latLng.latitude+" long:"+latLng.longitude);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,zoom));

        if(title != "My location"){

            MarkerOptions options = new MarkerOptions()
                    .position(latLng)
                    .title(title);
            mMap.addMarker(options);
        }

        hideSoftKeyboard();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;

        switch(requestCode){
            case LOCATION_PERMISSION_REQUEST_CODE:{
                if(grantResults.length>0){
                    for(int i=0;i< grantResults.length;i++){
                        if(grantResults[i] != PackageManager.PERMISSION_GRANTED){
                            mLocationPermissionGranted = false;
                            return;
                        }
                    }
                    mLocationPermissionGranted = true;
                    initMap();
                }
            }
        }
    }

private void hideSoftKeyboard() {
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private AdapterView.OnItemClickListener mAutoCompleteClickListener  = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            hideSoftKeyboard();
            final AutocompletePrediction item = mPlaceAutocompleteAdapter.getItem(i);
            final String placeId = item.getPlaceId();

            Task<PlaceBufferResponse> placeResult = mGoogleApiClient.getPlaceById(placeId);
            placeResult.addOnCompleteListener(mUpdatePlaceDetailsCallback);

        }
    };

    private OnCompleteListener<PlaceBufferResponse> mUpdatePlaceDetailsCallback
            = new OnCompleteListener<PlaceBufferResponse>() {
        @Override
        public void onComplete(Task<PlaceBufferResponse> task) {
            try {
                PlaceBufferResponse places = task.getResult();

                // Get the Place object from the buffer.
                final Place place = places.get(0);

                try{
                    mPlace = new PlaceInfo() ;
                    mPlace.setAddress(place.getAddress().toString());
                    mPlace.setName(place.getName().toString());
                    mPlace.setPhonenumber(place.getPhoneNumber().toString());
                    mPlace.setLatlng(place.getLatLng());
                    mPlace.setRating(place.getRating());
                    mPlace.setWebSite(place.getWebsiteUri());
                    mPlace.setId(place.getId());

                }catch (NullPointerException ex){
                    Log.e(TAG,"onComplete: NullPointerException " + ex.getMessage());
                }

                moveCamera(new LatLng(place.getViewport().getCenter().latitude,
                        place.getViewport().getCenter().longitude),DEFAULT_ZOOM,mPlace);
                places.release();
            } catch (RuntimeRemoteException e) {

                Log.e(TAG, "Place query did not complete.", e);

                return;
            }
        }
    };

    private void showDirections(LatLng origin,LatLng destination, String travelMode){
        String url = getDirectionsUrl(origin,destination,travelMode);
        new FetchURL(MapActivityNew.this).execute(url,"driving");
    }

    private String getDirectionsUrl(LatLng origin,LatLng destination, String travelMode ){

        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        String str_dest = "destination=" + destination.latitude + "," + destination.longitude ;
        String mode = "mode="+travelMode;
        String params  = str_origin+"&"+str_dest+"&"+mode;
        String output = "json";

        String url =  "https://maps.googleapis.com/maps/api/directions/"+output+"?"+params+"&key="+BuildConfig.ApiKey;

        return url;
    }

    @Override
    public void onBackPressed() {
        if(mAuth.getCurrentUser() == null ){
            Intent newActivityLoad = new Intent(MapActivityNew.this,Home.class);
            startActivity(newActivityLoad);
        }
        else {
            Intent startMain = new Intent(Intent.ACTION_MAIN);
            startMain.addCategory(Intent.CATEGORY_HOME);
            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(startMain);
        }

    }

    final static List<DriverMarkers> nearbyDrivers = new ArrayList<>();

    private void getNearbyBuses() {

        final DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Users/Drivers");

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Log.d(TAG, "1 nearbyDrivers_array_length: "+nearbyDrivers.size());
                String  uid_that ;
                String uid_this ;

                for (DataSnapshot driver : dataSnapshot.getChildren()) {

                    //check distance between each driver and my current location
                    double lat = Double.parseDouble(driver.child("currentLocation").child("lat").getValue().toString());
                    double lon = Double.parseDouble(driver.child("currentLocation").child("lon").getValue().toString());
                    LatLng Driverlatlng = new LatLng(lat, lon);
                    LatLng mylatlng = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
                    double dist = DriverMarkers.getStraightLineDistance(mylatlng, Driverlatlng);
                    LatLng llX = new LatLng(lat,lon);

                    if (dist < 200) {
                        //check nearbyDrivers whether this driver exist or not
                       // Log.d(TAG, "2 nearby driver : "+driver.child("uid").getValue().toString().trim());

                        Boolean driver_exist_in_nearbyDrivers = false;
                         uid_that = driver.child("uid").getValue().toString().trim();

                        if(nearbyDrivers.size()>0){
                            for(DriverMarkers driver_marker : nearbyDrivers){
                                uid_this = driver_marker.getD().getUid().trim();
                               // Log.d(TAG, "3 driver_marker loop = "+ driver_marker.getD().getUid().trim());
                                if(uid_this.equals(uid_that) ){
                                    driver_exist_in_nearbyDrivers = true;
                                   // Log.d(TAG, "uid_this == uid_that");
                                    //update existing drivers marker
                                    LatLng startPosition= driver_marker.getM().getPosition();
                                    LatLng endPosition = llX;
                                    driver_marker.animator(startPosition,endPosition,driver_marker.getM(),mMap);
                                    break;
                                }
                            }
                        }

                        if(!driver_exist_in_nearbyDrivers){
                            //add driver to nearbyDrivers list
                            Marker markerX = mMap.addMarker(new MarkerOptions().position(llX)
                                    .flat(true)
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_car)));
                            nearbyDrivers.add(new DriverMarkers(markerX,driver.getValue(Driver.class)));
                        }

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, "databaseError : " + databaseError.getMessage());
            }
        });

    }

    private void updateBusLocations(){

        final DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Users/Drivers/");

        userRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                getNearbyBuses();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        }




}

