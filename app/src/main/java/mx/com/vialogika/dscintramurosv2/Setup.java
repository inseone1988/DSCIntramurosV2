package mx.com.vialogika.dscintramurosv2;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import mx.com.vialogika.dscintramurosv2.Network.NetworkOperations;
import mx.com.vialogika.dscintramurosv2.Utils.UserKeys;

public class Setup extends AppCompatActivity {

    private static final int REQUEST_PERMISSION_CODE = 150;
    private boolean hasAllPermissions = false;

    private ProgressBar pb;
    private TextView status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);
        getItems();
        hideAppBar();
        checkAppStatus();
        requestDelay();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length > 0){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED && grantResults[2] == PackageManager.PERMISSION_GRANTED ){
                hasAllPermissions = true;
            }else{
                new AlertDialog.Builder(this)
                        .setTitle(R.string.app_permissions)
                        .setMessage(R.string.app_permissions_denied_message)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })
                        .show();
            }
        }
    }

    private void getItems(){
        pb = findViewById(R.id.progressBar);
        status = findViewById(R.id.progress_text);
        status.setText("Getting objects...");
    }

    private void checkAppStatus(){
        if (!checkPermissions()){
            askPermissions();
        }
    }

    private boolean checkPermissions(){
        status.setText("Checking permissions...");
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) return false;
        status.setText("Checking permissions... INTERNET OK");
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) return false;
        status.setText("Checking permissions... EXTERNAL STORAGE OK");
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) return false;
        status.setText("Checking permissions... READ PHONE INFO OK");
        hasAllPermissions = true;
        status.setText("Permissions OK...");
        return true;
    }

    private String getImei(){
        String id = "";
        TelephonyManager manager = (TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);
        if (manager != null){
            try{
                id = manager.getDeviceId();
                if (!id.equals("")){
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString(UserKeys.SP_DEVICE_ID,id);
                    editor.apply();
                }
            }catch(SecurityException e){
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_PHONE_STATE},REQUEST_PERMISSION_CODE);
            }
        }
        return id;
    }

    private void askPermissions(){
        status.setText("Asking required permissions...");
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.INTERNET,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_PHONE_STATE},REQUEST_PERMISSION_CODE);
    }

    private boolean deviceAuthorized(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        return !preferences.getString(UserKeys.SP_API_KEY,"").equals("");
    }

    private void requestDelay(){
        status.setText("Waiting for app to finish load...");
        new CountDownTimer(3000,1000){
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                getDeviceData();
            }
        }.start();
    }

    private void hideAppBar(){
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
    }

    private void getDeviceData(){
        if (!deviceAuthorized()){
            status.setText("Device is not authorized yet...getting credentials...");
            final String imei = getImei();
            if (!imei.equals("")){
                //User has given permission to read device identifier so auth and download device data
                //First save it to sp
                NetworkOperations.getInstance().getApiKey(new NetworkOperations.SimpleNetworkCallback<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //Download device data
                        //If we got a succesful response then continue else device is not allowed to continue
                        status.setText("Reading response...");
                        try{
                            if (response.getBoolean("success")){
                                status.setText("Yay!!! they gave us that magic key...");
                                status.setText("Getting device data...");
                                NetworkOperations.getInstance().downloadDeviceData(new NetworkOperations.SimpleNetworkCallback<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        status.setText("Wait, got something...reading...");
                                        try{
                                            if (response.getBoolean("success")){
                                                //Data has been succesfully downloaded, now we can start
                                                startMainApp();
                                            }
                                        }catch(JSONException e) {
                                            status.setText("Sorry, response looks like spaguetti and I can't read it please give this text to the fool is programming me \n" + e.getMessage());
                                        }
                                    }

                                    @Override
                                    public void onVolleyError(JSONObject response, VolleyError error) {

                                    }
                                });
                            }else{
                                status.setText("Well, I think this must be a mistake but we aren't invited.\nPlease call the bouncer and give him this key :\n " + imei);
                            }
                        }catch(JSONException e){
                            status.setText(R.string.not_allowed_device);
                        }
                    }

                    @Override
                    public void onVolleyError(JSONObject response, VolleyError error) {

                    }
                });
            }else{
                status.setText("Couldn't retrieve phone info. Check all permissions has been granted.");
                askPermissions();
            }
        }else {
            //Data has been downloaded before go main view
            status.setText("Got credentials...rock and roll");
            startMainApp();
        }
    }

    private void startMainApp(){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);

    }
}
