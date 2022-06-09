package com.teamaspirant.autoflash;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.sql.Time;
import java.util.Locale;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class ConnectionReceiver extends BroadcastReceiver {

    private CameraManager cameraManager;
    private String cameraID;
    public int delay;


    public ConnectionReceiver(int timer) {

        delay = timer;

    }



    @Override
    public void onReceive(Context context, Intent intent) {



            cameraManager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);


            try {
                cameraID = cameraManager.getCameraIdList()[0]; // 0 is for back camera and 1 is for front camera
            } catch (Exception e) {
                e.printStackTrace();
            }



            if(isConnected(context)){
                try {
                    cameraManager.setTorchMode(cameraID, false);
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

            else {
         

                try {

                    cameraManager.setTorchMode(cameraID, true);


                } catch (Exception e) {
                    e.printStackTrace();
                }


                if (delay > 0) {
                    Timer timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            try {
                                cameraManager.setTorchMode(cameraID, false);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    }, delay * 1000);


                }
            }

    }

    public boolean isConnected(Context context){

        try {
            ConnectivityManager connectivityManager
                    = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

            return (activeNetworkInfo != null && activeNetworkInfo.isConnected());


        }catch (NullPointerException e){
            e.printStackTrace();
            return false;
        }

    }


}






