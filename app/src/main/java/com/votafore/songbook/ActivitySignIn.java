package com.votafore.songbook;

import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;

public class ActivitySignIn extends AppCompatActivity implements View.OnClickListener, OnCompleteListener{

    FirebaseAuth mAuth;

    TextInputEditText email;
    TextInputEditText pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        mAuth = FirebaseAuth.getInstance();

        Button signin = (Button) findViewById(R.id.signin);
        email = (TextInputEditText) findViewById(R.id.signin_email);
        pass = (TextInputEditText) findViewById(R.id.signin_password);

        signin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        mAuth.signInWithEmailAndPassword(email.getText().toString(), pass.getText().toString()).addOnCompleteListener(this);
    }

    @Override
    public void onComplete(@NonNull Task task) {

        if(task.isSuccessful())
            finish();

        Toast.makeText(ActivitySignIn.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
    }
}
