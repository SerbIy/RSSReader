package com.example.serj_.rssreader.dialogscreen;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import com.example.serj_.rssreader.R;
import lombok.NonNull;

import java.util.logging.Logger;

public class AddChannelDialog extends AppCompatActivity {

    private static final int RESULT_NONE = 1451;
    private static final int RESULT_URL = 122;

    private final Logger logger = Logger.getLogger(this.getClass().getName());
    @Override
    protected void onCreate(Bundle Instance) {
        super.onCreate(Instance);

        setContentView(R.layout.activity_dialog);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

   public void onClickCancel(@NonNull final View view){
       setResult(RESULT_NONE);
       finish();
   }

    public void onClickADD(@NonNull final View view){
        final EditText urlField = (EditText) findViewById(R.id.urlField);
        final String output = urlField.getText().toString();
        if(!output.equals("")) {
            if (Patterns.WEB_URL.matcher(output).matches()) {
                logger.info("That's an url");
                final Intent intent = new Intent();
                intent.putExtra("URL", output);
                setResult(RESULT_URL, intent);
                finish();
            }
            else{
                logger.info("That's not an url");
            }
        }
    }


}
