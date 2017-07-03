package com.example.adolfo.platzigram.view.fragment;


import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.adolfo.platzigram.R;
import com.example.adolfo.platzigram.adapter.PictureAdapterRecyclerView;
import com.example.adolfo.platzigram.model.Picture;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.crash.FirebaseCrash;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {


    private static final String TAG = "ProfileFragment" ;
    private CircleImageView civAvatarProfile;
    private TextView tvDetailProfile;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FirebaseCrash.log("Init " + TAG);
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        showToolbar("", false, view);

        tvDetailProfile = (TextView) view.findViewById(R.id.tvDetailProfile);
        civAvatarProfile = (CircleImageView) view.findViewById(R.id.civAvatarProfile);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(user.getPhotoUrl() != null)
            Picasso.with(getContext()).load(user.getPhotoUrl()).into(civAvatarProfile);

        // Inflate the layout for this fragment
        return view;
    }

    public void showToolbar(String title, boolean upButton, View view){
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(title);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(upButton);
    }


}
