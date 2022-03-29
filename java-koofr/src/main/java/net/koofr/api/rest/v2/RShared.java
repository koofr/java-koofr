package net.koofr.api.rest.v2;

import net.koofr.api.json.JsonException;
import net.koofr.api.rest.v2.data.Shared;

import java.io.IOException;

public class RShared extends Resource {

  public RShared(Api parent) {
    super(parent, "/shared");
  }
  
  public Shared get() throws JsonException, IOException {
    return getResult(Shared.class);
  }
  
  public RSharedFile shared(String id) {
    return new RSharedFile(this, id);
  }
  
  public static class RSharedFile extends Resource {
    public RSharedFile(RShared parent, String id) {
      super(parent, "/" + id);
    }
    
    public Shared.SharedFile get() throws JsonException, IOException {
      return getResult(Shared.SharedFile.class);
    }
    
  }
  
}
