package io.deneb.server.endpoint;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommonEndPoint {

  private final static Logger log = LoggerFactory.getLogger(CommonEndPoint.class);

  private final static ObjectMapper om = new ObjectMapper();

  public void onMessage(String message) throws JsonProcessingException {
    var request = om.readValue(message, Request.class);
    var cmd = request.getCmd();

    switch (cmd) {
      case "GET": {
        log.info("GET");
        break;
      }
      case "POST": {
        log.info("POST");
        break;
      }
      default:
        log.warn("invalid cmd");
    }
  }

}
