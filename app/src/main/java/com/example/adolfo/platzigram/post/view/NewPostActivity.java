package com.example.adolfo.platzigram.post.view;

import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.adolfo.platzigram.PlatzigramApplication;
import com.example.adolfo.platzigram.R;
import com.example.adolfo.platzigram.model.Post;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;

public class NewPostActivity extends AppCompatActivity {

    private static final String TAG = "NewPostActivity";
    private static final String POST_NODE = "Posts";
    private ImageView imgPhoto;
    private EditText edtTitle;
    private EditText edtDescription;
    private Button btnCreatePost;
    private String photoPath;
    private PlatzigramApplication app;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private ProgressBar progressBarNewPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);

        app = (PlatzigramApplication) getApplicationContext();

        storageReference = app.getStorageReference();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        progressBarNewPost = (ProgressBar) findViewById(R.id.progressBarNewPost);
        hideProgressBar();

        imgPhoto = (ImageView) findViewById(R.id.imgPhoto);
        edtTitle = (EditText) findViewById(R.id.edtTitle);
        edtDescription = (EditText) findViewById(R.id.edtDescription);
        btnCreatePost = (Button) findViewById(R.id.btnCreatePost);

        if(getIntent().getExtras() != null){
            photoPath = getIntent().getExtras().getString("PHOTO_PATH_TEMP");
            showPhoto();
        }

        btnCreatePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgressBar();
                uploadPhoto();
                //hideProgressBar();
            }
        });
    }

    private void uploadPhoto() {
        imgPhoto.setDrawingCacheEnabled(true);
        imgPhoto.buildDrawingCache();

        Bitmap bitmap = imgPhoto.getDrawingCache();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);

        byte[] photoByte = byteArrayOutputStream.toByteArray();

        String photoName = photoPath.substring(photoPath.lastIndexOf("/") + 1, photoPath.length());

        StorageReference photoRefecence = storageReference.child("postImages/"+ photoName);

        UploadTask uploadTask = photoRefecence.putBytes(photoByte);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Error to upload the photo "+ e.toString() );
                e.printStackTrace();
                FirebaseCrash.report(e);
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Uri uriPhoto = taskSnapshot.getDownloadUrl();
                String photoUrl = uriPhoto.toString();
                createPost(edtTitle.getText().toString(), edtDescription.getText().toString(), photoUrl);
                Log.w(TAG, "URL Photo :"+ photoUrl);
                finish();
            }
        });
    }

    private void createPost(String title, String description, String img_url) {
        Post post = new Post(databaseReference.push().getKey(), title, description, img_url);
        databaseReference.child(POST_NODE)
                .child(post.getId()).setValue(post);

    }

    private void showPhoto(){
        Picasso.with(this).load(photoPath).into(imgPhoto);
    }


    public void showProgressBar() {
        progressBarNewPost.setVisibility(View.VISIBLE);
    }

    public void hideProgressBar() {
        progressBarNewPost.setVisibility(View.GONE);
    }

}
