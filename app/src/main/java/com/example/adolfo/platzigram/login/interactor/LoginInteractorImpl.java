package com.example.adolfo.platzigram.login.interactor;

import android.app.Activity;

import com.example.adolfo.platzigram.login.presenter.LoginPresenter;
import com.example.adolfo.platzigram.login.repository.LoginRepositoryImpl;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by Adolfo on 24/06/2017.
 */

public class LoginInteractorImpl implements LoginInteractor{

    private LoginPresenter presenter;
    private LoginRepositoryImpl repository;

    public LoginInteractorImpl(LoginPresenter presenter) {
        this.presenter = presenter;
        repository = new LoginRepositoryImpl(presenter);
    }

    @Override
    public void singIn(String username, String password, Activity activity, FirebaseAuth firebaseAuth) {
        repository.singIn(username, password, activity, firebaseAuth);
    }
}
