package com.example.serj_.rssreader.activities;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import com.example.serj_.rssreader.R;

public class AddChannelDialog extends AppCompatActivity {

    static final private int RESULT_NONE = 0;
    static final private int RESULT_URL = 1;

    @Override
    protected void onCreate(Bundle Instance) {
        super.onCreate(Instance);
        setContentView(R.layout.activity_dialog);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }
   public void onClickCancel(View view){
       setResult(RESULT_NONE);
       finish();
   }
    public void onClickADD(View view){

        EditText urlField = (EditText) findViewById(R.id.urlField);
        String output = urlField.getText().toString();

        if(Patterns.WEB_URL.matcher(output).matches()) {
            Intent intent = new Intent();
            intent.putExtra("URL",output);
            setResult(RESULT_URL,intent);
            finish();
        }
    }


}
