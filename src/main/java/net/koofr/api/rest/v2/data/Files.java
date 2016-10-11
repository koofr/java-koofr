package net.koofr.api.rest.v2.data;

import java.util.List;
import java.util.Map;

import net.koofr.api.json.JsonBase;

public class Files implements JsonBase {

  public static class File implements JsonBase {
    public String name;
    public String type;
    public Long modified;
    public Long size;
    public String contentType;
    public String hash;
    public Map<String, List<String>> tags;
    public List<File> children;
  }
  
  public static class Version implements JsonBase {
    public String id;
    public String type;
    public Long modified;
    public Long size;
    public String contentType;
    public Map<String, List<String>> tags;    
  }
  
  public static class Versions implements JsonBase {
    public List<Version> versions;
  }
  
  public List<File> files;

}
