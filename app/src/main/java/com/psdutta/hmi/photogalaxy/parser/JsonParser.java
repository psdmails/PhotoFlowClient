package com.psdutta.hmi.photogalaxy.parser;

import android.util.Log;

import com.psdutta.hmi.photogalaxy.data.Constants;
import com.psdutta.hmi.photogalaxy.data.ReceivedPacket;
import com.psdutta.hmi.photogalaxy.data.SendPacket;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonParser {
    private static final String TAG = JsonParser.class.getSimpleName();

    public static ReceivedPacket parse(JSONObject object) {
        ReceivedPacket packet = null;
        try {
            packet = new ReceivedPacket(object.getString(Constants.REQUEST_TYPE), object.getString(Constants.FILE_PATH));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return packet;
    }

    public static byte[] getInitialPacket(){
        JSONObject obj = new JSONObject();
        try {
            obj.put(Constants.REQUEST_TYPE,Constants.INIT);
            obj.put(Constants.INIT_MESSAGE, "HELLO");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return (obj.toString()).getBytes();
    }

    public static byte[] getByteArrayFromList(List<SendPacket> list) {
        JSONArray array = new JSONArray();
        for (SendPacket packet : list) {
            JSONObject obj = new JSONObject();
            try {
                obj.put(Constants.FILE_PATH, packet.getFilePath());
                obj.put(Constants.IMAGE_DATA, packet.getEncodedBitmap());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            array.put(obj);
        }
        JSONObject  object = new JSONObject();
        try {
            object.put(Constants.REQUEST_TYPE,Constants.ALL);
            object.put(Constants.IMAGE_DATA,array);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return (object.toString()).getBytes();
    }

    public static byte[] getByteArrayFromPhotoBitmap(SendPacket sendPacket) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constants.REQUEST_TYPE, Constants.SELECTED);
            jsonObject.put(Constants.IMAGE_DATA, sendPacket.getEncodedBitmap());
            jsonObject.put(Constants.FILE_PATH, sendPacket.getFilePath());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return (jsonObject.toString()).getBytes();
    }
}
