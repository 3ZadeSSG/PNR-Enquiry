package com.example.manug.indianrailenquiry;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.ybq.android.spinkit.style.CubeGrid;
import com.github.ybq.android.spinkit.style.DoubleBounce;

import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {
   String APIKEY;
   String address;
   String address2;
    EditText inputPNR;
    ProgressBar progressBar;
    LinearLayout details;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inputPNR=findViewById(R.id.PNR_Edit_text);
        details=findViewById(R.id.details);
        progressBar = findViewById(R.id.spin_kit);
        CubeGrid doubleBounce=new CubeGrid();
        progressBar.setIndeterminateDrawable(doubleBounce);
    }
    public void checkPNR(View view){
        String pnr=inputPNR.getText().toString();
        if(pnr.length()==10){
            PNRAsyncTask task=new PNRAsyncTask();
            String url=address;
            url=url+pnr;
            url=url+address2;
            task.execute(url);
        }
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    public class PNRAsyncTask extends AsyncTask<String,Void,String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
            details.setVisibility(View.INVISIBLE);
        }
        @Override
        protected String doInBackground(String... strings) {
            if(isNetworkAvailable()) {
                String res = "str";
                try {
                    URL url = new URL(strings[0]);
                    InputStream inputStream;
                    HttpsURLConnection pnrConnection = (HttpsURLConnection) url.openConnection();
                    pnrConnection.setReadTimeout(20000);
                    pnrConnection.setConnectTimeout(10000);
                    pnrConnection.setRequestMethod("GET");
                    pnrConnection.connect();
                    if (pnrConnection.getResponseCode() == 200) {
                        inputStream = pnrConnection.getInputStream();
                        res = readFromStream(inputStream);
                    }
                    pnrConnection.disconnect();
                    return res;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return "Error";
        }
        @Override
        protected void onPostExecute(String result){
            doActions(result);
            progressBar.setVisibility(View.INVISIBLE);
            details.setVisibility(View.VISIBLE);
        }
        private String readFromStream(InputStream inputStream) throws IOException {
            StringBuilder output = new StringBuilder();
            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
                BufferedReader reader = new BufferedReader(inputStreamReader);
                String line = reader.readLine();
                while (line != null) {
                    output.append(line);
                    line = reader.readLine();
                }
            }
            return output.toString();
        }
    }
    public void doActions(String result){
        if(!result.equals("Error")){
            try {
                JSONObject root = new JSONObject(result);
                String doj=root.getString("doj");
                if(doj.equals("null")){
                    Intent viewError = new Intent(MainActivity.this, PNR_Error.class);
                    viewError.putExtra("response", getString(R.string.invalid_pnr_number));
                    startActivity(viewError);
                }
                else{
                    Intent viewPNR = new Intent(MainActivity.this, pnr_view.class);
                    viewPNR.putExtra("response",result);
                    startActivity(viewPNR);
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        else{
            Intent viewError = new Intent(MainActivity.this, PNR_Error.class);
            viewError.putExtra("response",getString(R.string.network_error));
            startActivity(viewError);
        }
    }
}
