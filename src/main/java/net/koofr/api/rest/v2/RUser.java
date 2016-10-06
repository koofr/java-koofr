package net.koofr.api.rest.v2;

import java.io.IOException;
import java.util.Map;

import net.koofr.api.json.JsonBase;
import net.koofr.api.json.JsonException;
import net.koofr.api.rest.v2.data.ConnectionList;
import net.koofr.api.rest.v2.data.Self;

public class RUser extends Resource {

  public RUser(Api r) {
    super(r, "/user");
  }

  public Self get() throws JsonException, IOException {
    return getResult(Self.class); 
  }

  public class RConnections extends Resource {

    public RConnections() {
      super(RUser.this, "/connections");
    }

    public ConnectionList get() throws JsonException, IOException {
      return getResult(ConnectionList.class); 
    }
    
  }
  
  public RConnections connections() throws JsonException, IOException {
    return new RConnections(); 
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
  
}
