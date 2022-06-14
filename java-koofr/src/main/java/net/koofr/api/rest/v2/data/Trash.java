package net.koofr.api.rest.v2.data;

import net.koofr.api.json.JsonBase;
import net.koofr.api.rest.v2.data.Common.StringList;
import net.koofr.api.rest.v2.data.Mounts.Mount;

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
  }
  
  public List<TrashFile> files;
  public Map<String, Mount> mounts;
  public Long retentionDays;
}
