package com.example.serj_.rssreader.activities;
import android.content.Intent;
import android.app.Activity;
import android.os.Bundle;

import android.view.View;
import android.widget.EditText;
import com.example.serj_.rssreader.R;

public class AddChannelDialog extends Activity {

    static final private int RESULT_NONE = 0;
    static final private int RESULT_URL = 1;

    @Override
    protected void onCreate(Bundle Instance) {
        super.onCreate(Instance);
        setContentView(R.layout.activity_dialog);
    }
   public void onClickCancel(View view){
       setResult(RESULT_NONE);
       finish();
   }
    public void onClickADD(View view){

        EditText urlField = (EditText) findViewById(R.id.urlField);
        String output = urlField.getText().toString();

        if(!output.equals("")) {
            Intent intent = new Intent();
            intent.putExtra("URL",output);
            setResult(RESULT_URL,intent);
            finish();
        }
    }
}
