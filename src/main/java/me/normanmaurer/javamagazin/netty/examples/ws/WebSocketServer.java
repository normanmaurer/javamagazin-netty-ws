package me.normanmaurer.javamagazin.netty.examples.ws;

import java.net.InetSocketAddress;

import org.jboss.netty.bootstrap.ConnectionlessBootstrap;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.group.ChannelGroup;
import org.jboss.netty.channel.group.DefaultChannelGroup;
import org.jboss.netty.channel.socket.nio.NioDatagramChannelFactory;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

public class WebSocketServer {

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
        ConnectionlessBootstrap udpBootstrap = new ConnectionlessBootstrap(new NioDatagramChannelFactory());
        udpBootstrap.setPipelineFactory(new WebSocketBroadcastPipelineFactory(group));
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
        new WebSocketServer(8888, 9999).startUp();
    }
}
