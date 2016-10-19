package net.koofr.api.rest.v2.data;

import net.koofr.api.json.JsonBase;

public class Settings {

  private Settings() {}
  
  public static class Notifications implements JsonBase {
    public Boolean shared;
    public Boolean newComment;
    public Boolean deviceOffline;
  }  
  
  public static class Branding implements JsonBase {
    public String logo;
    public String backgroundColor;
    public String foregroundColor;
  }
  
  public static class Security implements JsonBase {
    public Boolean downloadLinkAutoPassword;
    public Boolean downloadLinkRequiresPassword;
    public Boolean uploadLinkAutoPassword;
    public Boolean uploadLinkRequiresPassword;
  }
  
  public static class Seen implements JsonBase {
    public Boolean intro;
  }

  public static class Language implements JsonBase {
    public String preferred;
  }
  
}
