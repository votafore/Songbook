package com.votafore.songbook;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class ActivityTest extends AppCompatActivity {

    final Object mLock = new Object();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        Button btn = (Button) findViewById(R.id.test);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                synchronized (mLock){
                    mLock.notify();
                }
            }
        });

        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {

                int i = 0;

                synchronized (mLock){

                    do{

                        try {
                            mLock.wait();

                            Log.v("MyThread", "дождались");

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        i++;

                    } while (i < 3);
                }
            }
        });

        th.start();
    }
}
