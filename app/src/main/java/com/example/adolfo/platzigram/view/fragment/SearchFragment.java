package com.example.adolfo.platzigram.view.fragment;


import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.adolfo.platzigram.R;
import com.example.adolfo.platzigram.adapter.PictureAdapterRecyclerView;
import com.example.adolfo.platzigram.model.Picture;
import com.example.adolfo.platzigram.model.Post;
import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static android.support.v7.widget.SearchView.*;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {

    private static final String POST_NODE = "Posts";
    private DatabaseReference databaseReference;
    private static final String TAG = "SearchFragment" ;
    private SearchView svPictureSearch;
    private ArrayList<Picture> pictures;

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FirebaseCrash.log("Init " + TAG);

        View view = inflater.inflate(R.layout.fragment_search, container, false);
        final RecyclerView pictureRecycler = (RecyclerView) view.findViewById(R.id.pictureSearchRecycler);

        svPictureSearch = (SearchView) view.findViewById(R.id.svPictureSearch);
        databaseReference = FirebaseDatabase.getInstance().getReference();

        svPictureSearch.setOnQueryTextListener(new OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filter(query, pictureRecycler);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText, pictureRecycler);
                return false;
            }
        });


        return view;
    }

    private void filter(String query, final RecyclerView pictureRecycler) {
        pictures = new ArrayList<>();

        databaseReference.child(POST_NODE).orderByChild("title").startAt(query).addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        pictures.clear();
                        if(dataSnapshot.exists()) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                Post post = snapshot.getValue(Post.class);
                                pictures.add(new Picture(post.getId(), post.getImgUrl(), post.getTitle(), "3 days", "20"));
                            }
                        }

                        Log.w(TAG, "Pictures size : " + String.valueOf(pictures.size()));
                        GridLayoutManager gridLayoutManager = new
                                GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false);

                        pictureRecycler.setLayoutManager(gridLayoutManager);
                        PictureAdapterRecyclerView pictureAdapterRecyclerView = new
                                PictureAdapterRecyclerView(pictures, R.layout.cardview_search, getActivity());

                        pictureRecycler.setAdapter(pictureAdapterRecyclerView);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

}
