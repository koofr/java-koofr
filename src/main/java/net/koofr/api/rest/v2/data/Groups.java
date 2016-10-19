package net.koofr.api.rest.v2.data;

import java.util.List;

import net.koofr.api.json.JsonBase;

public class Groups implements JsonBase {

  public List<Group> groups;
  
  public static class GroupUser implements JsonBase {
    public String id;
    public String firstName, lastName;
    public String email;
    public String phoneNumber;
    public Permissions permissions;
    public Long spaceTotal;
    public Long spaceUsed;
  }
  
  public static class GroupAccount implements JsonBase {
    public Long capacityMax;
    public Integer usersMax;
  }
  
  public static class GroupCommon implements JsonBase {
    public Long spaceTotal;
    public Long spaceUsed;
  }
  
  public static class GroupBranding extends Settings.Branding implements JsonBase {
  }
  
  public static class Group implements JsonBase {
    public String id;
    public String name;
    public List<GroupUser> users;
    public Permissions permissions;
    public GroupAccount account;
    public GroupCommon common;
    public GroupBranding branding;
  }
  
}
