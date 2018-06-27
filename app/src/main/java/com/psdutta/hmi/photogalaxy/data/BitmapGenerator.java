package com.psdutta.hmi.photogalaxy.data;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import com.psdutta.hmi.photogalaxy.ApplicationClass;
import com.kosalgeek.android.imagebase64encoder.ImageBase64;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class BitmapGenerator {
    private static final int THUMBNAIL_SIZE = 128;
    private static final String TAG = BitmapGenerator.class.getSimpleName();

    public static List<SendPacket> getAllThumbnail() {
        List<SendPacket> thumbNailList = new ArrayList<>();
        final List<String> urlList = PhotoListProvider.getCameraImages(ApplicationClass.getContext());
        if(urlList != null) {
            for (String url : urlList){
                Bitmap bitmap = BitmapFactory.decodeFile(url);
                float width = bitmap.getWidth();
                float height = bitmap.getHeight();
                Float ratio = width / height;
                bitmap = Bitmap.createScaledBitmap(bitmap, (int) (THUMBNAIL_SIZE * ratio), THUMBNAIL_SIZE, false);
                String encodedString = bitmapToString(bitmap);
                Log.i(TAG, "Encoded bit map " + encodedString);
                thumbNailList.add(new SendPacket(base64Encode(url), encodedString));
                bitmap.recycle();
            }
        }
        return thumbNailList;
    }

    private static String bitmapToString(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] b = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    public static SendPacket getEncodedBitMapFromFile(String filePath) {
        Log.d(TAG, "getEncodedBitMapFromFile file path: "+filePath);
        Bitmap bitmap = BitmapFactory.decodeFile(base64Decode(filePath));
        float width = bitmap.getWidth();
        float height = bitmap.getHeight();
        Float ratio = width / height;
        bitmap = Bitmap.createScaledBitmap(bitmap, (int) (4*THUMBNAIL_SIZE * ratio),
                4*THUMBNAIL_SIZE, false);
        String encodedString = bitmapToString(bitmap);
        Log.i(TAG, "Encoded bit map " + encodedString);
        bitmap.recycle();
        return new SendPacket(filePath, encodedString);
    }

    public static byte[] bitmapToByte(Bitmap bitmap) {
        // you can change the format of you image compressed for what do you want;
        // now it is set up to 640 x 480;
        Bitmap bmpCompressed = Bitmap.createScaledBitmap(bitmap, 640, 480, true);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        // CompressFormat set up to JPG, you can change to PNG or whatever you want;
        boolean isCompressSuccess = bmpCompressed.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        Log.d(TAG, "bitmapToByte() called with: bitmap isCompressSuccess = " + isCompressSuccess);
        byte[] data = bos.toByteArray();

        Log.d(TAG, "bitmapToByte() called. Returning data= \n" + data   +"\n \n<END>\n \n");

        return data;
    }

    private static String base64Encode(String token) {
        return Base64.encodeToString(token.getBytes(), Base64.DEFAULT);
    }


    private static String base64Decode(String token) {
        return new String(Base64.decode(token, Base64.DEFAULT));
    }
}
