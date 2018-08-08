package com.example.manug.indianrailenquiry;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class PNRAdapter extends ArrayAdapter<PNR> {
   public PNRAdapter(Activity context, ArrayList<PNR> arr){
       super(context,0,arr);
   }
   @NonNull
   @Override
    public View getView(int position, View convertView,ViewGroup parent) {
       //check if there is an existing list item view called convertView
       View listItemView = convertView;
       if (listItemView == null) {
           listItemView = LayoutInflater.from(getContext()).inflate(R.layout.passenger_list,parent,false);
       }
       PNR currentPNR=getItem(position);
       TextView current=listItemView.findViewById(R.id.current_status_textView);
       TextView booking=listItemView.findViewById(R.id.booking_status_textView);
       TextView number=listItemView.findViewById(R.id.pass_no);
       current.setText(currentPNR.getCurrent_state());
       booking.setText(currentPNR.getBooking_state());
       number.setText(currentPNR.getNumber());
       return listItemView;
   }
}
