package com.detection.diseases.maize.helpers;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.widget.EditText;

import androidx.annotation.RequiresApi;

import com.detection.diseases.maize.R;

public class ChangeEditTextDrawables {
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static void changeToErrorDrawable(EditText editText, int id, Context context){
        Drawable drawable = context.getResources().getDrawable(id);
        drawable.setTint(context.getResources().getColor(R.color.design_default_color_error));
        editText.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
        editText.setBackgroundResource(R.drawable.rounded_boaders_error);
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static void changeToNormalDrawable(EditText editText, int id, Context context){
        Drawable drawable = context.getResources().getDrawable(id);
        drawable.setTint(context.getResources().getColor(R.color.design_default_color_primary));
        editText.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
        editText.setBackgroundResource(R.drawable.input_rounded_corners);
    }

}
