package com.psdutta.hmi.photogalaxy.connection;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

final class ServerSocketConnection {
    private static final String TAG = ServerSocketConnection.class.getSimpleName();
    private ServerSocket mServerSocket;
    private Socket mSocket;
    private BufferedInputStream mBufferedInputStream;

    ServerSocketConnection() {
        mServerSocket = null;
        mBufferedInputStream = null;
    }

    void connect(final int port) {
        try {
            close();
            mServerSocket = new ServerSocket(port);
            Log.i(TAG,"Start accepting packet from client ......");
            mSocket = mServerSocket.accept();
            Log.i(TAG, "Started listening packet from client.....");
            mBufferedInputStream = new BufferedInputStream(mSocket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    boolean isConnected() {
        return mSocket != null && mSocket.isConnected();
    }

    void close() {
        try {
            if(mServerSocket != null){
                mServerSocket.close();
            }
            if (mBufferedInputStream != null) {
                mBufferedInputStream.close();
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    byte[] read() throws IOException {
        if (!isConnected()) {
            throw new IOException("Socket not connected");
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int available = mBufferedInputStream.available();
        byte[] buf = new byte[available];
        int n = mBufferedInputStream.read(buf);
        byteArrayOutputStream.write(buf, 0, n);
        byte[] result =  byteArrayOutputStream.toByteArray();
        byteArrayOutputStream.flush();
        byteArrayOutputStream.close();
        return result;
    }
}
