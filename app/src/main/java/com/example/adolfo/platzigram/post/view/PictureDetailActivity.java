package com.example.adolfo.platzigram.post.view;

import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.transition.Fade;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.adolfo.platzigram.PlatzigramApplication;
import com.example.adolfo.platzigram.R;
import com.example.adolfo.platzigram.model.Post;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class PictureDetailActivity extends AppCompatActivity {

    private static final String POSTS_NODE = "Posts";
    private static final String TAG = "PictureDetailActivity";
    private ImageView imageHeader;
    private PlatzigramApplication app;
    private StorageReference storageReference;
    private String photoName = "postImagesJPEG_20170627_06-37-29_7297759012139080784.jpg";
    private String postId;
    private DatabaseReference databaseReference;
    private Post post;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_detail);
        app = (PlatzigramApplication) getApplicationContext();
        storageReference = app.getStorageReference();

        imageHeader = (ImageView) findViewById(R.id.imageHeader);
        postId = getIntent().getExtras().getString("postId");
        databaseReference = FirebaseDatabase.getInstance().getReference();
        post = new Post();

        databaseReference.child(POSTS_NODE).child(postId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                post = dataSnapshot.getValue(Post.class);
                Picasso.with(PictureDetailActivity.this).load(post.getImgUrl()).into(imageHeader);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "Database error: "+ databaseError.getMessage());
            }
        });

        showToolbar("", true);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            getWindow().setStatusBarColor(Color.TRANSPARENT);
            getWindow().setEnterTransition(new Fade());
        }
    }

    private void showData(Post post) {
        storageReference.child("postImages/" + photoName)
                .getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.with(PictureDetailActivity.this)
                        .load(uri.toString()).into(imageHeader);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(PictureDetailActivity.this, "Something wrong happend to bring the picture", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
                FirebaseCrash.report(e);
            }
        });
    }

    public void showToolbar(String title, boolean upButton){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(upButton);

        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbarLayout);
    }
}
