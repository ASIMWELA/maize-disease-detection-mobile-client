package com.detection.diseases.maize.helpers;

import android.app.Activity;
import android.widget.ImageView;

import androidx.appcompat.widget.PopupMenu;

public class CustomOptionsMenu {
    public static PopupMenu prepareOptionsMenu(Activity activity, ImageView togler, int menuId){
        PopupMenu pm = new PopupMenu(activity, togler);
        pm.getMenuInflater().inflate(menuId, pm.getMenu());
        return pm;
    }
}
