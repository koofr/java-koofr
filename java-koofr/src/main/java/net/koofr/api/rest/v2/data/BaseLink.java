package net.koofr.api.rest.v2.data;

import net.koofr.api.json.JsonBase;
import net.koofr.api.util.U;

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

    @Override
    public boolean equals(Object obj) {
      if(obj == null || !(obj instanceof BaseLink)) {
        return false;
      }
      BaseLink o = (BaseLink)obj;
      return U.safeEq(id, o.id) &&
        U.safeEq(mountId, o.mountId) &&
        U.safeEq(name, o.name) &&
        U.safeEq(path, o.path) &&
        U.safeEq(counter, o.counter) &&
        U.safeEq(url, o.url) &&
        U.safeEq(shortUrl, o.shortUrl) &&
        U.safeEq(hash, o.hash) &&
        U.safeEq(host, o.host) &&
        U.safeEq(hasPassword, o.hasPassword) &&
        U.safeEq(password, o.password) &&
        U.safeEq(validFrom, o.validFrom) &&
        U.safeEq(validTo, o.validTo) &&
        U.safeEq(passwordRequired, o.passwordRequired);
    }

  }
  