package com.example.adolfo.platzigram.login.view;

/**
 * Created by Adolfo on 24/06/2017.
 */

public interface LoginView {

    void goCreateAccount();
    void goContainer();
    void enableInputs();
    void disabledInputs();
    void showProgressBar();
    void hideProgressBar();
    void loginError(String error);
}
