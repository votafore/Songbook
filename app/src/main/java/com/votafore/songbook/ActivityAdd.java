package com.votafore.songbook;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.provider.OpenableColumns;
import android.provider.Settings;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ButtonBarLayout;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.votafore.songbook.App;
import com.votafore.songbook.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

public class ActivityAdd extends AppCompatActivity {

    TextView songText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        Button save1 = (Button) findViewById(R.id.save_song1);
        Button save2 = (Button) findViewById(R.id.save_song2);
        Button save3 = (Button) findViewById(R.id.save_song3);

        Button load = (Button) findViewById(R.id.load_song);
        final TextInputEditText inputText = (TextInputEditText) findViewById(R.id.song_title);
        songText = (TextView) findViewById(R.id.song_text);


        save1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                App.getInstance().saveSong(inputText.getText().toString(), songText.getText().toString(), 1);
                exit();
            }
        });

        save2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                App.getInstance().saveSong(inputText.getText().toString(), songText.getText().toString(), 2);
                exit();
            }
        });

        save3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                App.getInstance().saveSong(inputText.getText().toString(), songText.getText().toString(), 3);
                exit();
            }
        });

        load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent getFile = new Intent(Intent.ACTION_GET_CONTENT);
                getFile.setType("text/*");

                startActivityForResult(getFile, 0);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode != RESULT_OK)
            return;

        Uri file = data.getData();

        loadTextFromFile(file);
    }

    private void loadTextFromFile(Uri path){

        ParcelFileDescriptor pdf;

        try {
            pdf = getApplicationContext().getContentResolver().openFileDescriptor(path, "r");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }

        Cursor fileCursor = getContentResolver().query(path, null, null, null, null);

        fileCursor.moveToFirst();

        String fileName = fileCursor.getString(fileCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
        fileName = fileName.replaceFirst("[.][^.]+$", "");

        TextInputEditText inputText = (TextInputEditText) findViewById(R.id.song_title);
        inputText.setText(fileName);

        InputStream         is      = new FileInputStream(pdf.getFileDescriptor());
        InputStreamReader   isr     = new InputStreamReader(is, Charset.defaultCharset());
        BufferedReader      reader  = new BufferedReader(isr);

        String fileText = "";

        try {
            while (reader.ready()){

                String line = reader.readLine();

                if(line == null)
                    break;

                fileText = fileText.concat(line);
                fileText = fileText.concat(System.lineSeparator());

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        fileText = fileText.replace("\nКуплет", "\n<b><u>Куплет</u></b>");
        fileText = fileText.replace("Куплет\n", "<b><u>Куплет</u></b>\n");
        fileText = fileText.replace("\nПрипев", "\n<b><u>Припев</u></b>");
        fileText = fileText.replace("\nМост", "\n<b><u>Мост</u></b>");
        fileText = fileText.replace("\nБридж", "\n<b><u>Бридж</u></b>");

        fileText = fileText.replace("\n", "<br>");

        songText.setText(Html.fromHtml(fileText));
    }

    public void exit(){
        finish();
    }
}
