package com.prototypes.test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.prototypes.test.net.InstagramApp;
import com.prototypes.test.util.Constants;

import static com.prototypes.test.util.Constants.CALLBACKURL;
import static com.prototypes.test.util.Constants.CLIENT_ID;
import static com.prototypes.test.util.Constants.CLIENT_SECRET;


public class SplashActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        final InstagramApp mApp = new InstagramApp(this,CLIENT_ID,CLIENT_SECRET,CALLBACKURL);
        mApp.setListener(new InstagramApp.OAuthAuthenticationListener() {
            @Override
            public void onSuccess() {
                if (mApp.hasAccessToken()){
                    Constants.ACCESS_TOKEN = mApp.getAccessToken();
                    Intent intent = new Intent(SplashActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onFail(String error) {
                mApp.authorize();

            }
        });
        mApp.authorize();



    }



}
