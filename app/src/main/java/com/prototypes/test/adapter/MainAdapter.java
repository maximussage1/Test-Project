package com.prototypes.test.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.octo.android.robospice.request.listener.RequestProgress;
import com.octo.android.robospice.request.listener.RequestProgressListener;
import com.octo.android.robospice.request.simple.BitmapRequest;
import com.octo.android.robospice.request.simple.SimpleTextRequest;
import com.prototypes.test.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import javax.xml.datatype.Duration;

import roboguice.util.temp.Ln;

/**
 * Created by renesignoret on 1/30/15.
 */
public class MainAdapter extends BaseAdapter implements View.OnClickListener {
    private static final String PICTURE_IMAGE_CACHE_KEY = "image";

    private final SpiceManager manager;
    private final JSONArray items;
    private final Context context;
    private final String nextURL;

    public MainAdapter(Context context, SpiceManager manager, JSONArray items, String nextURL){
       super();
        this.context = context;
        this.manager = manager;
        this.nextURL = nextURL;
        this.items = items;
    }

    @Override
    public int getCount() {
        return  items.length() - (items.length()/10*3) ;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private void Download(){
        manager.execute(new SimpleTextRequest(nextURL),new TextRequestListener() );
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.e("position",""+position+ " , and the count "+getCount() );
        if (position == getCount()-1){
            ProgressBar bar = new ProgressBar(context);
            Download();
            return bar;
        }else{
            LayoutInflater inflater =  (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View container = inflater.inflate(R.layout.list_item,parent,false);
            ImageView view;
            ImageView view2;
            if (convertView == null || (convertView instanceof  ProgressBar)){
                view = (ImageView) container.findViewById(R.id.imageView1);
                view2 = (ImageView)container.findViewById(R.id.imageView2);
                view.setPadding(8, 8, 8, 8);
            }else{
                view = (ImageView)  convertView.findViewById(R.id.imageView1);
                view2 = (ImageView)convertView.findViewById(R.id.imageView2);

            }
            view.setAdjustViewBounds(true);
            view2.setAdjustViewBounds(true);
            boolean isBig = position == 0 || position%2 == 0;

                if (isBig){
                view2.setVisibility(View.GONE);
            }else{
                view2.setVisibility(View.VISIBLE);
            }

            BitmapRequest imageRequest;
            BitmapRequest imageRequest2;

            try {
                if (isBig){

                    int realPosition = position/2 *3;
                    File cacheFile = new File(context.getCacheDir(), "photo"+realPosition+".jpg");

                    Log.e("realPosition"+ realPosition, "Position" + position);
                    imageRequest = new BitmapRequest(items.getJSONObject(realPosition).getJSONObject("images")
                            .getJSONObject("standard_resolution").getString("url"),cacheFile);
                    manager.execute(imageRequest,PICTURE_IMAGE_CACHE_KEY + items.getJSONObject(realPosition).getString("id"),5 * DurationInMillis.ONE_HOUR,new ImageRequestListener(container,isBig));

                }else{
                    int realPosition = Math.round(position/2 *3)+1;
                    File cacheFile = new File(context.getCacheDir(), "photo"+realPosition+".jpg");
                    File cacheFile2 = new File(context.getCacheDir(), "photo"+realPosition+1+".jpg");

                    Log.e("realPosition"+ realPosition, "Position" + position);

                    imageRequest = new BitmapRequest(items.getJSONObject(realPosition).getJSONObject("images")
                            .getJSONObject("low_resolution").getString("url"),cacheFile);
                    imageRequest2 = new BitmapRequest(items.getJSONObject(realPosition+1).getJSONObject("images")
                            .getJSONObject("low_resolution").getString("url"),cacheFile2);
                    manager.execute(imageRequest,PICTURE_IMAGE_CACHE_KEY + items.getJSONObject(realPosition).getString("id"),5 * DurationInMillis.ONE_HOUR,new ImageRequestListener(container,isBig));
                    manager.execute(imageRequest2,PICTURE_IMAGE_CACHE_KEY + items.getJSONObject(realPosition+1).getString("id"),5 * DurationInMillis.ONE_HOUR,new ImageRequestListener(container,isBig,2));

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return container;
        }

    }

    @Override
    public void onClick(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        ImageView view = new ImageView(context);
        view.setImageDrawable(((ImageView) v).getDrawable());
        builder.setView(view);
        builder.create().show();
        view.setAdjustViewBounds(true);
        view.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,FrameLayout.LayoutParams.MATCH_PARENT));

    }


    public final class ImageRequestListener implements RequestListener<Bitmap>, RequestProgressListener {
        final View view;
        final boolean isBig;
        final int position;
        public ImageRequestListener(View view, boolean isBig,int position){
            this.view = view;
            this.isBig = isBig;
            this.position= 2;
        }
        public ImageRequestListener(View view, boolean isBig){
            this.view = view;
            this.isBig = isBig;
            this.position= 1;
        }
        @Override
        public void onRequestFailure(SpiceException spiceException) {
            spiceException.printStackTrace();
        }

        @Override
        public void onRequestSuccess(final Bitmap bitmap) {
            ImageView image1 = (ImageView)view.findViewById(R.id.imageView1) ;
            ImageView image2 = (ImageView)view.findViewById(R.id.imageView2);
            if (position ==1 ){
                image1.setImageBitmap(bitmap);

            }else{
                image2.setImageBitmap(bitmap);

            }
           image1.setAdjustViewBounds(true);
            image2.setAdjustViewBounds(true);
            if (isBig){
                image2.setVisibility(View.GONE);

            }
            image1.setOnClickListener(MainAdapter.this);
            image2.setOnClickListener(MainAdapter.this);






        }

        @Override
        public void onRequestProgressUpdate(RequestProgress progress) {

        }
    }

    private final class TextRequestListener implements RequestListener<String>

    {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            spiceException.printStackTrace();
        }

        @Override
        public void onRequestSuccess(final String result) {
            try {
                Log.i("results",result);
                JSONArray newItems = new JSONObject(result).getJSONArray("data");
                for (int x = 0;x<newItems.length();x++) {
                      items.put(newItems.getJSONObject(x));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            notifyDataSetChanged();

        }
    }

}
