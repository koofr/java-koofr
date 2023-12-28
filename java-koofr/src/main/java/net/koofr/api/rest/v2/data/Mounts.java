package net.koofr.api.rest.v2.data;

import net.koofr.api.json.JsonBase;

import java.io.Serializable;
import java.util.List;

public class Mounts implements JsonBase, Serializable {
  private static final long serialVersionUID = 1L;

  public List<Mount> mounts;
  
  public static class MountCandidate implements JsonBase, Serializable {
    private static final long serialVersionUID = 1L;
    
    public String id;
    public String name;
    public String email;
    public String groupId;
    public String groupName;
  }
  
  public static class MountUser implements JsonBase, Serializable {
    private static final long serialVersionUID = 1L;
    
    public String id;
    public String name;
    public String email;
    public Permissions permissions;
  }
  
  public static class MountGroup implements JsonBase, Serializable {
    private static final long serialVersionUID = 1L;
    
    public String id;
    public String name;
    public Permissions permisssions; 
  }
  
  public static class MountMember implements JsonBase, Serializable {
    private static final long serialVersionUID = 1L;
    
    public String id;
    public String name;
    public String email;
    public Permissions permissions;
    public Boolean isGroup;
  }
  
  public static class MountRoot implements JsonBase, Serializable {
    private static final long serialVersionUID = 1L;
    
    public String id;
    public String name;
    public String path;
  }
  
  public static class Mount implements JsonBase, Serializable {
    private static final long serialVersionUID = 1L;

    public String id;
    public String name;
    public String type;
    public String origin;
    public MountRoot root;
    public Boolean online;
    public MountMember owner;
    public List<MountUser> users;
    public List<MountGroup> groups;
    public Boolean isShared;
    public Permissions permissions;
    public Long spaceTotal;
    public Long spaceUsed;
    public Integer version;
    public Boolean isPrimary, isDir;
    public Boolean canWrite, canUpload;
    public Boolean overQuota, almostOverQuota;
    public String deviceId;
  }

}
