package net.koofr.api.rest.v2.data;

import net.koofr.api.json.JsonBase;
import java.io.Serializable;

public class BaseLink implements JsonBase, Serializable {
    private static final long serialVersionUID = 1L;

    public String id;
    public String mountId;
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
  