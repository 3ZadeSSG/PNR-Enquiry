package com.example.manug.indianrailenquiry;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class pnr_view extends AppCompatActivity {
    PNRAdapter mAdapter;
    ArrayList<PNR> Passengers;
    String pnr_response=null;
    ListView passList;
    TextView pnr_view;
    TextView train_name_view;
    TextView doj_view;
    TextView chart_view;
    TextView total_passengers_view;
    TextView boarding_point_view;
    TextView reservation_upto_view;
    TextView journey_class_view;

    /*All informations that will be extracted from the response are as follows*/
    String doj=null;
    String pnr;
    String total_passengers;
    String chart_prepared;
    String from_station;
    String to_station;
    String boarding_point;
    String reservation_upto;
    String train_name;
    String train_number;
    String journey_class;
    String current_status;
    String booking_status;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pnr_view);
        Intent i=getIntent();
        pnr_response=i.getStringExtra("response");
        passList=findViewById(R.id.passenger_list_view);
        Passengers=new ArrayList<>();
        /*Initialize add views on create of the activity*/
        pnr_view=findViewById(R.id.pnr_TextView);
            train_name_view = findViewById(R.id.train_name_TextView);
            doj_view = findViewById(R.id.date_TextView);
            chart_view = findViewById(R.id.chart_prepared_TextView);
            total_passengers_view = findViewById(R.id.total_passengers_TextView);
            boarding_point_view = findViewById(R.id.boarding_point_TextView);
            reservation_upto_view = findViewById(R.id.reservation_upto_TextView);
            journey_class_view = findViewById(R.id.class_TextView);
            extractInformation();
            updateView();
    }
    private void updateView(){
        pnr_view.setText(getString(R.string.pnr_number)+pnr);
        train_name_view.setText(train_name+" "+train_number);
        doj_view.setText(getString(R.string.date)+doj);
        if(chart_prepared.equals("false")){
            chart_view.setText(getString(R.string.chart_not_prepared));
        }
        else{
            chart_view.setText(getString(R.string.chart_prepared));
        }
        //chart_view.setText("Chart prepared: "+chart_prepared);
        total_passengers_view.setText(getString(R.string.total_passengers)+total_passengers);
        boarding_point_view.setText(getString(R.string.from)+boarding_point);
        reservation_upto_view.setText(getString(R.string.to)+reservation_upto);
        journey_class_view.setText(getString(R.string.Class)+journey_class);
        mAdapter=new PNRAdapter(this,Passengers);
        passList.setAdapter(mAdapter); //view list of passengers with their respective status
    }
    private void extractInformation(){
        try {
            JSONObject root = new JSONObject(pnr_response);
            doj=root.getString("doj"); //extract date of journey
            pnr=root.getString("pnr"); //extract the prn number
            total_passengers=root.getString("total_passengers");    //total passengers count
            chart_prepared=root.getString("chart_prepared");    //chart status

            JSONObject station=root.getJSONObject("from_station");
            from_station=station.getString("name");
            from_station=from_station+" ("+station.getString("code")+" )";  //from_station

            station=root.getJSONObject("to_station");
            to_station=station.getString("name");
            to_station=to_station+" ("+station.getString("code")+")";  //to_station

            station=root.getJSONObject("boarding_point");
            boarding_point=station.getString("name");
            boarding_point=boarding_point+" ("+station.getString("code")+")";

            station=root.getJSONObject("reservation_upto");
            reservation_upto=station.getString("name");
            reservation_upto=reservation_upto+" ("+station.getString("code")+")";

            station=root.getJSONObject("train");
            train_name=station.getString("name");
            train_number=station.getString("number");

            station=root.getJSONObject("journey_class");
            if(!station.getString("name").equals("null")){
                journey_class=station.getString("name");
                journey_class=journey_class+" ("+station.getString("code")+")";
            }
            else{
                journey_class=station.getString("code");
            }
            JSONArray passengersArray=root.getJSONArray("passengers");
            for(int i=0;i<passengersArray.length();i++){
                station=passengersArray.getJSONObject(i);
                booking_status=station.getString("booking_status");
                current_status=station.getString("current_status");
                Passengers.add(new PNR(String.valueOf(i+1),current_status,booking_status));
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}
