package me.normanmaurer.javamagazin.netty.examples.ws;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.util.CharsetUtil;

public final class WebSocketServerIndexPage {

    private static final String NEWLINE = "\r\n";

    /**
     * Gebe Index.html als {@link ChannelBuffer} zurueck
     * 
     * @param webSocketLocation
     * @return html
     */
    public static ChannelBuffer getContent(String webSocketLocation) {
        StringBuilder page = new StringBuilder();
        page.append("<html><head><title>Web Socket Nachrichten Beispiel</title></head>").append(NEWLINE);
        page.append("<body>").append(NEWLINE);
        page.append("<script type=\"text/javascript\">").append(NEWLINE);
        page.append("var socket;").append(NEWLINE);
        page.append("if (!window.WebSocket) {").append(NEWLINE);
        page.append("  window.WebSocket = window.MozWebSocket;").append(NEWLINE);
        page.append("}").append(NEWLINE);
        page.append("if (window.WebSocket) {").append(NEWLINE);
        page.append("  socket = new WebSocket(\"" + webSocketLocation + "\");").append(NEWLINE);
        page.append("  socket.onmessage = function(event) {").append(NEWLINE);
        page.append("    var ta = document.getElementById('msgs');").append(NEWLINE);
        page.append("    ta.value = ta.value + event.data;").append(NEWLINE);
        page.append("  };").append(NEWLINE);
        page.append("} else {").append(NEWLINE);
        page.append("  alert(\"Web-Browser unterstuetzt keine WebSockets!\");").append(NEWLINE);
        page.append("}").append(NEWLINE);
        page.append(NEWLINE);
        page.append("</script>").append(NEWLINE);
        page.append("<h3>Nachrichten:</h3>").append(NEWLINE);
        page.append("<textarea id=\"msgs\" style=\"width:500px;height:300px;\"></textarea>").append(NEWLINE);
        page.append("</body>").append(NEWLINE);
        page.append("</html>").append(NEWLINE);
        
        return ChannelBuffers.copiedBuffer(page.toString(), CharsetUtil.US_ASCII);

    }

    private WebSocketServerIndexPage() {
        // Unused
    }
}
