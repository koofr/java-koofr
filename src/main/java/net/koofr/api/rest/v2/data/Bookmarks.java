package net.koofr.api.rest.v2.data;

import java.util.List;

import net.koofr.api.json.JsonBase;

public class Bookmarks implements JsonBase {

  public List<Bookmark> bookmarks;
  
  public static class Bookmark implements JsonBase {
    public String name;
    public String mountId;
    public String path;
  }
  
}
