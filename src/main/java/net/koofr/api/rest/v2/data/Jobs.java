package net.koofr.api.rest.v2.data;

import java.util.List;
import java.util.Map;

import net.koofr.api.json.JsonBase;

public class Jobs implements JsonBase {

  public List<Job> jobs;
  
  public static class Job implements JsonBase {
    public String id;
    public String type;
    public Map<String, Object> parameters;
    public String state;
    public Integer progress;
    public Map<String, Object> result;
    public Long created;
  }
  
  public static class JobMountPath implements JsonBase {
    public String mountId;
    public String path;
  }
  
  public static class JobMountPathPair implements JsonBase {
    public JobMountPath src;
    public JobMountPath dst;
  }
 
  public static class JobCreate implements JsonBase {    
  }
  
  public static class JobRemoveFiles extends JobCreate {
    public List<JobMountPath> files;
  }
  
  public static class JobCopyMoveFiles extends JobCreate {
    public List<JobMountPathPair> files;
  }

  public static class JobCopyFiles extends JobCopyMoveFiles {
  }

  public static class JobMoveFiles extends JobCopyMoveFiles {
  }
  
}
