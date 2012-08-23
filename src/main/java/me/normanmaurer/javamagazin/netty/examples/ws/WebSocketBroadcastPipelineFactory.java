
package me.normanmaurer.javamagazin.netty.examples.ws;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.group.ChannelGroup;

/**
 * {@link ChannelPipelineFactory} die den {@link WebSocketBroadcastHandler} in
 * die {@link ChannelPipeline} einfuegt. Dieser uebernimmt dann das senden aller
 * empfangenen UDP Nachrichten and die verbundenen WebSocket Clients.
 * 
 * @author Norman Maurer <norman@apache.org>
 *
 */
public class WebSocketBroadcastPipelineFactory implements ChannelPipelineFactory{

    private final ChannelGroup group;

    public WebSocketBroadcastPipelineFactory(ChannelGroup group) {
        this.group = group;
    }

    @Override
    public ChannelPipeline getPipeline() throws Exception {
        return Channels.pipeline(new WebSocketBroadcastHandler(group));
    }
}

