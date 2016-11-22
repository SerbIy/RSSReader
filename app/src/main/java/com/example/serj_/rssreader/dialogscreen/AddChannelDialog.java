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

public class AddChannelDialog extends AppCompatActivity {

    private static final int RESULT_NONE = 1451;
    private static final int RESULT_URL = 6123;

    private EditText urlField = null;

    @Override
    protected void onCreate(Bundle Instance) {
        super.onCreate(Instance);
        urlField = (EditText) findViewById(R.id.urlField);
        setContentView(R.layout.activity_dialog);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

   public void onClickCancel(@NonNull final View view){
       setResult(RESULT_NONE);
       finish();
   }

    public void onClickADD(@NonNull final View view){

        final String output = urlField.getText().toString();

        if(Patterns.WEB_URL.matcher(output).matches()) {
            final Intent intent = new Intent();
            intent.putExtra("URL",output);
            setResult(RESULT_URL,intent);
            finish();
        }
    }


}
