package me.normanmaurer.javamagazin.netty.examples.ws;

import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.ChannelHandler;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.handler.codec.http.HttpChunkAggregator;
import org.jboss.netty.handler.codec.http.HttpRequestDecoder;
import org.jboss.netty.handler.codec.http.HttpResponseEncoder;

/**
 * {@link ChannelPipelineFactory} die alle noetigen {@link ChannelHandler} in die erzeugte
 * {@link ChannelPipeline} einfuegt und diese dan zur Verfuegung stellt.
 */
public class WebSocketPipelineFactory implements ChannelPipelineFactory {
    public ChannelPipeline getPipeline() throws Exception {
        // Pipeline Object erstellen
        ChannelPipeline pipeline = Channels.pipeline();
        
        // Decoder der ChannelBuffer zu HttpRequest's umwandelt
        pipeline.addLast("reqDecoder", new HttpRequestDecoder());
        
        // Aggregator der HttpChunks' in HttpRequest's aggregiert
        pipeline.addLast("chunkAggregator", new HttpChunkAggregator(65536));
        
        // Encoder der HttpResponse's zu ChannellBuffer umwandelt
        pipeline.addLast("reqEncoder", new HttpResponseEncoder());
        
        // Handler der richtigen WebSocket Handshaker einfuegt
        // und die Index-Seite zur Verfügung stellt
        pipeline.addLast("handler", new WebSocketServerHandler());
        return pipeline;
    }
}
