package com.example.adolfo.platzigram.login.repository;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.example.adolfo.platzigram.login.presenter.LoginPresenter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by Adolfo on 24/06/2017.
 */

public class LoginRepositoryImpl implements LoginRepository {

    LoginPresenter presenter;

    public LoginRepositoryImpl(LoginPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void singIn(String username, String password, final Activity activity, FirebaseAuth firebaseAuth) {
        boolean success = true;

        firebaseAuth.signInWithEmailAndPassword(username, password)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser user = task.getResult().getUser();
                            SharedPreferences preferences = activity.getSharedPreferences("USER", Context.MODE_PRIVATE);

                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("email", user.getEmail());
                            editor.commit();

                            presenter.loginSuccess();

                        }else{
                            presenter.loginError("Something wrong");
                        }
                    }
                });

        if(success){
            presenter.loginSuccess();
        }else{
            presenter.loginError("Something wrong");
        }
    }
}
