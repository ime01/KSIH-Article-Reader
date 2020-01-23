package com.project.ksih_article.webView;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import com.project.ksih_article.R;

public class WebViewActivity extends AppCompatActivity {
    WebView webView;
    Button forward, back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view2);

        webView = findViewById(R.id.web_view);
        back = findViewById(R.id.back);
        forward = findViewById(R.id.forward);

        WebSettings webSettings = webView.getSettings();
        webView.setWebViewClient(new WebViewClient());
        webSettings.setJavaScriptEnabled(true);

        String website = getIntent().getStringExtra("website");

        webView.loadUrl("https://"+ website);


        forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                forward();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                back();
            }
        });
    }

    public void back() {
        if (webView.canGoBack()){
            webView.goBack();
        }else {return ;
        }

    }

    public void forward() {
        if (webView.canGoForward()){
            webView.goForward();
        }else {
            super.onBackPressed();
        }

    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()){
            webView.goBack();
        }else {
            super.onBackPressed();
        }
    }


}
