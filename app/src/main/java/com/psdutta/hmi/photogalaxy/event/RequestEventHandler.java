package com.psdutta.hmi.photogalaxy.event;

import com.psdutta.hmi.photogalaxy.data.BitmapGenerator;
import com.psdutta.hmi.photogalaxy.data.Constants;
import com.psdutta.hmi.photogalaxy.data.ReceivedPacket;
import com.psdutta.hmi.photogalaxy.data.SendPacket;
import com.psdutta.hmi.photogalaxy.parser.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

class RequestEventHandler {

    private EventManager.EventResponseCallback mCallback;

    RequestEventHandler(EventManager.EventResponseCallback callback) {
        mCallback = callback;
    }

    void processEvent(byte[] packet) {
        try {
            JSONObject jsonObject = new JSONObject(new String(packet));
            ReceivedPacket object = JsonParser.parse(jsonObject);
            List<byte[]> packetToBeSent = getDataForRequest(object);
            for (byte[] bytes : packetToBeSent) {
                mCallback.onEventProcessed(bytes);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private List<byte[]> getDataForRequest(ReceivedPacket packet) {
        if (packet.getRequestType().equals(Constants.ALL)) {
            List<SendPacket> thumbNailList = BitmapGenerator.getAllThumbnail();
            // Group of 5 thumbnails
            int processedIndex = 0;
            List<byte[]> listTosend = new ArrayList<>();
            while (processedIndex < thumbNailList.size()) {
                int length = thumbNailList.size() - processedIndex;
                if (length > 5) {
                    length = 5;
                }
                List<SendPacket> packetPart = thumbNailList.subList(processedIndex,
                        processedIndex+length);
                listTosend.add(JsonParser.getByteArrayFromList(packetPart));
                processedIndex += length;
            }
            return listTosend;
        } else {
            List<byte[]> byteList = new ArrayList<>();
            SendPacket sendPacket = BitmapGenerator.getEncodedBitMapFromFile(packet.getUri());
            byteList.add(JsonParser.getByteArrayFromPhotoBitmap(sendPacket));
            return byteList;
        }
    }
}
