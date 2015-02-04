package com.prototypes.test.net;

import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.prototypes.test.util.Constants;

/**
 * Created by renesignoret on 1/30/15.
 */
public class AuthWebViewClient extends WebViewClient {
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        if (url.startsWith(Constants.CALLBACKURL)) {
            System.out.println(url);
            String parts[] = url.split("=");
            Constants.ACCESS_TOKEN = parts[1];  //This is your request token.
            Log.e("TOKEN", Constants.ACCESS_TOKEN);
            return true;
        }
        return false;
    }
}
