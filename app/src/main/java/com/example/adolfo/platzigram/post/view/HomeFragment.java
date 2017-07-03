package com.example.adolfo.platzigram.post.view;


import android.app.Activity;
import android.app.DownloadManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";
    private static final String POST_NODE = "Posts";
    private static final int REQUEST_CAMERA = 1;
    private FloatingActionButton fadCamera;
    private String photoPathTemp = "";
    private DatabaseReference databaseReference;
    private ArrayList<Picture> pictures;
    private RecyclerView pictureRecycler;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        showToolbar(getResources().getString(R.string.tab_home), false, view);
        databaseReference = FirebaseDatabase.getInstance().getReference();

        pictureRecycler = (RecyclerView) view.findViewById(R.id.pictureRecycler);
        fadCamera = (FloatingActionButton) view.findViewById(R.id.fabCamera);
        pictures = new ArrayList<>();

        databaseReference.child(POST_NODE).addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        pictures.clear();
                        if(dataSnapshot.exists()) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                Post post = snapshot.getValue(Post.class);
                                pictures.add(new Picture(post.getId(), post.getImgUrl(), post.getTitle(), "3 days", "20"));
                            }

                            Log.w(TAG, "pictures size: "+String.valueOf(pictures.size()));
                            LinearLayoutManager linearLayoutManager= new LinearLayoutManager(getContext());
                            linearLayoutManager.setOrientation(linearLayoutManager.VERTICAL);

                            pictureRecycler.setLayoutManager(linearLayoutManager);
                            PictureAdapterRecyclerView pictureAdapterRecyclerView = new PictureAdapterRecyclerView(pictures, R.layout.cardview_picture, getActivity() );
                            pictureRecycler.setAdapter(pictureAdapterRecyclerView);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w(TAG, "Cancelled: "+databaseError.getDetails() );
                    }
                });

        fadCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture();
            }
        });

        return view;
    }

    private void takePicture() {

        Intent intentTakePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if(intentTakePicture.resolveActivity(getActivity().getPackageManager()) != null) {

            File photoFile = null;

            try {
                photoFile = createImageFile();
            } catch (Exception e) {

                e.printStackTrace();
                FirebaseCrash.report(e);
            }

            if (photoFile != null) {
                Uri photoUri = FileProvider.getUriForFile(getActivity(), getActivity().getApplicationContext().getPackageName(), photoFile);

                //pasar parametros
                intentTakePicture.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(intentTakePicture, REQUEST_CAMERA);
            }
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HH-mm-ss").format(new Date());
        String imageFileName = "JPEG_" +timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        Log.w(TAG,   getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString());
        File photo = File.createTempFile(imageFileName, ".jpg", storageDir);

        photoPathTemp = "file:" + photo.getAbsolutePath();
        Log.w(TAG,   getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString());
        return photo;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_CAMERA && resultCode == getActivity().RESULT_OK){

            Intent i = new Intent(getActivity(), NewPostActivity.class);
            i.putExtra("PHOTO_PATH_TEMP", photoPathTemp);
            startActivity(i);
        }
    }

    public void showToolbar(String title, boolean upButton, View view){
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(title);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(upButton);
    }


}
