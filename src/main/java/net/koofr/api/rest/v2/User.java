package net.koofr.api.rest.v2;

import java.io.IOException;

import net.koofr.api.json.JsonException;
import net.koofr.api.rest.v2.data.Self;

public class User extends Resource {

  public User(Api r) {
    super(r, "/user");
  }

  public Self self() throws JsonException, IOException {
    return getMapped(Self.class); 
  }

  public Connections connections() throws JsonException, IOException {
    return new Connections(this); 
  }
  
}
