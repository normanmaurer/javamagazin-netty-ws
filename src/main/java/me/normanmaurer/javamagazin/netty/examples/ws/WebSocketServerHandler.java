package me.normanmaurer.javamagazin.netty.examples.ws;


import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.*;
import org.jboss.netty.channel.group.ChannelGroup;
import org.jboss.netty.handler.codec.http.DefaultHttpResponse;
import org.jboss.netty.handler.codec.http.HttpHeaders;
import org.jboss.netty.handler.codec.http.HttpMethod;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponse;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;
import org.jboss.netty.handler.codec.http.HttpVersion;
import org.jboss.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import org.jboss.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;
import org.jboss.netty.util.CharsetUtil;

/**
 * {@link SimpleChannelUpstreamHandler} implementation der den WebSocket Handshake durchfuert
 * sowie das Abhandeln von {@link HttpRequest}'s.
 * 
 * @author Norman Maurer <norman@apache.org>
 */
public class WebSocketServerHandler extends SimpleChannelUpstreamHandler {

    private static final String WEBSOCKET_PATH = "/ws";
    private final ChannelGroup wsGroup;
    
    public WebSocketServerHandler(ChannelGroup wsGroup) {
        this.wsGroup = wsGroup;
    }
    
    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        Object msg = e.getMessage();
        if (msg instanceof HttpRequest) {
            handleHttpRequest(ctx, (HttpRequest) msg);
        } else {
            // Ungueltige Message, somit schliesen des Channel's
            ctx.getChannel().close();
        }
    }

    private void handleHttpRequest(ChannelHandlerContext ctx, HttpRequest req) throws Exception {        
        // Ueberpruefen ob der Request ein GET ist oder nicht, wenn nicht 
        // kann dieser nicht bearbeitet werden. Somit senden eines 403 codes
        if (req.getMethod() != HttpMethod.GET) {
            sendHttpResponse(ctx, req, new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.FORBIDDEN));
            return;
        }

        // Send the demo page and favicon.ico
        if (req.getUri().equals("/")) {
            HttpResponse res = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);

            ChannelBuffer content = WebSocketServerIndexPage.getContent(getWebSocketLocation(req));

            res.setHeader(HttpHeaders.Names.CONTENT_TYPE, "text/html; charset=UTF-8");
            HttpHeaders.setContentLength(res, content.readableBytes());

            res.setContent(content);
            sendHttpResponse(ctx, req, res);
        } else if (req.getUri().startsWith(WEBSOCKET_PATH)) {
            // Handshake
            WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(
                    getWebSocketLocation(req), null, false);
            WebSocketServerHandshaker handshaker = wsFactory.newHandshaker(req);
            // Ueberpruefen ob ein geeigneter WebSocketServerHandshaker fuer den request
            // gefunden worden konnte. wenn nicht wird der client darueber informiert
            if (handshaker == null) {
                wsFactory.sendUnsupportedWebSocketVersionResponse(ctx.getChannel());
            } else {
                // fuehre den Handshake
                handshaker.handshake(ctx.getChannel(), req).addListener(new ChannelFutureListener() {
                    @Override
                    public void operationComplete(ChannelFuture future) throws Exception {
                        if(future.isSuccess()) {
                            // Handshake war erfolgreich. Fuege Channel in die ChannelGroup hinzu
                            // um so auch UDP Nachrichten zu empfangen
                            wsGroup.add(future.getChannel());
                        } else {
                            // Handshake hat nicht geklappt. Feuere ein exceptionCaught event
                            Channels.fireExceptionCaught(future.getChannel(), future.getCause());
                        }
                    }
                });
            }
        } else {
            // Sende ein 404
            HttpResponse res = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.NOT_FOUND);
            sendHttpResponse(ctx, req, res);
            
        }

       
    }


    private static void sendHttpResponse(ChannelHandlerContext ctx, HttpRequest req, HttpResponse res) {
        // Generate an error page if response status code is not OK (200).
        if (res.getStatus().getCode() != 200) {
            res.setContent(ChannelBuffers.copiedBuffer(res.getStatus().toString(), CharsetUtil.UTF_8));
            HttpHeaders.setContentLength(res, res.getContent().readableBytes());
        }

        // Senden der HttpResponse
        ChannelFuture f = ctx.getChannel().write(res);
        if (!HttpHeaders.isKeepAlive(req) || res.getStatus().getCode() != 200) {
            // falls der HttpRequest nicht den Keep-Alive header enthielt
            // oder der Code nicht 200 war wird der Channel nachdem die 
            // Nachricht geschrieben wird geschlossen
            f.addListener(ChannelFutureListener.CLOSE);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
        // Stacktrace nach STDOUT senden und Channel schliesen
        e.getCause().printStackTrace();
        e.getChannel().close();
    }

    private static String getWebSocketLocation(HttpRequest req) {
        return "ws://" + req.getHeader(HttpHeaders.Names.HOST) + WEBSOCKET_PATH;
    }
}
