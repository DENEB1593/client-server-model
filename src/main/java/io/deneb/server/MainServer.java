package io.deneb.server;

import io.deneb.server.endpoint.SampleEndPoint;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;

public class MainServer {

  private final static int SERVER_PORT = 8080;

  public static void main(String[] args) throws Exception {
    Server server = new Server(SERVER_PORT);

    ServletHandler servletHandler = new ServletHandler();
    servletHandler.addServletWithMapping(SampleEndPoint.class, "/");

    server.setHandler(servletHandler);
    server.start();
  }
}
