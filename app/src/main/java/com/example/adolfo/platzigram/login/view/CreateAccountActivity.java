package com.example.adolfo.platzigram.login.view;

import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.adolfo.platzigram.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class CreateAccountActivity extends AppCompatActivity {

    private static final String TAG = "CreateAccountActivity";
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    private Button btnJoinUs;
    private TextInputEditText edtEmail, edtPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        showToolbar(getResources().getString(R.string.toolbar_tittle_createaccount), true);

        btnJoinUs   = (Button) findViewById(R.id.joinUs);
        edtEmail    = (TextInputEditText) findViewById(R.id.email);
        edtPassword = (TextInputEditText) findViewById(R.id.password_createaccount);

        firebaseAuth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if(firebaseUser != null){
                    Log.w(TAG, "User logged" + firebaseUser.getEmail());
                }else{
                    Log.w(TAG, "User no logged");
                }
            }
        };

        btnJoinUs.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.w(TAG, "click join us");
               CreateAccount();
            }
        });

    }

    private void CreateAccount() {
        String email    = edtEmail.getText().toString();
        String password = edtPassword.getText().toString();

        Toast.makeText(CreateAccountActivity.this, "creating account", Toast.LENGTH_LONG).show();
        Log.w(TAG, "Data:"+ email + password);
        try {
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Log.w(TAG, String.valueOf(task.isSuccessful()));
                            if(task.isSuccessful()){
                                Toast.makeText(CreateAccountActivity.this, "account created successfully", Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(CreateAccountActivity.this, "something wrong", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }catch (Exception ex){
            Log.w(TAG, "Error:"+ ex.toString());
        }

    }

    public void showToolbar(String title, boolean upButton){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(upButton);
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        firebaseAuth.removeAuthStateListener(authStateListener);
    }
}
