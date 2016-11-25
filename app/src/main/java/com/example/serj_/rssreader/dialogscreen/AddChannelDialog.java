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

    private static final int RESULT_URL = 122;
    private static final String TEXT_IN_FIELD = "current_text_in_field";
    private static final Logger logger = Logger.getLogger(AddChannelDialog.class.getName());
    private EditText rssField;
    @Override
    protected void onCreate(Bundle Instance) {
        super.onCreate(Instance);

        setContentView(R.layout.activity_dialog);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.rssField = (EditText) findViewById(R.id.rssField);
    }

   public void onClickCancel(@NonNull final View view){
       finish();
   }

    public void onClickADD(@NonNull final View view){

        final String output = rssField.getText().toString();
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
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        logger.info("We try to save something");
        if(rssField.getText()!=null) {
            savedInstanceState.putString(TEXT_IN_FIELD, rssField.getText().toString());
            logger.info("String saved");
        }
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onRestoreInstanceState(final Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        final String savedText = savedInstanceState.getString(TEXT_IN_FIELD);
        if(savedText!=null){
            rssField.setText(savedText);
            logger.info("String restored");
        }
    }


}
