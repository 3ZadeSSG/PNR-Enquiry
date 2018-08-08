package com.example.manug.indianrailenquiry;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class PNR_Error extends AppCompatActivity {
    String pnr_response;
    TextView result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pnr__error);
        Intent i=getIntent();
        pnr_response=i.getStringExtra("response");
        result=findViewById(R.id.result_TextView);
        result.setText(pnr_response);
    }
    public void done(View view){
        this.finish();
    }
}
