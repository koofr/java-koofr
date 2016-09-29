package net.koofr.api.rest.v2;

import java.io.IOException;

import net.koofr.api.util.StdLog;

public class User extends Resource {

  public User(Api r) {
    super(r, "/user");
  }

  public void info() throws IOException {
    logResponse(get(), new StdLog());
  }
}
