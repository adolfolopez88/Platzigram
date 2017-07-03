package com.example.adolfo.platzigram.login.interactor;

import android.app.Activity;

import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by Adolfo on 24/06/2017.
 */

public interface LoginInteractor {

    void singIn(String username, String password, Activity activity, FirebaseAuth firebaseAuth);
}
