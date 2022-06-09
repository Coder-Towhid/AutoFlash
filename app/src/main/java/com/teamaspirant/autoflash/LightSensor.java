package com.teamaspirant.autoflash;

import static java.security.AccessController.getContext;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.teamaspirant.autoflash.R;

public class LightSensor extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private TextView valueText,keep;
    private Float ChangeValue;
    private Sensor lightens;
    private CameraManager cameraManager;
    private String cameraID;
    private ImageView lightStat;
    private ConstraintLayout cs;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_light_sensor);


        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        valueText = findViewById(R.id.lightvalue);
        lightStat = findViewById(R.id.lightStat);
        keep = findViewById(R.id.textView3);
        cs = findViewById(R.id.layout_sensor);


        try {
            cameraID = cameraManager.getCameraIdList()[0]; // 0 is for back camera and 1 is for front camera
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT) !=null){
            lightens = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        }
        else{
            Toast.makeText(this, "no sensor", Toast.LENGTH_SHORT).show();
        }





    }

    @Override
    public void onSensorChanged(SensorEvent event) {
            ChangeValue = event.values[0];


            if(ChangeValue < 0.1){
                try {
                    cs.setBackgroundColor(Color.WHITE);
                    int image = getIntent().getIntExtra("image",R.drawable.ic_baseline_lighton);
                    lightStat.setImageResource(image);
                    valueText.setTextColor(Color.BLACK);
                    keep.setTextColor(Color.BLACK);

                    valueText.setText("Light On");
                    cameraManager.setTorchMode(cameraID, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else{
                try {
                    cs.setBackgroundColor(Color.BLACK);
                    keep.setTextColor(Color.WHITE);

                    int image = getIntent().getIntExtra("image",R.drawable.ic_baseline_lightoff);
                    lightStat.setImageResource(image);
                    valueText.setTextColor(Color.WHITE);
                    valueText.setText("Light Off");
                    cameraManager.setTorchMode(cameraID, false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this,sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT), SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
//        sensorManager.unregisterListener(this);

    }


}