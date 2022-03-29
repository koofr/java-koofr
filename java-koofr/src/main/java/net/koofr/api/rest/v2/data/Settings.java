package net.koofr.api.rest.v2.data;

import net.koofr.api.json.JsonBase;

import java.io.Serializable;

public class Settings {

  private Settings() {}
  
  public static class Notifications implements JsonBase, Serializable {
    private static final long serialVersionUID = 1L;
    
    public Boolean shared;
    public Boolean newComment;
    public Boolean deviceOffline;
  }  
  
  public static class Branding implements JsonBase, Serializable {
    private static final long serialVersionUID = 1L;
    
    public String logo;
    public String backgroundColor;
    public String foregroundColor;
  }
  
  public static class Security implements JsonBase, Serializable {
    private static final long serialVersionUID = 1L;
    
    public Boolean downloadLinkAutoPassword;
    public Boolean downloadLinkRequiresPassword;
    public Boolean uploadLinkAutoPassword;
    public Boolean uploadLinkRequiresPassword;
  }
  
  public static class Seen implements JsonBase, Serializable {
    private static final long serialVersionUID = 1L;
    
    public Boolean intro;
  }

  public static class Language implements JsonBase, Serializable {
    private static final long serialVersionUID = 1L;
    
    public String preferred;
  }
  
}
