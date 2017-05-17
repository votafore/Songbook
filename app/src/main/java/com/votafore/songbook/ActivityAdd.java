package com.votafore.songbook;

import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ButtonBarLayout;
import android.view.View;
import android.widget.Button;

import com.votafore.songbook.App;
import com.votafore.songbook.R;

public class ActivityAdd extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        Button save = (Button) findViewById(R.id.save_song);
        final TextInputEditText inputText = (TextInputEditText) findViewById(R.id.song_title);


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                App.getInstance().saveSong(inputText.getText().toString(), 1);
                exit();
            }
        });
    }

    public void exit(){
        finish();
    }
}
