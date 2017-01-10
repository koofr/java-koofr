package net.koofr.api.rest.v2;

import java.io.IOException;
import java.util.Date;

import net.koofr.api.json.JsonBase;
import net.koofr.api.json.JsonException;
import net.koofr.api.rest.v2.data.Receivers;
import net.koofr.api.rest.v2.data.Receivers.*;

public class RReceivers extends Resource {
  
  public RReceivers(RMounts.RMount parent) {
    super(parent, "/receivers");
  }

  public Receivers get() throws IOException, JsonException {
    return getResult(Receivers.class);
  }
  
  public static class ReceiverCreate implements JsonBase {
    public String path;
  }
  
  public Receiver create(String path) throws IOException, JsonException {
    ReceiverCreate c = new ReceiverCreate();
    c.path = path;
    return postJsonResult(c, Receiver.class);
  }
  
  public static class RReceiver extends Resource {
    public RReceiver(RReceivers parent, String id) {
      super(parent, "/" + id);
    }
    
    public Receiver get() throws IOException, JsonException {
      return getResult(Receiver.class);
    }
    
    public void delete() throws IOException, JsonException {
      deleteNoResult();
    }
    
    public static class ReceiverSetHash implements JsonBase {
      public String hash;
    }
    
    private static class RReceiverUrlHash extends Resource {
      public RReceiverUrlHash(RReceiver parent) {
        super(parent, "/urlHash");
      }
      
      public Receiver set(String hash) throws IOException, JsonException {
        ReceiverSetHash h = new ReceiverSetHash();
        h.hash = hash;        
        return putJsonResult(h, Receiver.class);
      }
    }
    
    public void setHash(String hash) throws IOException, JsonException {
      new RReceiverUrlHash(this).set(hash);
    }
    
    private static class RReceiverPasswordReset extends Resource {
      public RReceiverPasswordReset(RReceiverPassword parent) {
        super(parent, "/reset");
      }
      
      public Receiver reset() throws IOException, JsonException {
        return putJsonResult(Receiver.class);
      }
    }

    public static class RReceiverPassword extends Resource {
      public RReceiverPassword(RReceiver parent) {
        super(parent, "/password");
      }
      
      public Receiver delete() throws IOException, JsonException {
        return deleteResult(Receiver.class);
      }
      
      public Receiver reset() throws IOException, JsonException {
        return new RReceiverPasswordReset(this).reset();
      }
    }

    public RReceiverPassword password() {
      return new RReceiverPassword(this);
    }

    public static class ReceiverValidity implements JsonBase {
      public Long validFrom;
      public Long validTo;
    }
    
    private static class RReceiverValidity extends Resource {
      public RReceiverValidity(RReceiver parent) {
        super(parent, "/validity");
      }
      
      public Receiver set(Date from, Date to) throws IOException, JsonException {
        ReceiverValidity v = new ReceiverValidity();
        v.validFrom = from.getTime();
        v.validTo = to.getTime();
        return putJsonResult(v, Receiver.class);
      }
    }
    
    public Receiver setValidity(Date from, Date to) throws IOException, JsonException {
      return new RReceiverValidity(this).set(from, to);
    }
    
  }
  
  public RReceiver receiver(String id) {
    return new RReceiver(this, id);    
  }
  
}
