package net.koofr.api.rest.v2;

import java.io.IOException;

import net.koofr.api.json.JsonException;
import net.koofr.api.rest.v2.data.ConnectionList;

public class Connections extends Resource {

  public Connections(User r) {
    super(r, "/connections");
  }

  public ConnectionList get() throws JsonException, IOException {
    return getMapped(ConnectionList.class); 
  }
  
}
