package net.koofr.api.rest.v2.data;

import net.koofr.api.json.JsonBase;
import net.koofr.api.util.U;

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

    @Override
    public boolean equals(Object obj) {
      if(obj == null || !(obj instanceof MountCandidate)) {
        return false;
      }
      MountCandidate o = (MountCandidate)obj;
      return U.safeEq(id, o.id) &&
        U.safeEq(name, o.name) &&
        U.safeEq(email, o.email) &&
        U.safeEq(groupId, o.groupId) &&
        U.safeEq(groupName, o.groupName);
    }
  }
  
  public static class MountUser implements JsonBase, Serializable {
    private static final long serialVersionUID = 1L;
    
    public String id;
    public String name;
    public String email;
    public Permissions permissions;

    @Override
    public boolean equals(Object obj) {
      if(obj == null || !(obj instanceof MountUser)) {
        return false;
      }
      MountUser o = (MountUser)obj;
      return U.safeEq(id, o.id) &&
        U.safeEq(name, o.name) &&
        U.safeEq(email, o.email) &&
        U.safeEq(permissions, o.permissions);
    }
  }
  
  public static class MountGroup implements JsonBase, Serializable {
    private static final long serialVersionUID = 1L;
    
    public String id;
    public String name;
    public Permissions permissions; 

    @Override
    public boolean equals(Object obj) {
      if(obj == null || !(obj instanceof MountGroup)) {
        return false;
      }
      MountGroup o = (MountGroup)obj;
      return U.safeEq(id, o.id) &&
        U.safeEq(name, o.name) &&
        U.safeEq(permissions, o.permissions);
    }
  }
  
  public static class MountMember implements JsonBase, Serializable {
    private static final long serialVersionUID = 1L;
    
    public String id;
    public String name;
    public String email;
    public Permissions permissions;
    public Boolean isGroup;

    @Override
    public boolean equals(Object obj) {
      if(obj == null || !(obj instanceof MountMember)) {
        return false;
      }
      MountMember o = (MountMember)obj;
      return U.safeEq(id, o.id) &&
        U.safeEq(name, o.name) &&
        U.safeEq(email, o.email) &&
        U.safeEq(permissions, o.permissions) &&
        U.safeEq(isGroup, o.isGroup);
    }
  }
  
  public static class MountRoot implements JsonBase, Serializable {
    private static final long serialVersionUID = 1L;
    
    public String id;
    public String name;
    public String path;

    @Override
    public boolean equals(Object obj) {
      if(obj == null || !(obj instanceof MountRoot)) {
        return false;
      }
      MountRoot o = (MountRoot)obj;
      return U.safeEq(id, o.id) &&
        U.safeEq(name, o.name) &&
        U.safeEq(path, o.path);
    }
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

    @Override
    public boolean equals(Object obj) {
      if(obj == null || !(obj instanceof Mount)) {
        return false;
      }
      Mount o = (Mount)obj;
      return U.safeEq(id, o.id) &&
        U.safeEq(name, o.name) &&
        U.safeEq(type, o.type) &&
        U.safeEq(origin, o.origin) &&
        U.safeEq(type, o.type) &&
        U.safeEq(root, o.root) &&
        U.safeEq(online, o.online) &&
        U.safeEq(owner, o.owner) &&
        U.safeEq(users, o.users) &&
        U.safeEq(groups, o.groups) &&
        U.safeEq(isShared, o.isShared) &&
        U.safeEq(permissions, o.permissions) &&
        U.safeEq(spaceTotal, o.spaceTotal) &&
        U.safeEq(spaceUsed, o.spaceUsed) &&
        U.safeEq(version, o.version) &&
        U.safeEq(isPrimary, o.isPrimary) &&
        U.safeEq(isDir, o.isDir) &&
        U.safeEq(canWrite, o.canWrite) &&
        U.safeEq(canUpload, o.canUpload) &&
        U.safeEq(overQuota, o.overQuota) &&
        U.safeEq(almostOverQuota, o.almostOverQuota) &&
        U.safeEq(deviceId, o.deviceId);
    }
  }

  @Override
  public boolean equals(Object obj) {
    if(obj == null || !(obj instanceof Mounts)) {
      return false;
    }
    Mounts o = (Mounts)obj;
    return U.safeEq(mounts, o.mounts);
  }
}
