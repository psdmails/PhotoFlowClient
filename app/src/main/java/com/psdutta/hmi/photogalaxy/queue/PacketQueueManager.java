package com.psdutta.hmi.photogalaxy.queue;

import com.psdutta.hmi.photogalaxy.connection.ConnectionManager;
import com.psdutta.hmi.photogalaxy.event.EventManager;
import com.psdutta.hmi.photogalaxy.event.IOnEventProcessedListener;

public class PacketQueueManager implements IOnEventProcessedListener{
    private static final Object LOCK_OBJECT = new Object();
    private static PacketQueueManager mPacketQueueManager;
    private ConnectionManager mConnectionManager;
    private PacketRequestQueueHandler mPacketRequestQueueHandler;
    private EventManager mEventManager;

    private PacketQueueManager() {
        mEventManager = EventManager.getInstance();
        mEventManager.registerListener(this);
        mPacketRequestQueueHandler = new PacketRequestQueueHandler(mEventManager);
    }

    public static PacketQueueManager getInstance() {
        if (mPacketQueueManager == null) {
            synchronized (LOCK_OBJECT) {
                if (mPacketQueueManager == null) {
                    mPacketQueueManager = new PacketQueueManager();
                }
            }
        }
        return mPacketQueueManager;
    }

    public void setConnectManager(ConnectionManager connectManager){
        mConnectionManager = connectManager;
    }

    public void addPacketIntoRequestQueue(byte[] packet){
        mPacketRequestQueueHandler.addRequestIntoQueue(packet);
    }

    public void sendPacket(byte[] packet){
        mConnectionManager.sendPacket(packet);
    }

    @Override
    public void onEventProcessed(byte[] packet) {
        sendPacket(packet);
    }
}
