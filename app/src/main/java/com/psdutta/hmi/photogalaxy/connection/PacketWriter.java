package com.psdutta.hmi.photogalaxy.connection;

import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

final class PacketWriter implements Runnable {
    private static final String TAG = PacketWriter.class.getSimpleName();
    private final ClientSocketConnection mClientSocketConnection;
    private final Queue<byte[]> mQueue = new ConcurrentLinkedQueue<>();

    static final String star = "**";

    PacketWriter(final ClientSocketConnection connection) {
        mClientSocketConnection = connection;
    }

    @Override
    public final void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                if (mClientSocketConnection.isConnected()) {
                    if (mQueue.size() > 0) {
                        final byte[] sendBytes = mQueue.remove();
                        Log.i(TAG,"sendBytes psd"+sendBytes);
                        mClientSocketConnection.write(sendBytes);
                        //mClientSocketConnection.flush();

                        //mClientSocketConnection.write(star.getBytes());
                        mClientSocketConnection.flush();


                    }
                }


                mClientSocketConnection.flush();
                Thread.sleep(100);
            }
        } catch (final IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    void addDataIntoQueue(byte[] packet){
        mQueue.add(packet);
    }
}
