package net.koofr.api.rest.v2;

import net.koofr.api.http.content.MultipartBody;
import net.koofr.api.json.JsonBase;
import net.koofr.api.json.JsonException;
import net.koofr.api.rest.v2.data.Bookmarks;
import net.koofr.api.rest.v2.data.Bookmarks.Bookmark;
import net.koofr.api.rest.v2.data.ConnectionList;
import net.koofr.api.rest.v2.data.Files.DownloadResult;
import net.koofr.api.rest.v2.data.Self;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public class RSelf extends Resource {

  public RSelf(Api r) {
    super(r, "/user");
  }

  public Self get() throws JsonException, IOException {
    return getResult(Self.class); 
  }

  public static class RConnections extends Resource {

    public RConnections(RSelf parent) {
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
      super(RSelf.this, "/password");
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
      super(RSelf.this, "/changeemail");
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
      super(RSelf.this, "/attributes");
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
      super(RSelf.this, "/appconfig");
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
      super(RSelf.this, "/bookmarks");
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
      super(RSelf.this, "/activity");
    }
    
    public void get() {
      throw new RuntimeException("Not implemented.");
    }
  }
  
  public RActivity activity() {
    return new RActivity();
  }
  
  public static class RProfilePicture extends Resource {
    public RProfilePicture(RSelf parent) {
      super(parent, "/profile-picture");
      url = contentUrl(url);
    }
    
    public DownloadResult get() throws IOException {
      return resolveDownload(httpGet("nodefault", "true"));
    }
    
    public void update(InputStream content, String name, String contentType) throws IOException {
      new Update(this).update(content, name, contentType);
    }
    
    private static class Update extends Resource {
      public Update(RProfilePicture parent) {
        super(parent, "/update");
      }
      
      public void update(InputStream content, String name, String contentType) throws IOException {
        MultipartBody b = new MultipartBody(name, contentType, content);
        resolveNoResult(httpPost(b));
      }
    }
    
  }
  
  public RProfilePicture profilePicture() {
    return new RProfilePicture(this);
  }
  
}
