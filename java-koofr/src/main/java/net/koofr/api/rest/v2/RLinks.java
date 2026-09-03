package net.koofr.api.rest.v2;

import net.koofr.api.json.JsonBase;
import net.koofr.api.json.JsonException;
import net.koofr.api.rest.v2.data.Links;
import net.koofr.api.rest.v2.data.Links.Link;

import java.io.IOException;

public class RLinks extends RGenericLinks<Links, Links.Link> {
  
  public RLinks(RMounts.RMount parent) {
    super(parent, Links.class, Links.Link.class, "/links");
  }

  public static class RLink extends RGenericLink<Links.Link> {
    public RLink(RLinks parent, String id) {
      super(parent, id);
    }

    public static class LinkDownloadable implements JsonBase {
      public Boolean downloadable;
    }  
  
    private static class RLinkDownloadable extends Resource {
      public RLinkDownloadable(RLink parent) {
        super(parent, "/downloadable");
      }

      public Link set(boolean downloadable) throws IOException, JsonException {
        LinkDownloadable ld = new LinkDownloadable();
        ld.downloadable = downloadable;

        return putJsonResult(ld, Link.class);
      }
    }

    public Link setDownloadable(boolean downloadable) throws IOException, JsonException {
      return new RLinkDownloadable(this).set(downloadable);
    }
  }

  public RLink link(String id) {
    return new RLink(this, id);
  }
  
}
