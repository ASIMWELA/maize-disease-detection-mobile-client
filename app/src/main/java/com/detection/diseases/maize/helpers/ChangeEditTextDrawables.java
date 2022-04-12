package com.detection.diseases.maize.helpers;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.widget.EditText;

import androidx.annotation.RequiresApi;

import com.detection.diseases.maize.R;

/**
 * @author Augustine
 *
 * A Utility class to manage drawable icons on Edit texts
 */
public class ChangeEditTextDrawables {
    /**
     * Changes drawable icons to reflect errors on inputs
     * @param editText Change drawable icons of this editText
     * @param id Icon Drawable id
     * @param context Context in which all the changes are happening
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static void changeToErrorDrawable(EditText editText, int id, Context context){
        Drawable drawable = context.getResources().getDrawable(id);
        drawable.setTint(context.getResources().getColor(R.color.design_default_color_error));
        editText.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
        editText.setBackgroundResource(R.drawable.rounded_boaders_error);
    }

    /**
     * Changes drawable icons to reflect error free input values
     * @param editText Change drawable icons of this editText
     * @param id Icon Drawable id
     * @param context Context in which all the changes are happening
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static void changeToNormalDrawable(EditText editText, int id, Context context){
        Drawable drawable = context.getResources().getDrawable(id);
        drawable.setTint(context.getResources().getColor(R.color.teal_700));
        editText.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
        editText.setBackgroundResource(R.drawable.input_rounded_corners);
    }

}
