package io.deneb.server.database;

import io.deneb.server.ConfigProperties;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;

import static java.util.Objects.requireNonNull;

public class DataSourceConnection {

  private String driver;
  private String url;
  private String username;
  private String password;

  public DataSourceConnection(ConfigProperties properties) {
    requireNonNull(properties.get("db.driver"), "no db driver config");
    requireNonNull(properties.get("db.url"), "no db url config");
    requireNonNull(properties.get("db.username"), "no db username config");
    requireNonNull(properties.get("db.password"), "no db password config");

    driver = properties.get("db.driver");
    url = properties.get("db.url");
    username = properties.get("db.username");
    password = properties.get("db.password");

    this.init(driver);
  }

  void init(String driver) {
    try {
      Class.forName(driver);
    } catch (ClassNotFoundException e) {
      throw new Error(e);
    }

  }

  public Connection getConnection() throws SQLException {
    return DriverManager.getConnection(this.url,  this.username, this.password);
  }

}
