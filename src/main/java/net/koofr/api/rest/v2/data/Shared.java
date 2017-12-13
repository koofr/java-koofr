package net.koofr.api.rest.v2.data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import net.koofr.api.json.JsonBase;
import net.koofr.api.rest.v2.data.Links.*;
import net.koofr.api.rest.v2.data.Receivers.*;
import net.koofr.api.rest.v2.data.Common.StringList;
import net.koofr.api.rest.v2.data.Mounts.Mount;

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
