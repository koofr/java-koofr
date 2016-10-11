package net.koofr.api.rest.v2.data;

import java.util.List;
import java.util.Map;

import net.koofr.api.json.JsonBase;
import net.koofr.api.rest.v2.data.Mounts.Mount;

public class Trash implements JsonBase {

  public static class TrashFile implements JsonBase {
    public String mountId;
    public String path;
    public String name;
    public Long deleted;
    public Long size;
    public String contentType;
    public String versionId;
    public Map<String, List<String>> tags;
  }
  
  public List<TrashFile> files;
  public Map<String, Mount> mounts;
}
