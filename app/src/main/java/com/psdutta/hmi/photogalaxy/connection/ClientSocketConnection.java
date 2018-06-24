package com.psdutta.hmi.photogalaxy.connection;

import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

final class ClientSocketConnection {
    private static final String TAG = ClientSocketConnection.class.getSimpleName();
    private Socket mClientSocket;
    private BufferedOutputStream mBufferedOutputStream;
    private DataOutputStream mDataOutputStream;

    ClientSocketConnection() {
        mClientSocket = null;
        mDataOutputStream = null;
        mBufferedOutputStream = null;
    }

    void connect(final String ip, final int port) {
        try {
            close();
            mClientSocket = new Socket();
            mClientSocket.bind(null);
            mClientSocket.connect(new InetSocketAddress(ip, port), 5000);
            mBufferedOutputStream = new BufferedOutputStream(mClientSocket.getOutputStream());
            mDataOutputStream = new DataOutputStream(mClientSocket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    boolean isConnected() {
        return mClientSocket != null && mClientSocket.isConnected();
    }

    void close() {
        try {
            if (mClientSocket != null) {
                mClientSocket.close();
            }
            if (mBufferedOutputStream != null) {
                mBufferedOutputStream.close();
            }

            if (mDataOutputStream != null) {
                mDataOutputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    void write(byte[] bytes) throws IOException {
        if (!isConnected()) {
            throw new IOException("Socket not connected");
        }
        Log.d(TAG, "write: sending data");
        String str = new String(bytes);
        mDataOutputStream.writeBytes(str +"\n");
        Log.d(TAG, "write: done");
    }

    void flush() throws IOException {
        /*if (!isConnected()) {
            throw new IOException("Socket not connected");
        }
        mDataOutputStream.flush();*/
    }
}
