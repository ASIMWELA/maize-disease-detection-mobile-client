package com.detection.diseases.maize.helpers;


import android.content.Context;

/**
 * @author Augustine
 * <p>
 * A utility class that Checks if a user is logged in or not
 */
public class CheckUserSession {

    /**
     * Check is logged in or not
     * @param context checks session context
     * @return true if logged in otherwise false
     */
    public static boolean isUserLoggedIn(Context context){
        SessionManager sessionManager = new SessionManager(context);
        return sessionManager.getLoggedInUser() != null && sessionManager.getToken()!= null;
    }
}
