package com.example.serj_.rssreader.activities;
import android.content.Intent;
import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialog;
import android.view.View;
import android.widget.EditText;
import com.example.serj_.rssreader.R;

/**
 * Created by serj_ on 23.10.2016.
 */
public class AddChannelDialog extends Activity {

    static final int RESULT_NONE = 0;
    static final int RESULT_URL = 1;

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

        EditText urlfield = (EditText) findViewById(R.id.urlfield);
        String output = urlfield.getText().toString();

        if(!output.equals("")) {
            Intent intent = new Intent();
            intent.putExtra("URL",output);
            setResult(RESULT_URL,intent);
            finish();
        }
    }
}
