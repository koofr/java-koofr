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

public class SearchResult implements JsonBase, Serializable {
  private static final long serialVersionUID = 1L;

  public static class SearchHit implements JsonBase, Serializable {
    private static final long serialVersionUID = 1L;

    public String mountId;
    public String path;
    public Double score;
    public String name;
    public String type;
    public Long modified;
    public Long size;
    public String contentType;
    public Map<String, StringList> tags;
    public Mount mount;
    public Link link;
    public Receiver receiver;
    public VaultRepo vaultRepo;

    @Override
    public boolean equals(Object obj) {
      if(obj == null || !(obj instanceof SearchHit)) {
        return false;
      }
      SearchHit o = (SearchHit)obj;
      return U.safeEq(mountId, o.mountId) &&
        U.safeEq(path, o.path) &&
        U.safeEq(score, o.score) &&
        U.safeEq(name, o.name) &&
        U.safeEq(type, o.type) &&
        U.safeEq(modified, o.modified) &&
        U.safeEq(size, o.size) &&
        U.safeEq(contentType, o.contentType) &&
        U.safeEq(tags, o.tags) &&
        U.safeEq(mount, o.mount) &&
        U.safeEq(link, o.link) &&
        U.safeEq(receiver, o.receiver) &&
        U.safeEq(vaultRepo, o.vaultRepo);
    }
  }
  
  public List<SearchHit> hits;
  public Map<String, Mount> mounts;

  @Override
  public boolean equals(Object obj) {
    if(obj == null || !(obj instanceof SearchResult)) {
      return false;
    }
    SearchResult o = (SearchResult)obj;
    return U.safeEq(hits, o.hits) &&
      U.safeEq(mounts, o.mounts);
  }
}
