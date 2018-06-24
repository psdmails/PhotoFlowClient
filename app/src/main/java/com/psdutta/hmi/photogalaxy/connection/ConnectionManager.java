package com.psdutta.hmi.photogalaxy.connection;

import com.psdutta.hmi.photogalaxy.parser.JsonParser;
import com.psdutta.hmi.photogalaxy.queue.PacketQueueManager;

public class ConnectionManager{
    private static final int SERVER_LISTENING_PORT = 2323;
    //private static final String SERVER_IP_ADDRESS = "192.168.5.1"; /*for HU*/
    private static final String SERVER_IP_ADDRESS = "192.168.43.1"; /*for mobiles*/
    private static final int CLIENT_PORT = 1313;
    private static final String TAG = ConnectionManager.class.getSimpleName();
    private PacketQueueManager mPacketQueueManager;
    private Thread mClientConnectionThread;
    private Thread mServerConnectionThread;
    private ClientConnection mClientConnection;
    private ServerConnection mServerConnection;
    private PacketWriter mPacketWriter;

    ConnectionManager(PacketQueueManager manager) {
        mPacketQueueManager = manager;
        mClientConnection = new ClientConnection(SERVER_IP_ADDRESS, SERVER_LISTENING_PORT);
        mClientConnectionThread = new Thread(mClientConnection);
        mClientConnectionThread.start();

        mServerConnection = new ServerConnection(CLIENT_PORT);
        mServerConnectionThread = new Thread(mServerConnection);
        mServerConnectionThread.start();
    }

    public void close() {
        if(mServerConnection != null) {
            mServerConnection.serverSocketConnection.close();
        }
        if(mClientConnection != null) {
            mClientConnection.clientSocketConnection.close();
        }
    }

    public void sendPacket(byte[] packet){
        if(mPacketWriter != null){
            mPacketWriter.addDataIntoQueue(packet);
        }
    }

    private class ClientConnection implements Runnable {
        private final String ip;
        private final int port;
        final ClientSocketConnection clientSocketConnection;
        private final Thread writerThread;
        private boolean isMessageInitiated = false;

        ClientConnection(final String ipAddress,final int portNumber) {
            ip = ipAddress;
            port = portNumber;
            clientSocketConnection = new ClientSocketConnection();
            mPacketWriter = new PacketWriter(clientSocketConnection);
            writerThread = new Thread(mPacketWriter);
        }

        @Override
        public void run() {
            writerThread.start();
            try {
                clientSocketConnection.connect(ip, port);
                while (!Thread.currentThread().isInterrupted()) {
                    if (!clientSocketConnection.isConnected()) {
                        break;
                    }
                    if(!isMessageInitiated && clientSocketConnection.isConnected()) {
                        mPacketQueueManager.sendPacket(JsonParser.getInitialPacket());
                        isMessageInitiated = true;
                    }
                    Thread.sleep(100);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                clientSocketConnection.close();
                writerThread.interrupt();
                writerThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private class ServerConnection implements Runnable {
        private final int port;
        final ServerSocketConnection serverSocketConnection;
        private final Thread readerThread;

        ServerConnection(final int portNumber) {
            port = portNumber;
            serverSocketConnection = new ServerSocketConnection();
            readerThread = new Thread(new PacketReader(serverSocketConnection, mPacketQueueManager));
        }

        @Override
        public void run() {
            readerThread.start();
            try {
                serverSocketConnection.connect(port);
                while (!Thread.currentThread().isInterrupted()) {
                    if (!serverSocketConnection.isConnected()) {
                        break;
                    }
                    Thread.sleep(100);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                serverSocketConnection.close();
                readerThread.interrupt();
                readerThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
