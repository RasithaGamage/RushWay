package com.example.rushan.RushWay;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.example.rushan.RushWay.models.RWLocation;
import com.example.rushan.RushWay.models.Vehicle;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MyAsyncTask  extends AsyncTask<LatLng, String, String> {

    Context context;
    Handler handler;
    List<String> busNumberArr = new ArrayList<String>();


    List<String> fromArr = new ArrayList<String>();

    MyAsyncTask (Context ctx,Handler handler){
        context =  ctx;
        this.handler = handler;
    }

    @Override
    protected String doInBackground(LatLng... params) {

        busNumberArr.add("DEFAULT");
        fromArr.add("DEFAULT");

        final LatLng origin = params[0];
        LatLng destination =  params[1];

        final RWLocation start = new RWLocation(Double.doubleToLongBits(origin.latitude) ,Double.doubleToLongBits(origin.longitude));
        final RWLocation end = new RWLocation(Double.doubleToLongBits(destination.latitude),Double.doubleToLongBits(destination.longitude));

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
                    int counter = 0;

                    for (RWLocation loc:path) {
                        counter++;
                        if(isNear(loc,start,500)){
                            int isExist=0;
                            for (Vehicle a:match_start_bus) {
                                if( a.getBusId()== bus.getBusId()){
                                    isExist =1;
                                }
                            }
                            if(isExist==0){
                                Log.d("match_start_bus loc", Double.toString(loc.getLat())+","+ Double.toString(loc.getLon()));
                                Log.d("match_start_bus start",Double.toString(start.getLat())+","+ Double.toString(start.getLon()));
                                Log.d("match_start_bus", bus.getRoute().getNo());
                                match_start_bus.add(bus);
                            }
                        }
                        if(counter>0){
                            if(isNear(loc,end,500)){
                                int isExist=0;
                                for (Vehicle a:match_end_bus) {
                                    if( a.getBusId()== bus.getBusId()){
                                        isExist =1;
                                    }
                                }
                                if(isExist==0){
                                    Log.d("match_end_bus", bus.getRoute().getNo());
                                    match_end_bus.add(bus);
                                }
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
                        String[] ending_bus_route_arr = ending_bus_route.split("\\},\\{");

                        for (String s: starting_bus_route_arr) {
                            //convert s into a LatLng
                            RWLocation s_lt = new RWLocation(Long.parseLong(s.split(",")[0].substring(4)),Long.parseLong(s.split(",")[1].substring(4)));
//                            for(String x:ending_bus_route_arr){
//                                RWLocation x_lt = new RWLocation(Double.parseDouble(x.split(",")[0].substring(4)),Double.parseDouble(x.split(",")[1].substring(4)));
//                                //convert x into LatLng
//
//                                if(isNear(x_lt,s_lt,500)){
//
//                                    int isExist =  0 ;
//
//                                    for (List<Vehicle> v:sol_bus) {
//                                        if((v.get(0).getBusId()==starting_bus.getBusId() && v.get(1).getBusId()==ending_bus.getBusId()) ||
//                                                ( v.get(0).getBusId()==ending_bus.getBusId() && v.get(1).getBusId()==starting_bus.getBusId())){
//                                            isExist = 1;
//                                        }
//                                    }
//                                    if(isExist==0){
//                                        Log.d("sol", starting_bus.getRoute().getNo()+":"+ending_bus.getRoute().getNo() );
//                                        List<Vehicle> sol = new ArrayList<>();
//                                        sol.add(starting_bus);
//                                        sol.add(ending_bus);
//                                        sol_bus.add(sol);
//                                    }
//                                }
//
//                            }

                            if( ending_bus_route.contains(s)){

                                int isExist =  0 ;

                                for (List<Vehicle> v:sol_bus) {

//                                    Log.d("xxx",v.get(0).getRoute().getNo()+":"+starting_bus.getRoute().getNo()+
//                                            ":"+v.get(1).getRoute().getNo()+":"+ending_bus.getRoute().getNo()+":"+
//                                            v.get(0).getRoute().getNo()+":"+ending_bus.getRoute().getNo()+":"+
//                                            v.get(1).getRoute().getNo()+":"+starting_bus.getRoute().getNo());

                                    if((v.get(0).getRoute().getNo().equals(starting_bus.getRoute().getNo())
                                     && v.get(1).getRoute().getNo().equals(ending_bus.getRoute().getNo())) ||
                                      ( v.get(0).getRoute().getNo().equals(ending_bus.getRoute().getNo())
                                     && v.get(1).getRoute().getNo().equals(starting_bus.getRoute().getNo())))
                                    { //Log.d("isExist","1");
                                        isExist = 1;
                                    }
                                }
                                if(isExist==0){
                                    Log.d("sol",starting_bus.getRoute().getNo()+":"+ending_bus.getRoute().getNo() );
                                    List<Vehicle> sol = new ArrayList<>();
                                    sol.add(starting_bus);
                                    sol.add(ending_bus);
                                    sol_bus.add(sol);
                                }
                            }
                        }
                    }
                }
                busNumberArr.clear();
                fromArr.clear();

                for (List<Vehicle> sss:sol_bus) {
                    String setBusName1 = sss.get(0).getRoute().getNo()+" "+sss.get(0).getRoute().getDestination1()+"-"+sss.get(0).getRoute().getDestination2();
                    String setBusName2 = sss.get(1).getRoute().getNo()+" "+sss.get(1).getRoute().getDestination1()+"-"+sss.get(1).getRoute().getDestination2();

                    if(sss.get(0).getRoute().getNo()==sss.get(1).getRoute().getNo()) {
//                        busNumberArr.add(setBusName1);
//                        fromArr.add(origin.toString());
                        busNumberArr.add(setBusName1);

                        String name = null;

                        try {
                            name = "from "+ getLocName(new LatLng(origin.latitude,origin.longitude)).split(",")[0];
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        fromArr.add(name);

                    }

                    else if(true)
                    {
                        busNumberArr.add(setBusName1+" and "+setBusName2);
                        //if origin location is on route the show the origin location address
                        //else show the busroute starting location

                        String name = "";

                        try {
                            name = "from "+getLocName(new LatLng(origin.latitude,origin.longitude)).split(",")[0]+" and "+sss.get(1).getRoute().getDestination1();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        fromArr.add(name);

                    }
                }
                showPopup_bus();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("error DatabaseError", "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };

        userRef.addListenerForSingleValueEvent(postListener);
//        Log.d("getBusId:","XXXXX111");
//        List<List<Vehicle>> xss = getRoutesToDestination(new RWLocation(origin.latitude,origin.longitude),new RWLocation(destination.latitude,destination.longitude));
//        Log.d("getBusId:","XXXXX222");
//
//       while(true){
//          if(xss.size()>0){
//              break;
//          }
//       }
//
//        for(  List<Vehicle> z :xss)
//        {
//            Log.d("getBusId:","XXXXX333");
//            Log.d("getBusId: ",""+z.get(0).getRoute().getNo());
//        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

    }

    //    public List<List<Vehicle>> getRoutesToDestination(final RWLocation start, final RWLocation end ){
//        //get starting locaion
//        //get destination location
//
//        //search starting location of all routes <---- 1
//        //get all bus routes in to list
//
//
//        final List<Vehicle> busList = new ArrayList<>(); //get data from firebase to populate the array
//        final List<Vehicle> match_start_bus = new ArrayList<>();
//        final List<Vehicle> match_end_bus = new ArrayList<>();
//        final List<List<Vehicle>> sol_bus = new ArrayList<>();
//
//        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Busses/");
//        final ValueEventListener postListener = new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for (DataSnapshot bus : dataSnapshot.getChildren()) {
//                    Vehicle bus1 = bus.getValue(Vehicle.class);
//                    busList.add(bus1);
//                }
//                for (Vehicle bus:busList) {
//                    List<RWLocation> path = bus.getRoute().getRoutePathObject();
////                RWLocation destination1 = path.get(0);
////                RWLocation destination2 = path.get(path.size());
//                    for (RWLocation loc:path) {
//                        if(isNear(loc,start,500)){
//                            int isExist=0;
//                            for (Vehicle a:match_start_bus) {
//                                if( a.getBusId()==bus.getBusId()){
//                                    isExist =1;
//                                }
//                            }
//                            if(isExist==0){
//                                match_start_bus.add(bus);
////                            Log.d("loc:path", Double.toString(loc.getLat())+","+Double.toString(loc.getLon()));
////                            Log.d("loc:start", Double.toString(start.getLat())+","+Double.toString(start.getLon()));
////                            Log.d("loc:end", Double.toString(end.getLat())+","+Double.toString(end.getLon()));
//                            }
//
//                        }
//                        if(isNear(loc,end,500)){
//                            int isExist=0;
//                            for (Vehicle a:match_end_bus) {
//                                if( a.getBusId()==bus.getBusId()){
//                                    isExist =1;
//                                }
//                            }
//                            if(isExist==0){
//                                match_end_bus.add(bus);
////                            Log.d("loc:path", Double.toString(loc.getLat())+","+Double.toString(loc.getLon()));
////                            Log.d("loc:start", Double.toString(start.getLat())+","+Double.toString(start.getLon()));
////                            Log.d("loc:end", Double.toString(end.getLat())+","+Double.toString(end.getLon()));
//                            }
//                        }
//                    }
//                }
//                //this algorithm works only for two/one bus destination
//                for (Vehicle starting_bus:match_start_bus) {
//                    String starting_bus_route = starting_bus.getRoute().getRoutePath();
//                    //turn this string in to array
//                    starting_bus_route = starting_bus_route.substring(4,(starting_bus_route.length()-4)).replaceAll("\\s+","");
//                    String[] starting_bus_route_arr = starting_bus_route.split("\\},\\{");
//
//                    for (Vehicle ending_bus:match_end_bus) {
//
//                        String ending_bus_route = ending_bus.getRoute().getRoutePath();
//                        //turn this string in to array
//                        ending_bus_route = ending_bus_route.substring(4,(ending_bus_route.length()-4)).replaceAll("\\s+","");
////                        String[] ending_bus_route_arr = ending_bus_route.split("\\},\\{");
//
//                        for (String s: starting_bus_route_arr) {
//                            if( ending_bus_route.contains(s)){
//                                int isExist =  0 ;
//                                for (List<Vehicle> v:sol_bus) {
//                                    if((v.get(0).getBusId()==starting_bus.getBusId() && v.get(1).getBusId()==ending_bus.getBusId()) ||
//                                            ( v.get(0).getBusId()==ending_bus.getBusId() && v.get(1).getBusId()==starting_bus.getBusId())){
//                                        isExist = 1;
//                                    }
//                                }
//
//                                if(isExist==0){
//                                    Log.d("sol", starting_bus.getBusId()+":"+ending_bus.getBusId() );
//                                    List<Vehicle> sol = new ArrayList<>();
//                                    sol.add(starting_bus);
//                                    sol.add(ending_bus);
//                                    sol_bus.add(sol);
//                                }
//                             }
//                        }
//
////                        List<RWLocation> x = starting_bus.getRoute().getRoutePathObject();
////                        for(RWLocation loc1:x){
////                            List<RWLocation> y =ending_bus.getRoute().getRoutePathObject();
//////                            Log.d("sol", ">>>>>>>>>>>>>>>>>>>>>>>>>> ");
////                            for(RWLocation loc2:y){
////                                if(loc1.getLon()==loc2.getLon() && loc1.getLat()==loc2.getLat()){
////                                    List<Vehicle> sol = new ArrayList<>();
////                                    sol.add(starting_bus);
////                                    sol.add(ending_bus);
////                                    sol_bus.add(sol);
//////                                    Log.d("sol", ">>>>>>>>>>>>>>>>>>>>>>>>>> "+sol.get(0).getBusId());
////                                     //sol_bus has the list of matching busses
////                                }
////                            }
////                        }
//                    }
//                }
//            }
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                // Getting Post failed, log a message
//                Log.w("error DatabaseError", "loadPost:onCancelled", databaseError.toException());
//                // ...
//            }
//        };
//        userRef.addListenerForSingleValueEvent(postListener);
//
//        return sol_bus;
//
//    }
//

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

    public  void showPopup_bus(){

        final Dialog dialog_bus = new Dialog(context);
        dialog_bus.setContentView(R.layout.custompopup2);

        RelativeLayout popuplayout = dialog_bus.findViewById(R.id.popuplayout2);
        Button close_dialog = dialog_bus.findViewById(R.id.btn_pop_close);
        Button see_more_button = dialog_bus.findViewById(R.id.see_more);
//        final DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Busses/");
//
//        final ValueEventListener postListener = new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                // Get Post object and use the values to update the UI
//                for (DataSnapshot br : dataSnapshot.getChildren()) {
//                    Vehicle busRoutes = br.getValue(Vehicle.class);
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                // Getting Post failed, log a message
//                Log.w("loadPost", "loadPost:onCancelled", databaseError.toException());
//                // ...
//            }
//        };
//        userRef.addListenerForSingleValueEvent(postListener);

        ListView list_bus = (ListView) dialog_bus.findViewById(R.id.list_bus);

        CustomList listAdapter = new CustomList((Activity) context,busNumberArr,fromArr);
        list_bus.setAdapter(listAdapter);

        close_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog_bus.dismiss();
            }
        });
        dialog_bus.show();

        see_more_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog_bus.dismiss();

                handler.post(new Runnable() {

                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void run() {
                        handler.sendEmptyMessage(1);
                    }
                });
            }


        });
    }


    private String getLocName(LatLng loc) throws IOException {

        Geocoder geocoder = new Geocoder(context);
        List<Address> address = new ArrayList<>();
        address = geocoder.getFromLocation(loc.latitude,loc.longitude,1);
        return  address.get(0).getAddressLine(0);
    }


}
