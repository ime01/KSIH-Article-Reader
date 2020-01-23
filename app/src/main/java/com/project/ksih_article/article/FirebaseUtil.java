package com.project.ksih_article.article;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


import java.util.ArrayList;

public class FirebaseUtil {

    public static FirebaseDatabase mFirebaseDatabase;
    public static DatabaseReference mDatabaseReference;
    private static FirebaseUtil firebaseUtil;
    public static ArrayList<ArticleModel> mArticles;
    public static FirebaseStorage mStorage;
    public static StorageReference mStorageRef;

    private FirebaseUtil(){};

    public static void openFbReference (String ref) {
        if (firebaseUtil == null){
            firebaseUtil = new FirebaseUtil();
            mFirebaseDatabase = FirebaseDatabase.getInstance();

        }
        mArticles = new ArrayList<>();
        mDatabaseReference = mFirebaseDatabase.getReference().child(ref);

        connectStorage();
    }

    public static void connectStorage(){
        mStorage = FirebaseStorage.getInstance();
        mStorageRef = mStorage.getReference().child("images");
    }
}
