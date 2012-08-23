package me.normanmaurer.javamagazin.netty.examples.ws;

import java.net.InetSocketAddress;

import org.jboss.netty.bootstrap.ConnectionlessBootstrap;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.group.ChannelGroup;
import org.jboss.netty.channel.group.DefaultChannelGroup;
import org.jboss.netty.channel.socket.nio.NioDatagramChannelFactory;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

public class WebSocketServer {
    // Dieser Gruppe werden alle WebSocket client Verbindungen zu gefuegt
    // um somit eine einfache Kommunikation an alle zu ermoeglichen
    private final ChannelGroup group = new DefaultChannelGroup();
    private final int udpPort;
    private final int port;

    public WebSocketServer(int port, int udpPort) {
        this.port = port;
        this.udpPort = udpPort;
    }

    /**
     * Starten des Servers.
     * 
     */
    public void startUp() {
        // Bereite den UDP/Datagram Channel vor
        ConnectionlessBootstrap udpBootstrap = new ConnectionlessBootstrap(new NioDatagramChannelFactory());

        // Setzen der WebSocketBroadcastPipelineFactory die das Senden von UDP
        // Nachrichten an die WebSocket Clients uebernimmt
        udpBootstrap.setPipelineFactory(new WebSocketBroadcastPipelineFactory(group));

        // Binden des Sockets der die UDP Nachrichten entgegennimmt
        udpBootstrap.bind(new InetSocketAddress(udpPort));
        

        // Bereite den Channel vor
        ServerBootstrap bootstrap = new ServerBootstrap(new NioServerSocketChannelFactory());

        // Setzen der WebSocketPipelineFactory die das bearbeiten von HTTP 
        //und WebSockets uebernimmt
        bootstrap.setPipelineFactory(new WebSocketPipelineFactory(group));

        // Binden des Sockets der nun bereit ist requests engegen zu nehmen
        bootstrap.bind(new InetSocketAddress(port));
        
    }

    public static void main(String[] args) {
        int wsPort;
        int udpPort;
        if (args.length < 2) {
            wsPort = 8888;
            udpPort = 9999;
        } else {
            wsPort = Integer.parseInt(args[0]);
            udpPort = Integer.parseInt(args[1]);
        }
        new WebSocketServer(wsPort, udpPort).startUp();
    }
}
