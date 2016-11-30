package net.koofr.api.rest.v2.data;

import java.io.Serializable;
import java.util.List;

import net.koofr.api.json.JsonBase;
import net.koofr.api.rest.v2.data.Mounts.Mount;

public class Bookmarks implements JsonBase, Serializable {
  private static final long serialVersionUID = 1L;

  public List<Bookmark> bookmarks;
  
  public static class Bookmark implements JsonBase, Serializable {
    private static final long serialVersionUID = 1L;

    public String name;
    public String mountId;
    public String path;    
  }
  
}
