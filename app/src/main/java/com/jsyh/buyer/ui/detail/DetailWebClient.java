package com.jsyh.buyer.ui.detail;

import android.content.Context;
import android.graphics.Bitmap;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.jsyh.buyer.utils.L;
import com.jsyh.buyer.widget.LoadDialog;

/**
 * Created by mo on 17-4-20.
 */

public class DetailWebClient extends WebViewClient {



    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
    }
}
