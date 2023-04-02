package com.example.coursework;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;

public class userGuide extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_guide);

        WebView webview = (WebView) findViewById(R.id.webview);
        webview.loadUrl("file:///android_asset/user_guide.html");
    }
}
