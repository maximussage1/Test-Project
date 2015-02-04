package com.prototypes.test;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.octo.android.robospice.request.simple.SimpleTextRequest;
import com.prototypes.test.adapter.MainAdapter;
import com.prototypes.test.base.BaseTestActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends BaseTestActivity {
    private static final String BASE_HASH_TAG_REQUEST = "https://api.instagram.com//v1//tags//selfie//media//recent?access_token=1684067763.567d84f.5e85b006378d434586699c08591af08a";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new ProgressBar(this));
        getSpiceManager().execute(new SimpleTextRequest(BASE_HASH_TAG_REQUEST), new InitialTextRequestListener());


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            ((ListView)findViewById(R.id.listView)).smoothScrollToPosition(0);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private class InitialTextRequestListener implements RequestListener<String>{


        @Override
        public void onRequestFailure(SpiceException spiceException) {

        }

        @Override
        public void onRequestSuccess(final String s) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    setContentView(R.layout.activity_main);
                    ListView mainView = (ListView) findViewById(R.id.listView);
                    mainView.setLayoutParams( new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT));
                    try{
                        JSONObject mainObject = new JSONObject(s);
                        JSONArray mainArray = mainObject.getJSONArray("data");
                        MainAdapter adapter = new MainAdapter(MainActivity.this,getSpiceManager(),mainArray,mainObject.getJSONObject("pagination").getString("next_url"));
                        mainView.setAdapter(adapter);
                    }catch(JSONException e){
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}

