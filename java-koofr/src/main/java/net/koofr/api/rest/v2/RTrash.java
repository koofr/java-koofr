package net.koofr.api.rest.v2;

import net.koofr.api.json.JsonException;
import net.koofr.api.rest.v2.data.Trash;

import java.io.IOException;

public class RTrash extends Resource {
  public RTrash(Api parent) {
    super(parent, "/trash");
  }
  
  public Trash get() throws IOException, JsonException {
    return getResult(Trash.class);
  }
}
