package net.koofr.api.rest.v2.data;

import net.koofr.api.json.JsonBase;
import net.koofr.api.rest.v2.data.Bundle.BundleBookmark;
import net.koofr.api.util.U;

import java.io.Serializable;
import java.util.List;

public class Bookmarks implements JsonBase, Serializable {
  private static final long serialVersionUID = 1L;

  public List<Bookmark> bookmarks;
  
  public static class Bookmark implements JsonBase, Serializable {
    private static final long serialVersionUID = 1L;

    public String name;
    public String mountId;
    public String path;
    
    @Override
    public boolean equals(Object obj) {
      if(obj == null || !(obj instanceof Bookmark)) {
        return false;
      }
      Bookmark o = (Bookmark)obj;
      return U.safeEq(name, o.name) &&
        U.safeEq(mountId, o.mountId) &&
        U.safeEq(path, o.path);
    }    
  }
  
  @Override
  public boolean equals(Object obj) {
    if(obj == null || !(obj instanceof Bookmarks)) {
      return false;
    }
    Bookmarks o = (Bookmarks)obj;
    return U.safeEq(bookmarks, o.bookmarks);
  }
}
