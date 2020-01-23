package com.project.ksih_article;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.project.ksih_article.article.ArticleModel;
import com.project.ksih_article.article.FirebaseUtil;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ArticleActivity extends AppCompatActivity {

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private static final int PICTURE_RESULT = 42;

    EditText name, topic, weblink;
    ImageView imageView;

    ArticleModel article;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

         FirebaseUtil.openFbReference("articles");
         mFirebaseDatabase = FirebaseUtil.mFirebaseDatabase;
         mDatabaseReference = FirebaseUtil.mDatabaseReference;


        name = findViewById(R.id.enterName);
        topic = findViewById(R.id.topic);
        weblink = findViewById(R.id.weblink);
        imageView = findViewById(R.id.image);

        Intent intent = getIntent();
        ArticleModel articleModel = (ArticleModel)intent.getSerializableExtra("Article");

        if (articleModel==null){
            articleModel = new ArticleModel();
        }
        this.article = articleModel;
        name.setText(articleModel.getName());
        topic.setText(articleModel.getTitle());
        weblink.setText(articleModel.getWeblink());

        showImage(article.getImageUrl());

        Button uploadPic = findViewById(R.id.btnImage);
        uploadPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/jpeg");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(intent.createChooser(intent, "Insert Picture" ), PICTURE_RESULT);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.article_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.save_artice:
                saveArticle();
                Log.d("saved", "to firebase now");
                clean();
                return2Article();
                return true;
            case R.id.delete_artice:
                deleteArticle();
                Toast.makeText(this, "Article Deleted", Toast.LENGTH_SHORT).show();
                Log.d("deleted", "Article Removed");
                clean();
                return2Article();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void clean() {
        name.setText("");
        topic.setText("");
        weblink.setText("");
        topic.requestFocus();

    }

    public void saveArticle() {
//        String writersName = name.getText().toString();
//        String writersTopic = topic.getText().toString();
//        String writersLink = weblink.getText().toString();
        article.setName(name.getText().toString());
        article.setTitle(topic.getText().toString());
        article.setWeblink(weblink.getText().toString());

//        ArticleModel article = new ArticleModel( writersName, writersTopic, writersLink,  "" );

        if (article.getId()==null){
            mDatabaseReference.push().setValue(article);
            Toast.makeText(this, "Your Article has been added", Toast.LENGTH_SHORT).show();
        }else{
            mDatabaseReference.child(article.getId()).setValue(article);
            Toast.makeText(this, "Your article has been updated", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICTURE_RESULT && resultCode == RESULT_OK){

            Uri imageUri = data.getData();


            StorageReference ref = FirebaseUtil.mStorageRef.child(imageUri.getLastPathSegment());

            UploadTask uploadTask = ref.putFile(imageUri);


            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();

                    }

                    // Continue with the task to get the download URL
                    return ref.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        System.out.println("Upload " + downloadUri);
                        Toast.makeText(ArticleActivity.this, "Successfully uploaded", Toast.LENGTH_SHORT).show();
                        if (downloadUri != null) {

                            String url = downloadUri.toString(); //YOU WILL GET THE DOWNLOAD URL HERE !!!!
                            System.out.println("Upload " + url );

                            article.setImageUrl(url );


//                            String pictureName = ref.getStorage().getPath();

//                            String pictureName = ref.getStorage().getReference().getDownloadUrl().toString();

                            String pictureName = ref.getPath();

//                            String pictureName = ref.getStorage().getReference().getPath();

                            article.setImageName(pictureName);
                            Log.d("Url: ", url );
//                            Log.d("Name", pictureName);
                            showImage(url );
                            Log.d("Activity Result", "Activity picture executed");

                        }

                    } else {
                        // Handle failures
                        // ...
                    }
                }


//            ref.putFile(imageUri).addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                    String url = taskSnapshot.getMetadata().getReference().getDownloadUrl().toString();
//
//
//                    String pictureName = taskSnapshot.getStorage().getPath();
//                    article.setImageUrl(url);
//
//                    article.setImageName(pictureName);
//                    Log.d("Url: ", url);
//                    Log.d("Name", pictureName);
//                    showImage(url);
//                    Log.d("Activity Result", "Activity picture executed");
//                }
            });
        }
    }

    public void deleteArticle(){
        if (article==null){
//            Toast.makeText(this, "There is no article to delete", Toast.LENGTH_SHORT).show();
            return;
        }
        mDatabaseReference.child(article.getId()).removeValue();
        if (article.getImageName() != null && article.getImageName().isEmpty()==false){


            StorageReference picRef = FirebaseUtil.mStorage.getReference().child(article.getImageName());

            picRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d("Delete imageView", "Article Image deleted");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("Delete imageView", e.getMessage());
                }
            });
        }
    }

    private void showImage(String url){
      if (url != null && !url.isEmpty()){

          int width = Resources.getSystem().getDisplayMetrics().widthPixels;
          Picasso.with(this)
                  .load(url)
                  .resize(width, width*2/3)
                  .centerCrop()
                  .into(imageView);
          Log.d("Picasso", "image loaded with picasso");

      }

    }

    private void return2Article(){
        Intent back = new Intent(this, ShowArticlesActivity.class);
        startActivity(back);
    }
}
