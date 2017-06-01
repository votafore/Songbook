package com.votafore.songbook;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthCredential;
import com.google.firebase.auth.GoogleAuthProvider;

public class ActivitySignIn extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    FirebaseAuth mAuth;

    TextInputEditText email;
    TextInputEditText pass;

    GoogleApiClient client;

    int RC_SING_IN = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        mAuth = FirebaseAuth.getInstance();

        SignInButton signin = (SignInButton) findViewById(R.id.google_signin);
        signin.setSize(SignInButton.SIZE_STANDARD);
        signin.setOnClickListener(this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        client = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode != RC_SING_IN)
            return;

        GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);

        if(!result.isSuccess()){
            Toast.makeText(ActivitySignIn.this, "failure", Toast.LENGTH_SHORT).show();
            return;
        }

        GoogleSignInAccount account = result.getSignInAccount();

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);

        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(!task.isSuccessful())
                    Toast.makeText(ActivitySignIn.this, "Authentication failed.", Toast.LENGTH_SHORT).show();

                finish();
            }
        });

    }

    @Override
    public void onClick(View v) {

        Intent intent = Auth.GoogleSignInApi.getSignInIntent(client);
        startActivityForResult(intent, RC_SING_IN);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
