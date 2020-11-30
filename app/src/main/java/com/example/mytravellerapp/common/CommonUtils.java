package com.example.mytravellerapp.common;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.loader.content.CursorLoader;

import com.example.mytravellerapp.BaseApplication;

public class CommonUtils {
    private static CommonUtils instance = null;

    private CommonUtils() {}

    public static CommonUtils getInstance() {
        if (instance == null) instance = new CommonUtils();
        return instance;
    }

    public boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) BaseApplication.getBaseApplication().getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    public void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    //upload via gallery
    public String getPathFromUriGallery(Context context, Uri uri) {
        Cursor cursor = null;
        try {
            String wholeID = DocumentsContract.getDocumentId(uri);
            String id = wholeID.split(":")[1];
            String sel = MediaStore.Images.Media._ID + "=?";

            String[] filePath = {MediaStore.Images.Media.DATA};
            /*Uri, projection(A list of which columns to return), selection(A filter declaring which rows to return), selectionArgs, sortOrder*/
            cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, filePath, sel, new String[]{id}, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePath[0]);
            return  cursor.getString(columnIndex);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    //upload via camera
    public String getPathFromUri(Context context, Uri uri) {
        Cursor cursor = null;
        try {
            String[] data = {MediaStore.Images.Media.DATA};
            CursorLoader loader = new CursorLoader(context, uri, data, null, null, null);
//            CursorLoader loader = new CursorLoader(context, uri, data, null, null, null);
            cursor = loader.loadInBackground();
            int column_index = cursor.getColumnIndex(data[0]);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
}
