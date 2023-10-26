package net.koofr.api.rest.v2.data;

import net.koofr.api.json.JsonBase;
import net.koofr.api.rest.v2.data.Common.StringList;
import net.koofr.api.rest.v2.data.Links.Link;
import net.koofr.api.rest.v2.data.Mounts.Mount;
import net.koofr.api.rest.v2.data.Receivers.Receiver;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class Bundle implements JsonBase, Serializable {
  private static final long serialVersionUID = 1L;

  public static class BundleBookmark implements JsonBase, Serializable {
    private static final long serialVersionUID = 1L;
    public String name;
    public String path;
  }
  
  public static class BundleFile implements JsonBase, Serializable {
    private static final long serialVersionUID = 1L;

    public String name;
    public String type;
    public Long modified;
    public Long size;
    public String contentType;
    public String hash;
    public Map<String, StringList> tags;
    public Mount mount;
    public Link link;
    public Receiver receiver;
    public BundleBookmark bookmark;
    public VaultRepo vaultRepo;
  }
  
  public BundleFile file;
  public List<BundleFile> files;
  
}
