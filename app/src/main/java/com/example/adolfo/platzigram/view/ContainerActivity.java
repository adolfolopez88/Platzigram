package com.example.adolfo.platzigram.view;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.adolfo.platzigram.R;
import com.example.adolfo.platzigram.view.fragment.HomeFragment;
import com.example.adolfo.platzigram.view.fragment.ProfileFragment;
import com.example.adolfo.platzigram.view.fragment.SearchFragment;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabReselectListener;
import com.roughike.bottombar.OnTabSelectListener;

public class ContainerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);

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
}
