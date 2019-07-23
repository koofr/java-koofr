package net.koofr.api.rest.v2;

import java.io.IOException;

import net.koofr.api.json.JsonException;
import net.koofr.api.rest.v2.data.Links;

public class RLinkList extends Resource {
  
  public RLinkList(Api parent) {
    super(parent, "/links");
  }

  public Links get() throws IOException, JsonException {
    return getResult(Links.class);
  }

}
