package net.koofr.api.rest.v2.data;

import java.io.Serializable;
import java.util.List;

import net.koofr.api.json.JsonBase;

public class Receivers implements JsonBase, Serializable {
  private static final long serialVersionUID = 1L;

  public static class Receiver implements JsonBase, Serializable {
    private static final long serialVersionUID = 1L;
    
    public String id;
    public String name;
    public String path;
    public Long counter;
    public String url;
    public String shortUrl;
    public String hash;
    public String host;
    public Boolean hasPassword;
    public String password;
    public Long validFrom;
    public Long validTo;
    public Boolean alert;
  }
  
  public List<Receiver> receivers;
    
}
