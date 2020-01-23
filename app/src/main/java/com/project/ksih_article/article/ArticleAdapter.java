package com.project.ksih_article.article;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.project.ksih_article.ArticleActivity;
import com.project.ksih_article.R;
import com.project.ksih_article.webView.WebViewActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder> {
    ArrayList<ArticleModel> articles;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private ChildEventListener mChildEventListener;
    private ImageView articleImage;

    public ArticleAdapter (){
        FirebaseUtil.openFbReference("articles");
        mFirebaseDatabase = FirebaseUtil.mFirebaseDatabase;
        mDatabaseReference = FirebaseUtil.mDatabaseReference;

        articles = FirebaseUtil.mArticles;


        mChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                ArticleModel am = dataSnapshot.getValue(ArticleModel.class);
                Log.d("Deal: ", am.getName());
                am.setId(dataSnapshot.getKey());

                articles.add(am);
                notifyItemInserted(articles.size()-1);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

     mDatabaseReference.addChildEventListener(mChildEventListener);

    }


    @NonNull
    @Override
    public ArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();

        View itemView = LayoutInflater.from(context).inflate(R.layout.custom_display, parent, false);

        return new ArticleViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ArticleViewHolder holder, int position) {

        ArticleModel articleModel = articles.get(position);
        holder.bind(articleModel);

    }

    @Override
    public int getItemCount() {
        return articles.size();
    }


    public class ArticleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView name, topic, weblink;
        CardView cardView;
        ArticleModel article;

        public ArticleViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            topic = itemView.findViewById(R.id.topic);
            weblink = itemView.findViewById(R.id.weblink);
            cardView = itemView.findViewById(R.id.cardView);
            articleImage = itemView.findViewById(R.id.writer_image);

            itemView.setOnClickListener(this);

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PopupMenu popup = new PopupMenu(name.getContext(), itemView, Gravity.CENTER_VERTICAL);
                    popup.getMenuInflater().inflate(R.menu.article_popup_menu, popup.getMenu());

                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            switch (menuItem.getItemId()){

                                case R.id.read_artice:
                                    String url = weblink.getText().toString();
                                    Intent intent = new Intent(view.getContext(), WebViewActivity.class);
                                     intent.putExtra("website", url);
                                     view.getContext().startActivity(intent);
                                    return true;

                                case R.id.edit_artice:
                                    int position = getAdapterPosition();
                                    Log.d("Click", String.valueOf(position));

                                    ArticleModel articleModel =  articles.get(position);

                                    Intent intent1 = new Intent(view.getContext(), ArticleActivity.class);
                                    intent1.putExtra("Article", articleModel);
                                    view.getContext().startActivity(intent1);
                                    return true;

                                case R.id.delete_artice:
                                    ArticleActivity article2 = new ArticleActivity();
                                    article2.deleteArticle();
                                    Log.d("Delete", "selected article deleted");
                                    return true;
                            }
                            return true;
                        }
                    });
                    popup.show();
                }
            });


        }



        public void bind (ArticleModel article){
            name.setText(article.getName());
            topic.setText(article.getTitle());
            weblink.setText(article.getWeblink());
            showImage(article.getImageUrl());
        }
        @Override
        public void onClick(View view) {
//            int position = getAdapterPosition();
//            Log.d("Click", String.valueOf(position));
//            ArticleModel articleModel =  articles.get(position);
        }

        }


        private void showImage(String url){
            if (url != null && url.isEmpty() == false){
                Picasso.with(articleImage.getContext())
                        .load(url)
                        .resize(150,150)
                        .centerCrop()
                        .into(articleImage);

            }
        }



}
