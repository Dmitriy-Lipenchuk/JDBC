package ru.gamesphere.util;

import org.jetbrains.annotations.NotNull;

public final class JDBCCredentials {

  public static final @NotNull JDBCCredentials DEFAULT = new JDBCCredentials(
    "localhost",
    "5432",
    "jdbc",
    "postgres",
    "123456789"
  );

  private static final @NotNull String PREFIX = "jdbc:postgresql";

  private final @NotNull String host;
  private final @NotNull String port;
  private final @NotNull String dbName;
  private final @NotNull String login;
  private final @NotNull String password;

  private JDBCCredentials(@NotNull String host,
                          @NotNull String port,
                          @NotNull String dbName,
                          @NotNull String login,
                          @NotNull String password) {
    this.host = host;
    this.port = port;
    this.dbName = dbName;
    this.login = login;
    this.password = password;
  }

  public @NotNull String url() {
    return PREFIX
      + "://" + host + ':' + port
      + '/' + dbName;
  }

  public @NotNull String login() {
    return login;
  }

  public @NotNull String password() {
    return password;
  }
}
