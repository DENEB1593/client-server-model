package io.deneb.server;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.deneb.server.database.DataSourceConnection;
import io.deneb.server.endpoint.CommonEndPoint;
import io.deneb.server.servlet.SampleServlet;
import jakarta.servlet.Servlet;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.h2.server.web.WebServlet;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class MainServer extends WebSocketServer {

  private final static int SERVER_PORT = 8080;
  private final static int WEB_SOCKET_PORT = 9090;

  private final Map<WebSocket, CommonEndPoint> socketToEndPoint = new HashMap<>();

  private final static Logger log = LoggerFactory.getLogger(MainServer.class);

  public static void main(String[] args) throws Exception {
    ConfigProperties configProperties = new ConfigProperties();
    // db
    new DataSourceConnection(configProperties);
    // websocket
    var webSocket = new InetSocketAddress(WEB_SOCKET_PORT);
    var webSocketServer = new MainServer(webSocket);
    webSocketServer.start();

    var jettyServer = new Server(SERVER_PORT);

    // logging
    System.setProperty("org.eclipse.jetty.LEVEL", "INFO");

    // servlet
    var context = new ServletContextHandler(ServletContextHandler.SESSIONS);
    context.setResourceBase(System.getProperty("user.dir") + "/src/main/webapp");
    context.setContextPath("/");


    var staticContentServlet = new ServletHolder(
      "staticContentServlet", DefaultServlet.class);
    staticContentServlet.setInitParameter("dirAllowed", "true");
    context.addServlet(staticContentServlet, "/");

    jettyServer.setHandler(context);

    jettyServer.start();
  }

  public MainServer(InetSocketAddress address) {
    super(address);
  }

  @Override
  public void onStart() { }

  @Override
  public void onOpen(WebSocket webSocket, ClientHandshake clientHandshake) {
    webSocket.send("websocket open");
    socketToEndPoint.put(webSocket, new CommonEndPoint());
  }

  @Override
  public void onClose(WebSocket webSocket, int i, String s, boolean b) {
    webSocket.send("disconnected from server");
    socketToEndPoint.remove(webSocket);
  }

  @Override
  public void onMessage(WebSocket webSocket, String message) {
    try {
      var endPoint = socketToEndPoint.get(webSocket);
      endPoint.onMessage(message);
    } catch (Exception e) {
      if (e instanceof JsonProcessingException) {
        log.error("bad request format : {}", message);
      } else {
        log.error("error occurred : {}", e.getMessage(), e);
      }
    }
  }

  @Override
  public void onError(WebSocket webSocket, Exception e) {
    log.error("websocket error occured : {}", e.getMessage(), e);
  }

}
