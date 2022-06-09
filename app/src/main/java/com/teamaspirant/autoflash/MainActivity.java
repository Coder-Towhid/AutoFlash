package com.teamaspirant.autoflash;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.hardware.camera2.CameraManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.widget.SwitchCompat;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.teamaspirant.autoflash.R;

import java.util.Locale;


public class MainActivity extends AppCompatActivity  {

    BroadcastReceiver broadcastReceiver;
    private CameraManager cameraManager;
    private String cameraID;

    SwitchCompat setup;

    TextView time,setup_times,secondcount;
    SeekBar seekBar;
    int delay,minutes,seconds;
    LinearLayout timer_layoutSeekbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setup = findViewById(R.id.switchToggle);

        time = findViewById(R.id.timer_watch);
        seekBar = findViewById(R.id.seekbar_id);
        timer_layoutSeekbar = findViewById(R.id.timer_layoutSeekbar);

        secondcount= findViewById(R.id.seconds_count);

        seekBar.setProgress(0);


        findViewById(R.id.timer_layout).setVisibility(View.GONE);
//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
//                R.array.timer_spinner, android.R.layout.simple_spinner_item);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        timerspinner.setAdapter(adapter);
//
//        timerspinner.setOnItemSelectedListener(this);


        cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            cameraID = cameraManager.getCameraIdList()[0]; // 0 is for back camera and 1 is for front camera
        } catch (Exception e) {
            e.printStackTrace();
        }





        setup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!setup.isChecked()){
                    timer_layoutSeekbar.setVisibility(View.VISIBLE);

                    findViewById(R.id.timer_layout).setVisibility(View.GONE);
                    try {
                        cameraManager.setTorchMode(cameraID, false);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    unregisteredNetwork();



                }else{



                    if(isConnected()){
                        timer_layoutSeekbar.setVisibility(View.GONE);


                        if(seekBar.getProgress()!=0){

                            findViewById(R.id.timer_layout).setVisibility(View.VISIBLE);

                        }
                        Toast.makeText(MainActivity.this, "The torch will turn on when the wifi leaves.", Toast.LENGTH_LONG).show();

                        broadcastReceiver = new ConnectionReceiver(seekBar.getProgress());



                        updateTime();

                        registeredNetworkBrodcast();

                    }else{

                        timer_layoutSeekbar.setVisibility(View.VISIBLE);
                        openDialog();
                        setup.setChecked(false);




                    }


                }

            }
        });


        seekBar.setOnSeekBarChangeListener(

                new SeekBar.OnSeekBarChangeListener() {
                    int progress = seekBar.getProgress();



                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progressValue, boolean b) {
                        progress = progressValue;




                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                        secondcount.setText("The torch will remain on for "+progress+" seconds \nafter the WIFI leaves.");
                        updateTime();
                        if(progress ==0)
                        {

                            secondcount.setText("Untill I turn off/ WIFI comes \nback");



                        }

                    }
                });





    }
    public boolean isConnected(){



        try {
            ConnectivityManager connectivityManager
                    = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

            return (activeNetworkInfo != null && activeNetworkInfo.isConnected());


        }catch (NullPointerException e){
            e.printStackTrace();
            return false;
        }

    }


    private void updateTime() {

        minutes = (( seekBar.getProgress() * 1000) / 1000) / 60;
        seconds = ((seekBar.getProgress() * 1000)/1000) % 60;

       String format = String.format(Locale.ENGLISH,"%02d : %02d",minutes,seconds);

       time.setText(format);


    }





    public void openDialog(){
        AlertDialog dlg = new AlertDialog.Builder(MainActivity.this).setTitle("Message")
                .setMessage("You have to turn on your wifi before turning on")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
        dlg.show();
    }



    protected  void registeredNetworkBrodcast(){

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
                registerReceiver(broadcastReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
            }
    }



    protected void unregisteredNetwork(){
        try {
            unregisterReceiver(broadcastReceiver);
        }catch (IllegalArgumentException e){
            e.printStackTrace();
        }
    }

    protected void onDestroy(){
        super.onDestroy();
        unregisteredNetwork();
    }



}

