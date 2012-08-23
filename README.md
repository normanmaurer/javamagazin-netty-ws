javamagazin-netty-ws
=================

Das hier gezeigte Programm ist Bestandteil des Java Magazin Artikels "Netty - WebSockets to the rescue".

Starten des Servers
=================
Der Server kann via mvn gestartet werden. Hierbei kann -DwsPort= und -DudpPort= verwendet werden um den 
jeweiligen Port zu spezifizieren.

# mvn exec:exec -DwsPort=8888 -DudpPort=9999

