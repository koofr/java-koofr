package net.koofr.api.rest.v2.data;

import net.koofr.api.json.JsonBase;
import net.koofr.api.rest.v2.data.Links.Link;
import net.koofr.api.rest.v2.data.Mounts.Mount;
import net.koofr.api.rest.v2.data.Receivers.Receiver;
import net.koofr.api.util.U;

import java.io.Serializable;
import java.util.List;

public class Shared implements JsonBase, Serializable {
  private static final long serialVersionUID = 1L;

  public static class SharedFile implements JsonBase, Serializable {
    private static final long serialVersionUID = 1L;

    public String name;
    public String type;
    public Long modified;
    public Long size;
    public String contentType;
    public String hash;
    public Mount mount;
    public Link link;
    public Receiver receiver;

    @Override
    public boolean equals(Object obj) {
      if(obj == null || !(obj instanceof SharedFile)) {
        return false;
      }
      SharedFile o = (SharedFile)obj;
      return U.safeEq(name, o.name) &&
        U.safeEq(type, o.type) &&
        U.safeEq(modified, o.modified) &&
        U.safeEq(size, o.size) &&
        U.safeEq(contentType, o.contentType) &&
        U.safeEq(hash, o.hash) &&
        U.safeEq(mount, o.mount) &&
        U.safeEq(link, o.link) &&
        U.safeEq(receiver, o.receiver);
    }
  }
  
  public List<SharedFile> files;

  @Override
  public boolean equals(Object obj) {
    if(obj == null || !(obj instanceof Shared)) {
      return false;
    }
    Shared o = (Shared)obj;
    return U.safeEq(files, o.files);
  }
  
}
