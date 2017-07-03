package com.example.adolfo.platzigram;

import android.app.Application;
import android.support.annotation.NonNull;
import android.util.Log;

import com.facebook.FacebookSdk;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * Created by Adolfo on 26/06/2017.
 */

public class PlatzigramApplication extends Application {

    private static final String TAG = "PlatzigramApplication";
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseStorage firebaseStorage;

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseCrash.log("Init " + TAG);
        FacebookSdk.sdkInitialize(getApplicationContext());

        firebaseAuth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if(firebaseUser != null){
                    //Log.w(TAG, "User logged :" + firebaseUser.getEmail());
                    FirebaseCrash.logcat(Log.WARN, TAG, "User Logged "+ firebaseUser.getEmail());
                }else{
                    //Log.w(TAG, "User no logged");
                    FirebaseCrash.logcat(Log.WARN, TAG, "User no Logged "+ firebaseUser.getEmail());
                }
            }
        };

        firebaseStorage = FirebaseStorage.getInstance();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }

    public StorageReference getStorageReference(){
        return  firebaseStorage.getReference();
    }
}
