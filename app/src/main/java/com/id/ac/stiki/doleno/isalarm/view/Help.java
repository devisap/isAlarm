package com.id.ac.stiki.doleno.isalarm.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;

import com.id.ac.stiki.doleno.isalarm.R;

public class Help extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        WebView webView = findViewById(R.id.web_view);
        webView.loadUrl("file:///android_asset/asset/help.html");
    }
}