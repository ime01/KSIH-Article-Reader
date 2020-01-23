package com.project.ksih_article;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.project.ksih_article.article.ArticleAdapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ShowArticlesActivity extends AppCompatActivity {

    FloatingActionButton fab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_articles);

        fab = findViewById(R.id.fab);

        RecyclerView rvArticles = findViewById(R.id.rv_articles);
        final ArticleAdapter adapter = new ArticleAdapter();
        rvArticles.setAdapter(adapter);

        LinearLayoutManager articlelayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        rvArticles.setLayoutManager(articlelayoutManager);


    }

    public void onFabClicked(View view) {
        insertNewArticle();
    }

    private void insertNewArticle() {
        Intent intent = new Intent(this, ArticleActivity.class);
        startActivity(intent);
    }
}
