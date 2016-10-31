package com.example.serj_.rssreader.activities;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import com.example.serj_.rssreader.R;
import com.example.serj_.rssreader.services.NetworkService;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    static final int ADD_CHANNEL_DIALOG = 0;
    static final int RESULT_NONE = 0;
    static final int RESULT_URL = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();


        if (id == R.id.add_new_rrs) {
            startActivityForResult(new Intent(this, AddChannelDialog.class),ADD_CHANNEL_DIALOG);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data){
        TextView test =(TextView) findViewById(R.id.test);
        if(resultCode==RESULT_URL) {
            String url = data.getExtras().getString("URL");
            try {

            }
            catch (Exception E){

            }
        }
    }
    private void StartNetwork() throws IOException{

    }

}
