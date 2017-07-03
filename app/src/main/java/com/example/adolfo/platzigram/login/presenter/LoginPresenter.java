package com.example.adolfo.platzigram.login.presenter;

import android.app.Activity;

import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by Adolfo on 24/06/2017.
 */

public interface LoginPresenter {

    void singIn(String username, String password, Activity activity, FirebaseAuth firebaseAuth);
    void loginSuccess();
    void loginError(String error);
}
