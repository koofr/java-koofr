package net.koofr.api.rest.v2.data;

import net.koofr.api.json.JsonBase;
import net.koofr.api.rest.v2.data.Links.Link;
import net.koofr.api.rest.v2.data.Mounts.Mount;
import net.koofr.api.rest.v2.data.Receivers.Receiver;

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
  }
  
  public List<SharedFile> files;
  
}
