package net.koofr.api.rest.v2;

import java.io.IOException;
import java.util.Map;

import net.koofr.api.json.JsonBase;
import net.koofr.api.json.JsonException;
import net.koofr.api.rest.v2.data.Bookmarks;
import net.koofr.api.rest.v2.data.ConnectionList;
import net.koofr.api.rest.v2.data.Self;
import net.koofr.api.rest.v2.data.Bookmarks.Bookmark;

public class RUser extends Resource {

  public RUser(Api r) {
    super(r, "/user");
  }

  public Self get() throws JsonException, IOException {
    return getResult(Self.class); 
  }

  public static class RConnections extends Resource {

    public RConnections(RUser parent) {
      super(parent, "/connections");
    }

    public ConnectionList get() throws JsonException, IOException {
      return getResult(ConnectionList.class); 
    }
    
  }
  
  public RConnections connections() throws JsonException, IOException {
    return new RConnections(this); 
  }

  public class Edit implements JsonBase {
    public String firstName;
    public String lastName;      
  }
  
  public void edit(String newFirstName, String newLastName) throws JsonException, IOException {
    Edit e = new Edit();
    e.firstName = newFirstName;
    e.lastName = newLastName;
    putJsonNoResult(e);
  }
  
  public class PasswordChange implements JsonBase {
    public String oldPassword;
    public String newPassword;
  }
  
  public class RPassword extends Resource {
    
    public RPassword() {
      super(RUser.this, "/password");
    }
    
    public void change(String oldPassword, String newPassword) throws JsonException, IOException {
      PasswordChange e = new PasswordChange();
      e.oldPassword = oldPassword;
      e.newPassword = newPassword;
      putJsonNoResult(e);      
    }
    
  }
  
  public RPassword password() {
    return new RPassword();
  }

  public class EmailChange implements JsonBase {
    public String newEmail;
  }
  
  public class REmail extends Resource {
    
    public REmail() {
      super(RUser.this, "/changeemail");
    }
    
    public void change(String newEmail) throws JsonException, IOException {
      EmailChange e = new EmailChange();
      e.newEmail = newEmail;
      putJsonNoResult(e);
    }
    
  }
  
  public REmail email() {
    return new REmail();
  }

  public class RAttributes extends Resource {
    
    public RAttributes() {
      super(RUser.this, "/attributes");
    }
    
    @SuppressWarnings("unchecked")
    public Map<String, Object> get() throws JsonException, IOException {
      return getResult(Map.class);
    }
  }
  
  public RAttributes attributes() {
    return new RAttributes();
  }

  public class RAppConfig extends Resource {
    
    public RAppConfig() {
      super(RUser.this, "/appconfig");
    }
    
    @SuppressWarnings("unchecked")
    public Map<String, Object> get() throws JsonException, IOException {
      return getResult(Map.class);
    }    
  }
  
  public RAppConfig appConfig() {
    return new RAppConfig();
  }
  
  public RSettings settings() {
    return new RSettings(this);
  }

  public class RBookmarks extends Resource {

    public RBookmarks() {
      super(RUser.this, "/bookmarks");
    }

    public Bookmarks get() throws JsonException, IOException {
      return getResult(Bookmarks.class);
    }
   
    public void create(Bookmark bookmark) throws JsonException, IOException {
      postJsonNoResult(bookmark);
    }
    
    public void edit(Bookmarks bookmarks) throws JsonException, IOException {
      putJsonNoResult(bookmarks);
    }
    
    public void delete(String mountId, String path) throws IOException {
      deleteNoResult("mountId", mountId, "path", path);
    }
    
  }
  
  public RBookmarks bookmarks() {
    return new RBookmarks();
  }
  
  public class RActivity extends Resource {
    
    public RActivity() {
      super(RUser.this, "/activity");
    }
    
    public void get() {
      throw new RuntimeException("Not implemented.");
    }
  }
  
}
