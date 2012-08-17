package me.normanmaurer.javamagazin.netty.examples.ws;

import java.net.InetSocketAddress;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

/**
 * Simpler WebSockets-Server
 */
public class WebSocketServer {

    private final int port;

    public WebSocketServer(int port) {
        this.port = port;
    }

    public void startUp() {
        // Bereite den Channel vor
        ServerBootstrap bootstrap = new ServerBootstrap(new NioServerSocketChannelFactory());

        // Setzen der WebSocketPipelineFactory die das bearbeiten von HTTP 
        //und WebSockets uebernimmt
        bootstrap.setPipelineFactory(new WebSocketPipelineFactory());

        // Binden des Sockets der nun bereit ist requests engegen zu nehmen
        bootstrap.bind(new InetSocketAddress(port));

    }

    public static void main(String[] args) {
        new WebSocketServer(8888).startUp();
    }
}
