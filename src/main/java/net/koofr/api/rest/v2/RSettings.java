package net.koofr.api.rest.v2;

import java.io.IOException;

import net.koofr.api.json.JsonException;
import net.koofr.api.rest.v2.data.Settings.*;

public class RSettings extends Resource {

  public RSettings(RUser user) {
    super(user, "/settings");
  }
  
  public class RNotifications extends Resource {
    public RNotifications() {
      super(RSettings.this, "/notifications");
    }
    
    public Notifications get() throws JsonException, IOException {
      return getResult(Notifications.class);
    }
    
    public void edit(Notifications newNotifications) throws JsonException, IOException {
      putJsonNoResult(newNotifications);
    }
  }

  public RNotifications notifications() {
    return new RNotifications();
  }
  
  public class RBranding extends Resource {
    public RBranding() {
      super(RSettings.this, "/branding");
    }

    public Branding get() throws JsonException, IOException {
      return getResult(Branding.class);
    }
    
    public void edit(Branding newBranding) throws JsonException, IOException {
      putJsonNoResult(newBranding);
    }    
  }
  
  public RBranding branding() {
    return new RBranding();
  }

  public class RSecurity extends Resource {
    public RSecurity() {
      super(RSettings.this, "/security");
    }

    public Security get() throws JsonException, IOException {
      return getResult(Security.class);
    }
    
    public void edit(Security newSecurity) throws JsonException, IOException {
      putJsonNoResult(newSecurity);
    }    
  }

  public RSecurity security() {
    return new RSecurity();
  }

  public class RSeen extends Resource {
    public RSeen() {
      super(RSettings.this, "/seen");
    }
    
    public Seen get() throws JsonException, IOException {
      return getResult(Seen.class);
    }
    
    public void edit(Seen newSeen) throws JsonException, IOException {
      putJsonNoResult(newSeen);
    }        
  }

  public RSeen seen() {
    return new RSeen();
  }

  public class RLanguage extends Resource {
    public RLanguage() {
      super(RSettings.this, "/seen");
    }
    
    public Language get() throws JsonException, IOException {
      return getResult(Language.class);
    }
    
    public void edit(Language newLanguage) throws JsonException, IOException {
      putJsonNoResult(newLanguage);
    }        
  }

  public RLanguage language() {
    return new RLanguage();
  }
    
}
