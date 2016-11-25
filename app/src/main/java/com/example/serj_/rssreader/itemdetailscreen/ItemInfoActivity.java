package com.example.serj_.rssreader.itemdetailscreen;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.webkit.WebView;
import com.example.serj_.rssreader.R;
import com.example.serj_.rssreader.process.IntentEditor;

public class ItemInfoActivity extends AppCompatActivity {
    private WebView webview;
    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_info);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final Intent intent = getIntent();
        webview = (WebView) findViewById(R.id.web_view);
        webview.loadUrl(IntentEditor.getUrl(intent));
    }
}
