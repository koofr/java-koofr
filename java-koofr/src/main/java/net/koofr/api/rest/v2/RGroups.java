package net.koofr.api.rest.v2;

import java.io.IOException;
import java.util.Map;

import net.koofr.api.json.JsonBase;
import net.koofr.api.json.JsonException;
import net.koofr.api.rest.v2.data.Groups;
import net.koofr.api.rest.v2.data.Groups.GroupBranding;
import net.koofr.api.rest.v2.data.Permissions;

public class RGroups extends Resource {
  
  public RGroups(Api api) {
    super(api, "/groups");
  }

  public Groups get() throws JsonException, IOException {
    return getResult(Groups.class);
  }

  public static class RGroup extends Resource {
    public RGroup(RGroups parent, String id) {
      super(parent, "/" + id);
    }
    
    public Groups.Group get() throws JsonException, IOException {
      return getResult(Groups.Group.class);
    }
    
    public RGroupUsers users() {
      return new RGroupUsers(this);
    }
    
    public RGroupBranding branding() {
      return new RGroupBranding(this);
    }
    
    public RGroupAttributes attributes() {
      return new RGroupAttributes(this);
    }
    
    public RGroupCommon common() {
      return new RGroupCommon(this);
    }
  }

  public static class RGroupUsers extends Resource {
    public RGroupUsers(RGroup parent) {
      super(parent, "/users");
    }

    public static class GroupUserCreate implements JsonBase {
      public String email;
      public Permissions permissions;
      public Long spaceTotal;
    }

    public Groups.GroupUser add(String email, Permissions permissions, Long spaceTotal) throws JsonException, IOException {
      GroupUserCreate b = new GroupUserCreate();
      b.email = email;
      b.permissions = permissions;
      b.spaceTotal = spaceTotal;
      return postJsonResult(b, Groups.GroupUser.class);
    }
    
    public RGroupUser user(String id) {
      return new RGroupUser(this, id);
    }
  }
  
  public static class RGroupUser extends Resource {
    public RGroupUser(RGroupUsers parent, String id) {
      super(parent, "/" + id);
    }

    public static class GroupUserEdit implements JsonBase {
      public Permissions permissions;
      public Long spaceTotal;
    }

    public void edit(Permissions permissions, Long spaceTotal) throws JsonException, IOException {
      GroupUserEdit b = new GroupUserEdit();
      b.permissions = permissions;
      b.spaceTotal = spaceTotal;
      putJsonNoResult(b);
    }
    
    public void delete() throws IOException {
      deleteNoResult();
    }
  }
  
  public RGroup group(String id) {
    return new RGroup(this, id);
  }
  
  public static class RGroupAttributes extends Resource {
    public RGroupAttributes(RGroup parent) {
      super(parent, "/attributes");
    }
    
    @SuppressWarnings("unchecked")
    public Map<String, Object> get() throws JsonException, IOException {
      return (Map<String, Object>)getResult(Map.class);
    }
  }

  public static class RGroupCommon extends Resource {
    public RGroupCommon(RGroup parent) {
      super(parent, "/common");
    }

    public static class GroupCommonEdit implements JsonBase {
      public Long spaceTotal;
    }
        
    public void edit(Long spaceTotal) throws JsonException, IOException {
      GroupCommonEdit c = new GroupCommonEdit();
      c.spaceTotal = spaceTotal;
      putJsonNoResult(c);
    }
  }

  public static class RGroupBranding extends Resource {
    public RGroupBranding(RGroup parent) {
      super(parent, "/branding");
    }
    
    public void edit(GroupBranding b) throws JsonException, IOException {
      putJsonNoResult(b);
    }    
  }
  
}
