package com.teamaspirant.autoflash;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;
import android.widget.LinearLayout;

public class About extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        WebView view = new WebView(this);
        view.setVerticalScrollBarEnabled(false);

        ((LinearLayout)findViewById(R.id.inset_web_view)).addView(view);

        view.loadData(getString(R.string.about_info), "text/html; charset=utf-8", "utf-8");
    }
}