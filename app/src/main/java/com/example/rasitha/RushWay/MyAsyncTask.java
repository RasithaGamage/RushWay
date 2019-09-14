package com.example.rasitha.RushWay;

import android.os.AsyncTask;
import android.util.Log;

import com.example.rasitha.RushWay.models.RWLocation;
import com.example.rasitha.RushWay.models.Vehicle;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MyAsyncTask  extends AsyncTask<LatLng, String, String> {{
}

    @Override
    protected String doInBackground(LatLng... params) {

        LatLng origin = params[0];
        LatLng destination =  params[1];
        List<List<Vehicle>> xss = getRoutesToDestination(new RWLocation(origin.latitude,origin.longitude),new RWLocation(destination.latitude,destination.longitude));

        for(  List<Vehicle> z :xss)
        {
            Log.d("getBusId: ",""+z.get(0).getBusId());
        }

        return null;
    }

    public List<List<Vehicle>> getRoutesToDestination(final RWLocation start, final RWLocation end ){
        //get starting locaion
        //get destination location

        //search starting location of all routes <---- 1
        //get all bus routes in to list


        final List<Vehicle> busList = new ArrayList<>(); //get data from firebase to populate the array
        final List<Vehicle> match_start_bus = new ArrayList<>();
        final List<Vehicle> match_end_bus = new ArrayList<>();
        final List<List<Vehicle>> sol_bus = new ArrayList<>();

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Busses/");
        final ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot bus : dataSnapshot.getChildren()) {
                    Vehicle bus1 = bus.getValue(Vehicle.class);
                    busList.add(bus1);
                }
                for (Vehicle bus:busList) {
                    List<RWLocation> path = bus.getRoute().getRoutePathObject();
//                RWLocation destination1 = path.get(0);
//                RWLocation destination2 = path.get(path.size());
                    for (RWLocation loc:path) {
                        if(isNear(loc,start,500)){
                            int isExist=0;
                            for (Vehicle a:match_start_bus) {
                                if( a.getBusId()==bus.getBusId()){
                                    isExist =1;
                                }
                            }
                            if(isExist==0){
                                match_start_bus.add(bus);
//                            Log.d("loc:path", Double.toString(loc.getLat())+","+Double.toString(loc.getLon()));
//                            Log.d("loc:start", Double.toString(start.getLat())+","+Double.toString(start.getLon()));
//                            Log.d("loc:end", Double.toString(end.getLat())+","+Double.toString(end.getLon()));
                            }

                        }
                        if(isNear(loc,end,500)){
                            int isExist=0;
                            for (Vehicle a:match_end_bus) {
                                if( a.getBusId()==bus.getBusId()){
                                    isExist =1;
                                }
                            }
                            if(isExist==0){
                                match_end_bus.add(bus);
//                            Log.d("loc:path", Double.toString(loc.getLat())+","+Double.toString(loc.getLon()));
//                            Log.d("loc:start", Double.toString(start.getLat())+","+Double.toString(start.getLon()));
//                            Log.d("loc:end", Double.toString(end.getLat())+","+Double.toString(end.getLon()));
                            }
                        }
                    }
                }
                //this algorithm works only for two/one bus destination
                for (Vehicle starting_bus:match_start_bus) {
                    String starting_bus_route = starting_bus.getRoute().getRoutePath();
                    //turn this string in to array
                    starting_bus_route = starting_bus_route.substring(4,(starting_bus_route.length()-4)).replaceAll("\\s+","");
                    String[] starting_bus_route_arr = starting_bus_route.split("\\},\\{");

                    for (Vehicle ending_bus:match_end_bus) {

                        String ending_bus_route = ending_bus.getRoute().getRoutePath();
                        //turn this string in to array
                        ending_bus_route = ending_bus_route.substring(4,(ending_bus_route.length()-4)).replaceAll("\\s+","");
//                        String[] ending_bus_route_arr = ending_bus_route.split("\\},\\{");

                        for (String s: starting_bus_route_arr) {
                            if( ending_bus_route.contains(s)){
                                int isExist =  0 ;
                                for (List<Vehicle> v:sol_bus) {
                                    if((v.get(0).getBusId()==starting_bus.getBusId() && v.get(1).getBusId()==ending_bus.getBusId()) ||
                                            ( v.get(0).getBusId()==ending_bus.getBusId() && v.get(1).getBusId()==starting_bus.getBusId())){
                                        isExist = 1;
                                    }
                                }

                                if(isExist==0){
                                    Log.d("sol", starting_bus.getBusId()+":"+ending_bus.getBusId() );
                                    List<Vehicle> sol = new ArrayList<>();
                                    sol.add(starting_bus);
                                    sol.add(ending_bus);
                                    sol_bus.add(sol);
                                }
                             }
                        }

//                        List<RWLocation> x = starting_bus.getRoute().getRoutePathObject();
//                        for(RWLocation loc1:x){
//                            List<RWLocation> y =ending_bus.getRoute().getRoutePathObject();
////                            Log.d("sol", ">>>>>>>>>>>>>>>>>>>>>>>>>> ");
//                            for(RWLocation loc2:y){
//                                if(loc1.getLon()==loc2.getLon() && loc1.getLat()==loc2.getLat()){
//                                    List<Vehicle> sol = new ArrayList<>();
//                                    sol.add(starting_bus);
//                                    sol.add(ending_bus);
//                                    sol_bus.add(sol);
////                                    Log.d("sol", ">>>>>>>>>>>>>>>>>>>>>>>>>> "+sol.get(0).getBusId());
//                                     //sol_bus has the list of matching busses
//                                }
//                            }
//                        }
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("error DatabaseError", "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        userRef.addListenerForSingleValueEvent(postListener);

        return sol_bus;

    }


    private static final int EARTH_RADIUS = 6371; // Approx Earth radius in KM
    public static double haversin(double val) {
        return Math.pow(Math.sin(val / 2), 2);
    }

    //compare to locations
    public boolean isNear(RWLocation loc1, RWLocation loc2, double rangeInMeters){
        double dist = getStraightLineDistance(new LatLng(loc1.getLat(),loc1.getLon()),
                new LatLng(loc2.getLat(),loc2.getLon()));

        if(dist<=(rangeInMeters/1000)){
            return true;
        }
        else {
            return false;
        }
    }

    public static double getStraightLineDistance(LatLng latlang1, LatLng latlang2) {

        double dLat  = Math.toRadians((latlang2.latitude - latlang1.latitude));
        double dLong = Math.toRadians((latlang2.longitude - latlang1.longitude));

        double startLat = Math.toRadians(latlang1.latitude);
        double endLat   = Math.toRadians(latlang2.latitude);

        double a = haversin(dLat) + Math.cos(startLat) * Math.cos(endLat) * haversin(dLong);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS * c; // <-- d
    }
}
