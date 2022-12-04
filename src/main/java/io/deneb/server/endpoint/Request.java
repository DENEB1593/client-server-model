package io.deneb.server.endpoint;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class Request {
  private final String cmd;
  private final String body;

  @JsonCreator
  public Request(@JsonProperty("cmd") String cmd,
                 @JsonProperty("body") String body) {
    this.cmd = cmd;
    this.body = body;
  }

  public String getCmd() {
    return cmd;
  }

  public String getBody() {
    return body;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
      .append("cmd", cmd)
      .append("body", body)
      .toString();
  }
}
