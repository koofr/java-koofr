package net.koofr.api.rest.v2.data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import net.koofr.api.json.JsonBase;

public class Jobs implements JsonBase, Serializable {
  private static final long serialVersionUID = 1L;

  public List<Job> jobs;
  
  public static class AttributeMap extends HashMap<String, Object> implements Serializable {
    private static final long serialVersionUID = 1L;
  };
  
  public static class Job implements JsonBase, Serializable {
    private static final long serialVersionUID = 1L;

    public String id;
    public String type;    
    public AttributeMap parameters;
    public String state;
    public Integer progress;
    public AttributeMap result;
    public Long created;
  }
  
  public static class JobMountPath implements JsonBase, Serializable {
    private static final long serialVersionUID = 1L;

    public String mountId;
    public String path;
  }
  
  public static class JobMountPathPair implements JsonBase, Serializable {
    private static final long serialVersionUID = 1L;

    public JobMountPath src;
    public JobMountPath dst;
  }
 
  public static class JobCreate implements JsonBase {    
  }
  
  public static class JobRemoveFiles extends JobCreate implements Serializable {
    private static final long serialVersionUID = 1L;

    public List<JobMountPath> files;
  }
  
  public static class JobCopyMoveFiles extends JobCreate implements Serializable {
    private static final long serialVersionUID = 1L;

    public List<JobMountPathPair> files;
  }

  public static class JobCopyFiles extends JobCopyMoveFiles implements Serializable {
    private static final long serialVersionUID = 1L;
  }

  public static class JobMoveFiles extends JobCopyMoveFiles implements Serializable {
    private static final long serialVersionUID = 1L;
  }
  
}
