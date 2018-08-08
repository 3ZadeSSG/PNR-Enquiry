package com.example.manug.indianrailenquiry;

public class PNR {
    String no;
    String current_state;
    String booking_state;
    public PNR(String no,String current_state,String booking_state){
        this.no=no;
        this.current_state=current_state;
        this.booking_state=booking_state;
    }
    public String getBooking_state(){
        return booking_state;
    }
    public String getCurrent_state(){
        return current_state;
    }
    public String getNumber(){
        return no;
    }
}
