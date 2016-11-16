package net.koofr.api.rest.v2.data;

import java.util.List;

import net.koofr.api.json.JsonBase;

public class Links {

  public static class Link implements JsonBase {
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
    public Boolean passwordRequired;
  }

  public List<Link> links;  
  
}
