package io.deneb.server;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigProperties {
  private static Properties properties = new Properties();

  static {
    try (InputStream input =
           ConfigProperties.class.getClassLoader().getResourceAsStream("config.properties")) {
      properties.load(input);
    } catch (FileNotFoundException e) {
      throw new RuntimeException(e);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public String get(String key) {
    return properties.getProperty(key);
  }

}

