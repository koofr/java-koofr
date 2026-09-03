package net.koofr.api.rest.v2;

import net.koofr.api.http.Response;
import net.koofr.api.json.JsonBase;
import net.koofr.api.json.JsonException;
import net.koofr.api.rest.v2.data.Files.DownloadResult;

import java.io.IOException;
import java.util.Date;

public class RGenericLinks<LS, L> extends Resource {
  
  Class<LS> cls;
  Class<L> cl;
  String path;

  public RGenericLinks(RMounts.RMount parent, Class<LS> cls, Class<L> cl, String path) {
    super(parent, path);
    this.cls = cls;
    this.cl = cl;
    this.path = path;
  }

  public LS get() throws IOException, JsonException {
    return getResult(cls);
  }
  
  public static class LinkCreate implements JsonBase {
    public String path;
  }
  
  public L create(String path) throws IOException, JsonException {
    LinkCreate c = new LinkCreate();
    c.path = path;
    return postJsonResult(c, cl);
  }
  
  public static abstract class RGenericLinkSub<L> extends Resource {
    Class<L> cl;

    public RGenericLinkSub(RGenericLinks<?, L> parent, String path) {
      super(parent, path);
      this.cl = parent.cl;
    }

    public RGenericLinkSub(RGenericLinkSub<L> parent, String path) {
      super(parent, path);
      this.cl = parent.cl;
    }
  }

  public static class RGenericLink<L> extends RGenericLinkSub<L> {
    public RGenericLink(RGenericLinks<?, L> parent, String id) {
      super(parent, "/" + id);
    }
    
    public L get() throws IOException, JsonException {
      return getResult(cl);
    }
    
    public void delete() throws IOException, JsonException {
      deleteNoResult();
    }
    
    public static class LinkSetHash implements JsonBase {
      public String hash;
    }
    
    private static class RGenericLinkUrlHash<L> extends RGenericLinkSub<L> {
      public RGenericLinkUrlHash(RGenericLinkSub<L> parent) {
        super(parent, "/urlHash");
      }
      
      public L set(String hash) throws IOException, JsonException {
        LinkSetHash h = new LinkSetHash();
        h.hash = hash;        
        return putJsonResult(h, cl);
      }
    }
    
    public L setHash(String hash) throws IOException, JsonException {
      return new RGenericLinkUrlHash<>(this).set(hash);
    }
    
    public static class LinkMessage implements JsonBase {
      public String message;
    }

    public static class RGenericLinkMessage<L> extends RGenericLinkSub<L> {
      public RGenericLinkMessage(RGenericLink<L> parent) {
        super(parent, "/message");
      }

      public L set(String message) throws IOException, JsonException {
        LinkMessage lm = new LinkMessage();
        lm.message = message;

        return putJsonResult(lm, cl);
      }
    }

    public L setMessage(String message) throws IOException, JsonException {
      return new RGenericLinkMessage<>(this).set(message);
    }

    private static class RGenericLinkPasswordReset<L> extends RGenericLinkSub<L> {
      public RGenericLinkPasswordReset(RGenericLinkPassword<L> parent) {
        super(parent, "/reset");
      }
      
      public L reset() throws IOException, JsonException {
        return putJsonResult(cl);
      }
    }

    public static class LinkPassword implements JsonBase {
      public String password;
    }

    private static class RGenericLinkPasswordSet<L> extends RGenericLinkSub<L> {
      public RGenericLinkPasswordSet(RGenericLinkPassword<L> parent) {
        super(parent, "/set");
      }

      public L set(String password) throws IOException, JsonException {
        LinkPassword lp = new LinkPassword();
        lp.password = password;

        return putJsonResult(lp, cl);
      }
    }

    public static class RGenericLinkPassword<L> extends RGenericLinkSub<L> {
      public RGenericLinkPassword(RGenericLink<L> parent) {
        super(parent, "/password");
      }
      
      public L delete() throws IOException, JsonException {
        return deleteResult(cl);
      }
      
      public L reset() throws IOException, JsonException {
        return new RGenericLinkPasswordReset<>(this).reset();
      }

      public L set(String password) throws IOException, JsonException {
        return new RGenericLinkPasswordSet<>(this).set(password);
      }
    }

    public RGenericLinkPassword<L> password() {
      return new RGenericLinkPassword<>(this);
    }

    public static class LinkValidity implements JsonBase {
      public Long validFrom;
      public Long validTo;
    }
    
    private static class RGenericLinkValidity<L> extends RGenericLinkSub<L> {
      public RGenericLinkValidity(RGenericLink<L> parent) {
        super(parent, "/validity");
      }
      
      public L set(Date from, Date to) throws IOException, JsonException {
        LinkValidity v = new LinkValidity();
        if(null != from) {
          v.validFrom = from.getTime();
        }
        if(null != to) {
          v.validTo = to.getTime();
        }
        return putJsonResult(v, cl);
      }
    }
    
    public L setValidity(Date from, Date to) throws IOException, JsonException {
      return new RGenericLinkValidity<>(this).set(from, to);
    }

    private static class RLinkQR<L> extends Resource {
      public RLinkQR(RGenericLink<L> parent) {
        super(parent, "/qr");
      }

      public DownloadResult get() throws IOException {
        Response r = httpGet();
        return resolveDownload(r);
      }
    }

    public DownloadResult getQR() throws IOException {
      return new RLinkQR<>(this).get();
    }
  }
  
  public RGenericLink<L> link(String id) {
    return new RGenericLink<>(this, id);
  }
  
}
