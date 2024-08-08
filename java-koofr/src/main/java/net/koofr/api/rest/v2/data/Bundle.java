package net.koofr.api.rest.v2.data;

import net.koofr.api.json.JsonBase;
import net.koofr.api.rest.v2.data.Common.StringList;
import net.koofr.api.rest.v2.data.Links.Link;
import net.koofr.api.rest.v2.data.Mounts.Mount;
import net.koofr.api.rest.v2.data.Receivers.Receiver;
import net.koofr.api.util.U;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class Bundle implements JsonBase, Serializable {
  private static final long serialVersionUID = 1L;

  public static class BundleBookmark implements JsonBase, Serializable {
    private static final long serialVersionUID = 1L;
    public String name;
    public String path;

    @Override
    public boolean equals(Object obj) {
      if(obj == null || !(obj instanceof BundleBookmark)) {
        return false;
      }
      BundleBookmark o = (BundleBookmark)obj;
      return U.safeEq(name, o.name) && U.safeEq(path, o.path);
    }
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

    @Override
    public boolean equals(Object obj) {
      if(obj == null || !(obj instanceof BundleFile)) {
        return false;
      }
      BundleFile o = (BundleFile)obj;
      return U.safeEq(name, o.name) &&
        U.safeEq(type, o.type) &&
        U.safeEq(modified, o.modified) &&
        U.safeEq(size, o.size) &&
        U.safeEq(contentType, o.contentType) &&
        U.safeEq(hash, o.hash) &&
        U.safeEq(tags, o.tags) &&
        U.safeEq(mount, o.mount) &&
        U.safeEq(link, o.link) &&
        U.safeEq(receiver, o.receiver) &&
        U.safeEq(bookmark, o.bookmark) &&
        U.safeEq(vaultRepo, o.vaultRepo);
    }
  }
  
  public BundleFile file;
  public List<BundleFile> files;
  
  @Override
  public boolean equals(Object obj) {
    if(obj == null || !(obj instanceof Bundle)) {
      return false;
    }
    Bundle o = (Bundle)obj;
    return U.safeEq(file, o.file) && U.safeEq(files, o.files);
  }
}
