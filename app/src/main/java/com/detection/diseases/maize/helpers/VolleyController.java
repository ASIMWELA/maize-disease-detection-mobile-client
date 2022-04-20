package com.detection.diseases.maize.helpers;

import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * @author Augustine
 *
 * An Http client helper
 *
 * Builds on top of volley and implements singleton design pattern to efficiently utilise resources
 */
public class VolleyController    {
    /**
     * An instance of the class. The only instance to be created and returned throughout the app
     */
    private static VolleyController mInstance;

    /**
     * Volley request queue. Enable non blocking network operations. Dispatches the calls on non-UI thread
     */
    private RequestQueue mRequestQueue;

    /**
     * The context where the class will be used
     */
    private static Context mCtx;

    /**
     * A constructor for creating an object of this class
     *
     * @param context  The context where the instance will be created
     */
    private VolleyController(Context context) {
        mCtx = context;
        mRequestQueue = getRequestQueue();
    }

    /**
     * Singleton implementation
     *
     * If instance is not available, create it. If available, reuse and return the object.
     *
     * @param context The context where the instance will be created
     *
     * @return An Object of this class
     */
    public static synchronized VolleyController getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new VolleyController(context);
        }
        return mInstance;
    }

    /**
     * Create a single queue for all the request
     *
     * Helps in easy management
     *
     * @return RequestQueue for the wholw app
     */
    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key. It should not be activity context,
            // or else RequestQueue won’t last for the lifetime of your app
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());

        }
        return mRequestQueue;
    }

    /**
     * Add any type of request to a queue
     *
     * @param req The actual request
     *
     * @param <T> The return type of the request
     */
    public <T> void addToRequestQueue(Request<T> req) {

        req.setRetryPolicy((new DefaultRetryPolicy(
                        30000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)));
        getRequestQueue().add(req);
    }

}