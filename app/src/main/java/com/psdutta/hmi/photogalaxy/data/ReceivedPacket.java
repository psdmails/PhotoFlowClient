package com.psdutta.hmi.photogalaxy.data;

public class ReceivedPacket {
    private final String mRequestType;
    private final String mUri;

    public ReceivedPacket(String requestType, String uri) {
        mRequestType = requestType;
        mUri = uri;
    }

    public String getRequestType() {
        return mRequestType;
    }

    public String getUri() {
        return mUri;
    }
}
