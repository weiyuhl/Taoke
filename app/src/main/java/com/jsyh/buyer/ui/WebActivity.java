package com.jsyh.buyer.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.jsyh.buyer.MainActivity;
import com.jsyh.buyer.R;
import com.jsyh.buyer.ui.splash.SplashActivity;

public class WebActivity extends AppCompatActivity {


    private WebView webView;
    private ImageView back;
    private TextView title;

    public static final int adb_type = 1;
    public static final int jpush_type = 2;

    private int type ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jpush_web_activity);

        back = (ImageView) findViewById(R.id.back);
        title = (TextView) findViewById(R.id.title);


        Bundle extras = getIntent().getExtras();
        String url = extras.getString("url");
        String titleStr = extras.getString("title");

        type = extras.getInt("type");

        webView = (WebView) findViewById(R.id.webview);

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);


        webView.setWebChromeClient(new WebChromeClient());

        webView.setWebViewClient(new WebViewClient());

        title.setText(titleStr);

        webView.loadUrl(url);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type == adb_type) {
                    Intent intent = new Intent(WebActivity.this, MainActivity.class);
                    startActivity(intent);
                }
                finish();
            }
        });

    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK&&type == adb_type) {
            Intent intent = new Intent(WebActivity.this, MainActivity.class);
            startActivity(intent);
            finish();

            return true;
        }
        return super.onKeyUp(keyCode, event);
    }
}
