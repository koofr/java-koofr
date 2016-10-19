package net.koofr.api.rest.v2.data;

import java.util.List;
import java.util.Map;

import net.koofr.api.json.JsonBase;
import net.koofr.api.rest.v2.data.Links.*;
import net.koofr.api.rest.v2.data.Receivers.*;
import net.koofr.api.rest.v2.data.Common.StringList;
import net.koofr.api.rest.v2.data.Mounts.Mount;

public class Bundle implements JsonBase {

  public static class BundleBookmark implements JsonBase {
    public String name;
    public String path;
  }
  
  public static class BundleFile implements JsonBase {
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
  }
  
  public BundleFile file;
  public List<BundleFile> files;
  
}
