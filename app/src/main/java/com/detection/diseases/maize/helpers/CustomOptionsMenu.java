package com.detection.diseases.maize.helpers;

import android.app.Activity;
import android.widget.ImageView;

import androidx.appcompat.widget.PopupMenu;

/**
 * @author Augustine
 * <p>
 * A Utility class that creates custom options menu
 */
public class CustomOptionsMenu {

    /**
     * A static methos that creates and returns a PopUP menu
     * @param activity An activity or Flagment where the popmenu is
     * @param togler A View that triggers the menu when clicked
     * @param menuId An id of the menu file
     * @return an {@link PopupMenu}
     */
    public static PopupMenu prepareOptionsMenu(Activity activity, ImageView togler, int menuId){
        PopupMenu pm = new PopupMenu(activity, togler);
        pm.getMenuInflater().inflate(menuId, pm.getMenu());
        return pm;
    }
}
