package com.psdutta.hmi.photogalaxy.data;

import android.content.Context;
import android.database.Cursor;
import android.os.Environment;
import android.provider.MediaStore;

import java.util.ArrayList;
import java.util.List;


class PhotoListProvider {
    private static final String CAMERA_IMAGE_BUCKET_NAME = Environment.getExternalStorageDirectory().toString() + "/DCIM/Camera";
    private static final String CAMERA_IMAGE_BUCKET_ID = getBucketId(CAMERA_IMAGE_BUCKET_NAME);
    private static final int MAX_DATA = 30;

    private static String getBucketId(String path) {
        return String.valueOf(path.toLowerCase().hashCode());
    }

    static List<String> getCameraImages(Context context) {
        final String[] projection = {MediaStore.Images.Media.DATA};
        final String selection = MediaStore.Images.Media.BUCKET_ID + " = ?";
        final String[] selectionArgs = {CAMERA_IMAGE_BUCKET_ID};
        final Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection,
                selection,
                selectionArgs,
                MediaStore.Images.Media.DATE_TAKEN + " DESC");
        List<String> result = null;
        if (cursor != null) {
            int n = cursor.getCount();

            if(n>MAX_DATA) {
                n = MAX_DATA;
            }
            int i = 0;
            result = new ArrayList<>(n);
            if (cursor.moveToFirst()) {
                final int dataColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

                do {
                    final String data = cursor.getString(dataColumn);
                    result.add(data);
                    i++;

                    if(i==n) {
                        break;
                    }
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        return result;
    }
}
