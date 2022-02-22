package com.detection.diseases.maize.helpers;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;

public class HttpAsyncHelper {
    private static HttpAsyncHelper instance = null;
    private AsyncHttpClient client;
    private Context context;

    public HttpAsyncHelper(Context context) {
        this.context = context;
    }

    public static synchronized HttpAsyncHelper getInstance(Context context){
        if(instance == null){
            return new HttpAsyncHelper(context);
        }
        return instance;
    }
    public AsyncHttpClient getHttpClient(){
        if(client == null){
            client = new AsyncHttpClient();
        }
        client.setResponseTimeout(70000);
        client.setTimeout(70000);
        return client;
    }

}
