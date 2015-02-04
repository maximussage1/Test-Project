package com.prototypes.test.net;

import android.content.Context;
import android.webkit.WebView;

import com.octo.android.robospice.request.SpiceRequest;
import static com.prototypes.test.util.Constants.*;
/**
 * Created by renesignoret on 1/30/15.
 */
public class AuthorizationRequest extends SpiceRequest<String>{

    Context context;
    public AuthorizationRequest(String string){
        super(String.class);

    }

    @Override
    public String loadDataFromNetwork() throws Exception {


        return null;
    }


}
