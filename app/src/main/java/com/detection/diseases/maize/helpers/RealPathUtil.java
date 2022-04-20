package com.detection.diseases.maize.helpers;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

import androidx.annotation.RequiresApi;
import androidx.loader.content.CursorLoader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Augustine
 *
 * A Utility class for accessing real path of an image from gallery
 */
public class RealPathUtil {

    /**
     * Get path for devices running sdk 19 and above
     *
     * @param context The context where the class is used
     *
     * @param uri the uri to get the path from
     *
     * @return A string representing the path
     */
    @SuppressLint("NewApi")
    public static String getRealPathFromURI_API19(Context context, Uri uri) {
        String filePath = "";
        String wholeID = DocumentsContract.getDocumentId(uri);

        // Split at colon, use second item in the array
        String id = wholeID.split(":")[1];

        String[] column = {MediaStore.Images.Media.DATA};

        // where id is equal to
        String sel = MediaStore.Images.Media._ID + "=?";

        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                column, sel, new String[]{id}, null);

        int columnIndex = cursor.getColumnIndex(column[0]);

        if (cursor.moveToFirst()) {
            filePath = cursor.getString(columnIndex);
        }
        cursor.close();
        return filePath;
    }


    /**
     * Get path for devices running sdk between 11 and 18
     *
     * @param context The context where the class is used
     *
     * @param contentUri the uri to get the path from
     *
     * @return A string representing the path
     */
    @SuppressLint("NewApi")
    public static String getRealPathFromURI_API11to18(Context context, Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        String result = null;

        CursorLoader cursorLoader = new CursorLoader(
                context,
                contentUri, proj, null, null, null);
        Cursor cursor = cursorLoader.loadInBackground();

        if (cursor != null) {
            int column_index =
                    cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            result = cursor.getString(column_index);
        }
        return result;
    }

    /**
     * Get all path of images from gallery
     *
     * @param activity The activity that requests access for images from gallery
     *
     * @return List image Files representing images
     */
    @SuppressLint("Recycle")
    @RequiresApi(api = Build.VERSION_CODES.Q)
    public static List<File> getAllShownImagesPath(Activity activity) {

        List<File> listOfAllImages = new ArrayList<>();

        boolean isSDPresent = android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
        Cursor cursor;
        int column_index_data;
        String[] projection = {MediaStore.MediaColumns.DATA};
        String orderBy = MediaStore.Images.Media.DATE_TAKEN + " DESC";

        //GET IMAGES FROM EXTERNAL SD CARD
        if(isSDPresent){
            cursor = activity.getContentResolver().query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    projection,
                    null,
                    null,
                    orderBy);

            column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            while (cursor.moveToNext()) {
                File file = new File(cursor.getString(column_index_data));
                listOfAllImages.add(file);
            }
            cursor.close();
        }

        //GET IMAGES FROM ITERNAL CARD
        cursor = activity.getContentResolver().query(
                MediaStore.Images.Media.INTERNAL_CONTENT_URI,
                projection,
                null,
                null,
                orderBy);

        column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        while (cursor.moveToNext()) {
            File file = new File(cursor.getString(column_index_data));
            listOfAllImages.add(file);
        }
        cursor.close();

        return listOfAllImages;
    }

}