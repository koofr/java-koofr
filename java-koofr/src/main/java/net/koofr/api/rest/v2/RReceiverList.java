package net.koofr.api.rest.v2;

import java.io.IOException;

import net.koofr.api.json.JsonException;
import net.koofr.api.rest.v2.data.Receivers;

public class RReceiverList extends Resource {
  
  public RReceiverList(Api parent) {
    super(parent, "/receivers");
  }

  public Receivers get() throws IOException, JsonException {
    return getResult(Receivers.class);
  }

}
