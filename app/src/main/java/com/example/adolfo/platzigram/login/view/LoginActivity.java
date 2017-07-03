package com.example.adolfo.platzigram.login.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.Tag;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.adolfo.platzigram.R;
import com.example.adolfo.platzigram.login.presenter.LoginPresenter;
import com.example.adolfo.platzigram.login.presenter.LoginPresenterImpl;
import com.example.adolfo.platzigram.view.ContainerActivity;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.crash.FirebaseCrash;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import java.util.Arrays;
import java.util.List;

public class LoginActivity extends AppCompatActivity implements LoginView{

    @NotEmpty
    @Email
    private EditText username;

    @NotEmpty
    private EditText password;

    private Button login;
    private ProgressBar progressBarLogin;
    private LoginPresenter presenter;

    private static final String TAG = "LoginActivity";
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    private LoginButton loginButtonFacebook;
    private CallbackManager callbackManager;
    private Validator validator;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        firebaseAuth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if(firebaseUser != null){
                    Log.w(TAG, "User logged :" + firebaseUser.getEmail());
                }else{
                    Log.w(TAG, "User no logged");
                }
            }
        };

        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);

        login = (Button) findViewById(R.id.login);
        progressBarLogin = (ProgressBar) findViewById(R.id.progressBarLogin);
        loginButtonFacebook = (LoginButton) findViewById(R.id.login_button_facebook);
        hideProgressBar();

        presenter = new LoginPresenterImpl(this);

        validator = new Validator(LoginActivity.this);
        validator.setValidationListener(new Validator.ValidationListener() {
            @Override
            public void onValidationSucceeded() {
                signIn(username.getText().toString(), password.getText().toString());
            }

            @Override
            public void onValidationFailed(List<ValidationError> errors) {

                for (ValidationError error : errors) {
                    View view = error.getView();
                    String message = error.getCollatedErrorMessage(LoginActivity.this);

                    if (view instanceof EditText) {
                        ((EditText) view).setError(message);
                    } else {
                        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            validator.validate();
            }
        });

        loginButtonFacebook.setReadPermissions(Arrays.asList("email"));
        loginButtonFacebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.w(TAG, "Facebook login Success Token :"+ loginResult.getAccessToken().getApplicationId());
                FirebaseCrash.logcat(Log.WARN, TAG,  "Facebook login Success Token :"+ loginResult.getAccessToken().getApplicationId());
                SingInFacebookFirebase(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.w(TAG, "Facebook login Cancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.w(TAG, "Facebook login Error " + error.toString());
                error.printStackTrace();
                FirebaseCrash.report(error);
            }
        });

    }


    private void SingInFacebookFirebase(AccessToken accessToken) {
        AuthCredential authCredential = FacebookAuthProvider.getCredential(accessToken.getToken());
        Log.w(TAG, accessToken.getToken() );
        firebaseAuth.signInWithCredential(authCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.w(TAG, String.valueOf(task.isSuccessful()));

                if(task.isSuccessful()){

                    FirebaseUser user = task.getResult().getUser();
                    SharedPreferences preferences = getSharedPreferences("USER", Context.MODE_PRIVATE);

                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("email", user.getEmail());
                    editor.commit();

                    goContainer();
                    Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                    FirebaseCrash.logcat(Log.WARN, TAG, "Login Successful"+ user.getEmail());
                }else{
                    Toast.makeText(LoginActivity.this, "Login not successful", Toast.LENGTH_SHORT).show();
                    FirebaseCrash.logcat(Log.WARN, TAG, "Login Successful");
                }
            }
        });
    }

    private void signIn(String username, String password) {
            presenter.singIn(username, password, this, firebaseAuth);
    }

    public void goCreateAccount(View view){
        goCreateAccount();
    }

    @Override
    public void goCreateAccount() {
        Intent intent = new Intent(this, CreateAccountActivity.class);
        startActivity(intent);
    }

    @Override
    public void goContainer() {
        Intent intent = new Intent(this, ContainerActivity.class);
        startActivity(intent);
    }

    @Override
    public void enableInputs() {
        username.setEnabled(true);
        password.setEnabled(true);
        login.setEnabled(true);
    }

    @Override
    public void disabledInputs() {
        username.setEnabled(false);
        password.setEnabled(false);
        login.setEnabled(false);
    }

    @Override
    public void showProgressBar() {
        progressBarLogin.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        progressBarLogin.setVisibility(View.GONE);
    }

    @Override
    public void loginError(String error) {
        Toast.makeText(this, getString(R.string.login_error) +" "+ error, Toast.LENGTH_SHORT).show();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
