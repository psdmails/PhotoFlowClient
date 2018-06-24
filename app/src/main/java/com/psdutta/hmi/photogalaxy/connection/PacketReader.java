package com.psdutta.hmi.photogalaxy.connection;

import android.util.Log;

import com.psdutta.hmi.photogalaxy.queue.PacketQueueManager;

import java.io.IOException;

final class PacketReader implements Runnable {
    private static final String TAG = PacketReader.class.getSimpleName();
    private final ServerSocketConnection mServerSocketConnection;
    private final PacketQueueManager mPacketQueueManager;

    PacketReader(ServerSocketConnection connection, PacketQueueManager manager) {
        mServerSocketConnection = connection;
        mPacketQueueManager = manager;
    }

    @Override
    public final void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                if (!mServerSocketConnection.isConnected()) {
                    Thread.sleep(100);
                    continue;
                }
                final byte[] receiveSizeBytes = mServerSocketConnection.read();
                if(receiveSizeBytes != null && receiveSizeBytes.length > 0) {
                    Log.i(TAG,"receiveSizeBytes :"+receiveSizeBytes.length);
                    mPacketQueueManager.addPacketIntoRequestQueue(receiveSizeBytes);
                }
                Thread.sleep(100);
            }
        } catch (final IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}