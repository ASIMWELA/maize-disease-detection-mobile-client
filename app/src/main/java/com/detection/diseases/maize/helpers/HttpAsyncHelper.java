package com.detection.diseases.maize.helpers;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;

/**
 * @author Augustine
 *
 * A singleton Http client that helps is sending requests
 */
public class HttpAsyncHelper {

    /**
     * Creating an object of this class that will be returned for all requests
     */
    private static HttpAsyncHelper instance = null;

    /**
     * The actual Http Client
     */
    private AsyncHttpClient client;
    private Context context;

    public HttpAsyncHelper(Context context) {
        this.context = context;
    }

    /**
     * Singleton implementation
     *
     * @param context Context where the request will executed
     *
     * @return {@link HttpAsyncHelper}
     */
    public static synchronized HttpAsyncHelper getInstance(Context context){
        if(instance == null){
            return new HttpAsyncHelper(context);
        }
        return instance;
    }

    /**
     * Create an object only once
     *
     * @return {@link AsyncHttpClient}
     */
    public AsyncHttpClient getHttpClient(){
        if(client == null){
            client = new AsyncHttpClient();
        }
        client.setResponseTimeout(70000);
        client.setTimeout(70000);
        return client;
    }

}
