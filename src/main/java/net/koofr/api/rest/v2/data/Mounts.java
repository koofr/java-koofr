package net.koofr.api.rest.v2.data;

import java.util.List;
import java.util.Map;

import net.koofr.api.json.JsonBase;

public class Mounts implements JsonBase {

  public List<Mount> mounts;
  
  public static class MountCandidate implements JsonBase {
    public String id;
    public String name;
    public String email;
    public String groupId;
    public String groupName;
  }
  
  public static class MountUser implements JsonBase {
    public String id;
    public String name;
    public String email;
    public Permissions permissions;
  }
  
  public static class MountGroup implements JsonBase {
    public String id;
    public String name;
    public Permissions permisssions; 
  }
  
  public static class MountMember implements JsonBase {
    public String id;
    public String name;
    public String email;
    public Permissions permissions;
    public Boolean isGroup;
  }
  
  public static class MountRoot implements JsonBase {
    public String id;
    public String name;
    public String path;
  }
  
  public static class Mount implements JsonBase {
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
    public Boolean isPrimary;
    public Boolean canWrite, canUpload;
    public Boolean overQuota, almostOverQuota;
  }

}
