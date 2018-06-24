package com.psdutta.hmi.photogalaxy.data;

public class SendPacket {
    private final String filePath;
    private final String encodedBitmap;

    public SendPacket(String path, String enBitmap) {
        filePath = path;
        encodedBitmap = enBitmap;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getEncodedBitmap() {
        return encodedBitmap;
    }
}
