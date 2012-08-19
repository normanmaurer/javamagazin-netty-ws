
package me.normanmaurer.javamagazin.netty.examples.ws;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.util.CharsetUtil;

public final class WebSocketServerIndexPage {

    private static final String NEWLINE = "\r\n";

    public static ChannelBuffer getContent(String webSocketLocation) {
        return ChannelBuffers.copiedBuffer(
                "<html><head><title>Web Socket Nachrichten Beispiel</title></head>" + NEWLINE +
                "<body>" + NEWLINE +
                "<script type=\"text/javascript\">" + NEWLINE +
                "var socket;" + NEWLINE +
                "if (!window.WebSocket) {" + NEWLINE +
                "  window.WebSocket = window.MozWebSocket;" + NEWLINE +
                "}" + NEWLINE +
                "if (window.WebSocket) {" + NEWLINE +
                "  socket = new WebSocket(\"" + webSocketLocation + "\");" + NEWLINE +
                "  socket.onmessage = function(event) {" + NEWLINE +
                "    var ta = document.getElementById('msgs');" + NEWLINE +
                "    ta.value = ta.value + event.data" +
                "  };" + NEWLINE +
                "} else {" + NEWLINE +
                "  alert(\"Web-Browser unterstuetzt keine WebSockets!\");" + NEWLINE +
                "}" + NEWLINE +
                NEWLINE +
                "</script>" + NEWLINE +
                "<h3>Nachrichten:</h3>" + NEWLINE +
                "<textarea id=\"msgs\" style=\"width:500px;height:300px;\"></textarea>" + NEWLINE +
                "</form>" + NEWLINE +
                "</body>" + NEWLINE +
                "</html>" + NEWLINE, CharsetUtil.US_ASCII);
    }

    private WebSocketServerIndexPage() {
        // Unused
    }
}
