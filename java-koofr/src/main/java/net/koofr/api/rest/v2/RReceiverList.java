package net.koofr.api.rest.v2;

import net.koofr.api.json.JsonException;
import net.koofr.api.rest.v2.data.Receivers;

import java.io.IOException;

public class RReceiverList extends Resource {
  
  public RReceiverList(Api parent) {
    super(parent, "/receivers");
  }

  public Receivers get() throws IOException, JsonException {
    return getResult(Receivers.class);
  }

}
