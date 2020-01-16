package com.example.rushan.RushWay.models;
import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

public class BusRoute{

    private String destination1;
    private String destination2;
    private String no;
    private String routePath;

    public BusRoute() {

    }

    public BusRoute(String destination1, String destination2, String no,String routePath) {
        this.destination1 = destination1;
        this.destination2 = destination2;
        this.no = no;
        this.routePath = routePath;

        Log.e("busRoute","busRoute "+this.getRoutePathObject().get(2).getLat());
    }

    public String getDestination1() {
        return destination1;
    }

    public void setDestination1(String destination1) {
        this.destination1 = destination1;
    }

    public String getDestination2() {
        return destination2;
    }

    public void setDestination2(String destination2) {
        this.destination2 = destination2;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getRoutePath() {
        return routePath;
    }

    public void setRoutePath(String routePath) {
        this.routePath = routePath;
    }

    public List<RWLocation> getRoutePathObject() {
        List<RWLocation> path = new ArrayList<>();
        String x = this.routePath.substring(1,(this.routePath.length()-1)).replaceAll("\\s+","");
        x= x.replace("lat","\"lat\"");
        x= x.replace("lng","\"lng\"");
        x= x.replace("=",":");

//       Log.d("getBusId2: ",x);

//            String rrr = x.toString();
//            int maxLogSize = 1000;
//            for(int i = 0; i <= rrr.length() / maxLogSize; i++) {
//                int start = i * maxLogSize;
//                int end = (i+1) * maxLogSize;
//                end = end > rrr.length() ? rrr.length() : end;
//                Log.v("", rrr.substring(start, end));
//            }

        JsonParser jsonParser = new JsonParser();
        JsonArray jsonObject =  jsonParser.parse(x).getAsJsonArray();

        for (int i = 0; i < jsonObject.size(); i++) {
            double lat = jsonObject.get(i).getAsJsonObject().get("lat").getAsDouble();
            double lng = jsonObject.get(i).getAsJsonObject().get("lng").getAsDouble();
            RWLocation loc = new RWLocation(lat,lng);
            path.add(loc);
        }

        return path ;
    }

}