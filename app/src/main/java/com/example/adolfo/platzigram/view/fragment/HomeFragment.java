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

import com.example.adolfo.platzigram.R;
import com.example.adolfo.platzigram.adapter.PictureAdapterRecyclerView;
import com.example.adolfo.platzigram.model.Picture;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        showToolbar(getResources().getString(R.string.tab_home), false, view);

        RecyclerView pictureRecycler = (RecyclerView) view.findViewById(R.id.pictureRecycler);

        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(linearLayoutManager.VERTICAL);

        pictureRecycler.setLayoutManager(linearLayoutManager);
        PictureAdapterRecyclerView pictureAdapterRecyclerView = new PictureAdapterRecyclerView(buildPictures(), R.layout.cardview_picture, getActivity() );

        pictureRecycler.setAdapter(pictureAdapterRecyclerView);

        return view;
    }

    public ArrayList<Picture> buildPictures(){
        ArrayList<Picture> pictures = new ArrayList<>();
        pictures.add(new Picture("https://upload.wikimedia.org/wikipedia/commons/1/1e/Stonehenge.jpg", "Adolfo Lopez", "2 dias", "3 Likes" ));
        pictures.add(new Picture("https://upload.wikimedia.org/wikipedia/commons/1/1e/Stonehenge.jpg", "Adolfo Lopez", "3 dias", "3 Likes" ));
        pictures.add(new Picture("https://upload.wikimedia.org/wikipedia/commons/1/1e/Stonehenge.jpg", "Adolfo Lopez", "2 dias", "3 Likes" ));
        pictures.add(new Picture("https://upload.wikimedia.org/wikipedia/commons/1/1e/Stonehenge.jpg", "Adolfo Lopez", "4 dias", "3 Likes" ));
        return pictures;
    }

    public void showToolbar(String title, boolean upButton, View view){
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(title);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(upButton);
    }


}
