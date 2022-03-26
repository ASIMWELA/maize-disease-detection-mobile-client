package com.detection.diseases.maize.helpers;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Set;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class SessionManager {
    final SharedPreferences sharedPreferences;
    final SharedPreferences.Editor editor;

    public SessionManager(Context context){
        sharedPreferences = context.getSharedPreferences("maize-disease", 0);
        editor = sharedPreferences.edit();
        editor.apply();
    }
    public void setLoggedInUser(String userObj){
        editor.putString(AppConstants.LOGGED_IN_USER_SESSION_KEY, userObj).commit();
    }

    public String getLoggedInUser(){
       return sharedPreferences.getString(AppConstants.LOGGED_IN_USER_SESSION_KEY, null);
    }
    public void setAccessToken(String token){
        editor.putString(AppConstants.API_ACCESS_TOKEN, token).commit();
    }
    public String getToken(){
        return sharedPreferences.getString(AppConstants.API_ACCESS_TOKEN, null);
    }

    public void setUserRole(String role){
        editor.putString(AppConstants.USER_ROLE, role).commit();
    }
    public String getUserRole(){
        return sharedPreferences.getString(AppConstants.USER_ROLE, null);
    }

}