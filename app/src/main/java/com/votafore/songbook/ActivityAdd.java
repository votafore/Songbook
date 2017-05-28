package com.votafore.songbook;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.provider.OpenableColumns;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.votafore.songbook.firetestmodel.Group;
import com.votafore.songbook.support.DialogListAdapter;
import com.votafore.songbook.firetestmodel.Song;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

public class ActivityAdd extends AppCompatActivity {

    TextView songText;
    String fileText;
    String fileName;

    private boolean isUserSigned;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        Button save1 = (Button) findViewById(R.id.save_song1);

        Button load = (Button) findViewById(R.id.load_song);
        final TextInputEditText inputText = (TextInputEditText) findViewById(R.id.song_title);
        songText = (TextView) findViewById(R.id.song_text);


        save1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                if(!isUserSigned){
//                    Intent signin = new Intent(ActivityAdd.this, ActivitySignIn.class);
//                    startActivity(signin);
//                    return;
//                }

                createDialog(0);
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
    protected void onStart() {
        super.onStart();
        isUserSigned = (FirebaseAuth.getInstance().getCurrentUser() != null);
    }

    private void createDialog(int id) {

        AlertDialog.Builder  builder = new AlertDialog.Builder(this);
        AlertDialog dialog;

        View v = View.inflate(getApplicationContext(), R.layout.dialog_pick_group, null);

        ListView list = (ListView) v.findViewById(R.id.dialog_list_groups);

        final DialogListAdapter adapter = new DialogListAdapter();

        list.setAdapter(adapter);

        builder.setTitle("Укажите группу для осхранения");
        builder.setView(v);

        dialog = builder.create();

        final AlertDialog finalDialog = dialog;
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Group g = adapter.getItem(position);

                Toast.makeText(ActivityAdd.this, String.format("Chosen group %1$s", g.title), Toast.LENGTH_SHORT).show();

                finalDialog.dismiss();

                Song song = new Song();
                song.text = songText.getText().toString();
                song.title = fileName;

                FIreApp.getInstance().loadSong(song, "");

                finish();
            }
        });

        dialog.show();
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

        fileName = fileCursor.getString(fileCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
        fileName = fileName.replaceFirst("[.][^.]+$", "");

        TextInputEditText inputText = (TextInputEditText) findViewById(R.id.song_title);
        inputText.setText(fileName);

        InputStream         is      = new FileInputStream(pdf.getFileDescriptor());
        InputStreamReader   isr     = new InputStreamReader(is, Charset.defaultCharset());
        BufferedReader      reader  = new BufferedReader(isr);

        fileText = "";

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
