package com.detection.diseases.maize.helpers;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Set;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

/**
 * @author Augustine
 *
 * A Utility class for keeping user session
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SessionManager {
    /**
     * Helps to remember user session even after exiting the application
     */
    final SharedPreferences sharedPreferences;

    /**
     * Helps to write and commit changes into shared preferences
     */
    final SharedPreferences.Editor editor;

    /**
     * Constructor for creating object of this class
     *
     * @param context The general context where the session object will be initialised.
     *
     */
    public SessionManager(Context context){
        sharedPreferences = context.getSharedPreferences("maize-disease", 0);
        editor = sharedPreferences.edit();
        editor.apply();
    }

    /**
     * Write a logged in user into the session
     *
     * @param userObj A Stringified logged in user object
     */
    public void setLoggedInUser(String userObj){
        editor.putString(AppConstants.LOGGED_IN_USER_SESSION_KEY, userObj).commit();
    }

    /**
     * Get A logged in user from session
     *
     * @return Stringified logged in user object
     */
    public String getLoggedInUser(){
       return sharedPreferences.getString(AppConstants.LOGGED_IN_USER_SESSION_KEY, null);
    }

    /**
     * Write an access token into session
     *
     * @param token The token to be passed into session
     */
    public void setAccessToken(String token){
        editor.putString(AppConstants.API_ACCESS_TOKEN, token).commit();
    }

    /**
     * Get a token from session
     *
     * @return An Access token
     */
    public String getToken(){
        return sharedPreferences.getString(AppConstants.API_ACCESS_TOKEN, null);
    }

    /**
     * Set user role into session
     *
     * @param role The actual user role to be written into session
     */
    public void setUserRole(String role){
        editor.putString(AppConstants.USER_ROLE, role).commit();
    }

    /**
     * Get user role from session
     *
     * @return user role name
     */
    public String getUserRole(){
        return sharedPreferences.getString(AppConstants.USER_ROLE, null);
    }

}