package com.example.adolfo.platzigram.view;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.ClipData;
import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Switch;
import android.widget.Toast;

import com.example.adolfo.platzigram.R;
import com.example.adolfo.platzigram.login.view.CreateAccountActivity;
import com.example.adolfo.platzigram.login.view.LoginActivity;
import com.example.adolfo.platzigram.post.view.HomeFragment;
import com.example.adolfo.platzigram.view.fragment.ProfileFragment;
import com.example.adolfo.platzigram.view.fragment.SearchFragment;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.crash.FirebaseCrash;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

public class ContainerActivity extends AppCompatActivity {

    private static final String TAG = "ContainerActivity";
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseCrash.log("Init " + TAG);
        setContentView(R.layout.activity_container);
        firebaseInitialize();
        BottomBar bottomBar = (BottomBar) findViewById(R.id.bottombar);
        bottomBar.setDefaultTab(R.id.home);

        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                switch (tabId){
                    case R.id.home:
                        addFragment(new HomeFragment());
                        break;
                    case R.id.search:
                        addFragment(new SearchFragment());
                        break;
                    case R.id.profile:
                        addFragment(new ProfileFragment());
                        break;
                }
            }
        });
    }

    private void addFragment(Fragment fragment){
        if (null != fragment){
            getFragmentManager().beginTransaction()
                    .replace(R.id.container, fragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .addToBackStack(null)
                    .commit();
        }
    }

    private void firebaseInitialize(){
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){

            case R.id.mSingnOut:
                firebaseAuth.signOut();

                if(LoginManager.getInstance() != null){
                    LoginManager.getInstance().logOut();
                }
                Toast.makeText(this, "Sign out session", Toast.LENGTH_SHORT).show();
                Intent intent =new Intent(ContainerActivity.this, LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.mAbout:
                Toast.makeText(this, "By Adolfo", Toast.LENGTH_SHORT).show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
