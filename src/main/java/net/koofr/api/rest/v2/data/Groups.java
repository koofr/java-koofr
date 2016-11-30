package net.koofr.api.rest.v2.data;

import java.io.Serializable;
import java.util.List;

import net.koofr.api.json.JsonBase;

public class Groups implements JsonBase, Serializable {
  private static final long serialVersionUID = 1L;

  public List<Group> groups;
  
  public static class GroupUser implements JsonBase, Serializable {
    private static final long serialVersionUID = 1L;

    public String id;
    public String firstName, lastName;
    public String email;
    public String phoneNumber;
    public Permissions permissions;
    public Long spaceTotal;
    public Long spaceUsed;
  }
  
  public static class GroupAccount implements JsonBase, Serializable {
    private static final long serialVersionUID = 1L;

    public Long capacityMax;
    public Integer usersMax;
  }
  
  public static class GroupCommon implements JsonBase, Serializable {
    private static final long serialVersionUID = 1L;

    public Long spaceTotal;
    public Long spaceUsed;
  }
  
  public static class GroupBranding extends Settings.Branding implements JsonBase, Serializable {
    private static final long serialVersionUID = 1L;
  }
  
  public static class Group implements JsonBase, Serializable {
    private static final long serialVersionUID = 1L;

    public String id;
    public String name;
    public List<GroupUser> users;
    public Permissions permissions;
    public GroupAccount account;
    public GroupCommon common;
    public GroupBranding branding;
  }
  
}
