package me.normanmaurer.javamagazin.netty.examples.ws;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.channel.group.ChannelGroup;
import org.jboss.netty.handler.codec.http.websocketx.TextWebSocketFrame;

/**
 * {@link SimpleChannelUpstreamHandler} der alle empfangenen UDP Nachrichten
 * als {@link TextWebSocketFrame} an alle verbundenen {@link Channel} via {@link ChannelGroup}
 * sendet.
 * 
 * @author Norman Maurer <norman@apache.org>
 *
 */
public class WebSocketBroadcastHandler extends SimpleChannelUpstreamHandler {

    private final ChannelGroup wsGroup;

    public WebSocketBroadcastHandler(ChannelGroup wsGroup) {
        this.wsGroup = wsGroup;
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        // Senden der empfangenen UDP Nachricht an alle verbundenen WebSocket Clients
        wsGroup.write(new TextWebSocketFrame((ChannelBuffer)e.getMessage()));
    }

}
