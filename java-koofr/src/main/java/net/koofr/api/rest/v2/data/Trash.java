package net.koofr.api.rest.v2.data;

import net.koofr.api.json.JsonBase;
import net.koofr.api.rest.v2.data.Common.StringList;
import net.koofr.api.rest.v2.data.Mounts.Mount;
import net.koofr.api.util.U;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class Trash implements JsonBase, Serializable {
  private static final long serialVersionUID = 1L;

  public static class TrashFile implements JsonBase, Serializable {
    private static final long serialVersionUID = 1L;
    
    public String mountId;
    public String path;
    public String name;
    public Long deleted;
    public Long size;
    public String contentType;
    public String versionId;
    public Map<String, StringList> tags;

    @Override
    public boolean equals(Object obj) {
      if(obj == null || !(obj instanceof TrashFile)) {
        return false;
      }
      TrashFile o = (TrashFile)obj;
      return U.safeEq(mountId, o.mountId) &&
        U.safeEq(path, o.path) &&
        U.safeEq(name, o.name) &&
        U.safeEq(deleted, o.deleted) &&
        U.safeEq(size, o.size) &&
        U.safeEq(contentType, o.contentType) &&
        U.safeEq(versionId, o.versionId) &&
        U.safeEq(tags, o.tags);
    }
  }
  
  public List<TrashFile> files;
  public Map<String, Mount> mounts;
  public Long retentionDays;

  @Override
  public boolean equals(Object obj) {
    if(obj == null || !(obj instanceof Trash)) {
      return false;
    }
    Trash o = (Trash)obj;
    return U.safeEq(files, o.files) &&
      U.safeEq(mounts, o.mounts) &&
      U.safeEq(retentionDays, o.retentionDays);
  }

}
