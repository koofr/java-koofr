package net.koofr.api.rest.v2;

import java.io.IOException;

import net.koofr.api.json.JsonBase;
import net.koofr.api.json.JsonException;
import net.koofr.api.rest.v2.data.Mounts;
import net.koofr.api.rest.v2.data.Permissions;
import net.koofr.api.rest.v2.data.Mounts.*;

public class RMounts extends Resource {

  public RMounts(Api parent) {
    super(parent, "/mounts");
  }
  
  public Mounts get() throws JsonException, IOException {
    return getResult(Mounts.class);
  }
  
  public RMount mount(String id) {
    return new RMount(this, id);
  }
  
  public static class RMount extends Resource {
    public RMount(RMounts parent, String id) {
      super(parent, "/" + id);
    }
    
    public Mount get() throws JsonException, IOException {
      return getResult(Mount.class);
    }
    
    public static class MountEdit implements JsonBase {
      public String name;
    }
    
    public void edit(String newName) throws JsonException, IOException {
      MountEdit e = new MountEdit();
      e.name = newName;
      putJsonNoResult(e);
    }
    
    public void delete() throws JsonException, IOException {
      deleteNoResult();
    }

    public static class SubmountCreate implements JsonBase {
      public String name;
      public String path;
    }    
    
    public Mount createSubmount(String name, String path) throws JsonException, IOException {
      SubmountCreate c = new SubmountCreate();
      c.path = path;
      c.name = name;

      return new Resource(this, "/submounts").postJsonResult(c, Mount.class);
    }
    
    public RMountUsers users() {
      return new RMountUsers(this);
    }

    public RMountGroup group(String id) {
      return new RMountGroup(this, id);
    }
  
  }
  
  public static class RMountUsers extends Resource {
    public RMountUsers(RMount parent) {
      super(parent, "/users");
    }
    
    public static class MountUserAdd implements JsonBase {
      String id;
      String email;
      Permissions permissions;
    }
    
    public MountUser add(String id, String email, Permissions permissions) throws JsonException, IOException {
      MountUserAdd a = new MountUserAdd();
      a.id = id;
      a.email = email;
      a.permissions = permissions;
      return postJsonResult(a, MountUser.class);
    }
  }

  public static class RMountUser extends Resource {
    public RMountUser(RMountUsers parent, String id) {
      super(parent, "/" + id);
    }
    
    public static class MountUserEdit implements JsonBase {
      Permissions permissions;
    }
    
    public void edit(Permissions permissions) throws JsonException, IOException {
      MountUserEdit e = new MountUserEdit();
      e.permissions = permissions;
      putJsonNoResult(e);
    }
    
    public void delete() throws JsonException, IOException {
      deleteNoResult();
    }
  }
  
  public static class RMountGroup extends Resource {
    public RMountGroup(RMount parent, String id) {
      super(parent, "/groups/" + id);
    }

    public static class MountGroupEdit implements JsonBase {
      Permissions permissions;
    }
    
    public void add(Permissions permissions) throws JsonException, IOException {
      MountGroupEdit e = new MountGroupEdit();
      e.permissions = permissions;
      putJsonNoResult(e);
    }
    
    public void delete() throws JsonException, IOException {
      deleteNoResult();
    }  
  }

}
