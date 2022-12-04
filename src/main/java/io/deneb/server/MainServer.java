package io.deneb.server;

import io.deneb.server.servlet.SampleServlet;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;

public class MainServer extends WebSocketServer {

  private final static int SERVER_PORT = 8080;
  private final static int WEB_SOCKET_PORT = 9090;

  public static void main(String[] args) throws Exception {
    // websocket
    var webSocket = new InetSocketAddress(WEB_SOCKET_PORT);
    var webSocketServer = new MainServer(webSocket);
    webSocketServer.start();

    var jettyServer = new Server(SERVER_PORT);

    // logging
    System.setProperty("org.eclipse.jetty.LEVEL", "INFO");

    // servlet
    var servletHandler = new ServletHandler();
    servletHandler.addServletWithMapping(SampleServlet.class, "/");

    jettyServer.setHandler(servletHandler);

    jettyServer.start();
  }

  public MainServer(InetSocketAddress address) {
    super(address);
  }

  @Override
  public void onOpen(WebSocket webSocket, ClientHandshake clientHandshake) {
    webSocket.send("websocket open");
    System.out.println("request: " + webSocket.getResourceDescriptor());
  }

  @Override
  public void onClose(WebSocket webSocket, int i, String s, boolean b) {
    System.out.println("disconnected from client");
  }

  @Override
  public void onMessage(WebSocket webSocket, String s) {
    System.out.println(webSocket + " : " + s);
  }

  @Override
  public void onError(WebSocket webSocket, Exception e) {

  }

  @Override
  public void onStart() {
    System.out.println("==== websocket start ====");

  }
}
