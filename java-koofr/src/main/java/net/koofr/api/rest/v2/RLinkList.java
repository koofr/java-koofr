package net.koofr.api.rest.v2;

import net.koofr.api.json.JsonException;
import net.koofr.api.rest.v2.data.Links;

import java.io.IOException;

public class RLinkList extends Resource {
  
  public RLinkList(Api parent) {
    super(parent, "/links");
  }

  public Links get() throws IOException, JsonException {
    return getResult(Links.class);
  }

}
