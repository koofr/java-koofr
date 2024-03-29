package net.koofr.api.rest.v2;

import net.koofr.api.json.JsonBase;
import net.koofr.api.json.JsonException;
import net.koofr.api.rest.v2.data.Links;
import net.koofr.api.rest.v2.data.Links.Link;

import java.io.IOException;
import java.util.Date;

public class RLinks extends Resource {
  
  public RLinks(RMounts.RMount parent) {
    super(parent, "/links");
  }

  public Links get() throws IOException, JsonException {
    return getResult(Links.class);
  }
  
  public static class LinkCreate implements JsonBase {
    public String path;
  }
  
  public Link create(String path) throws IOException, JsonException {
    LinkCreate c = new LinkCreate();
    c.path = path;
    return postJsonResult(c, Link.class);
  }
  
  public static class RLink extends Resource {
    public RLink(RLinks parent, String id) {
      super(parent, "/" + id);
    }
    
    public Link get() throws IOException, JsonException {
      return getResult(Link.class);
    }
    
    public void delete() throws IOException, JsonException {
      deleteNoResult();
    }
    
    public static class LinkSetHash implements JsonBase {
      public String hash;
    }
    
    private static class RLinkUrlHash extends Resource {
      public RLinkUrlHash(RLink parent) {
        super(parent, "/urlHash");
      }
      
      public Link set(String hash) throws IOException, JsonException {
        LinkSetHash h = new LinkSetHash();
        h.hash = hash;        
        return putJsonResult(h, Link.class);
      }
    }
    
    public Link setHash(String hash) throws IOException, JsonException {
      return new RLinkUrlHash(this).set(hash);
    }
    
    private static class RLinkPasswordReset extends Resource {
      public RLinkPasswordReset(RLinkPassword parent) {
        super(parent, "/reset");
      }
      
      public Link reset() throws IOException, JsonException {
        return putJsonResult(Link.class);
      }
    }

    public static class RLinkPassword extends Resource {
      public RLinkPassword(RLink parent) {
        super(parent, "/password");
      }
      
      public Link delete() throws IOException, JsonException {
        return deleteResult(Link.class);
      }
      
      public Link reset() throws IOException, JsonException {
        return new RLinkPasswordReset(this).reset();
      }
    }

    public RLinkPassword password() {
      return new RLinkPassword(this);
    }

    public static class LinkValidity implements JsonBase {
      public Long validFrom;
      public Long validTo;
    }
    
    private static class RLinkValidity extends Resource {
      public RLinkValidity(RLink parent) {
        super(parent, "/validity");
      }
      
      public Link set(Date from, Date to) throws IOException, JsonException {
        LinkValidity v = new LinkValidity();
        if(null != from) {
          v.validFrom = from.getTime();
        }
        if(null != to) {
          v.validTo = to.getTime();
        }
        return putJsonResult(v, Link.class);
      }
    }
    
    public Link setValidity(Date from, Date to) throws IOException, JsonException {
      return new RLinkValidity(this).set(from, to);
    }
    
  }
  
  public RLink link(String id) {
    return new RLink(this, id);
  }
  
}
