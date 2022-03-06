package com.detection.diseases.maize.helpers;


import android.content.Context;

public class CheckUserSession {
    public static boolean isUserLoggedIn(Context context){
        SessionManager sessionManager = new SessionManager(context);
        return sessionManager.getLoggedInUser() != null && sessionManager.getToken()!= null;
    }
}
