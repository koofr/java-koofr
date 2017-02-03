package net.koofr.api.rest.v2;

import java.io.IOException;

import net.koofr.api.rest.v2.data.Files.DownloadResult;

public class RUsers extends Resource {

  public RUsers(Api parent) {
    super(parent, "/users");
  }
  
  public RUser user(String id) {
    return new RUser(this, id);
  }
  
  public static class RUser extends Resource {
    public RUser(RUsers parent, String id) {
      super(parent, "/" + id);
    }    
    
    public RProfilePicture profilePicture() {
      return new RProfilePicture(this);
    }
  }
  
  public static class RProfilePicture extends Resource {
    public RProfilePicture(RUser parent) {
      super(parent, "/profile-picture");
      url = contentUrl(url);
    }
    
    public DownloadResult get() throws IOException {
      return resolveDownload(httpGet("nodefault", "true"));
    }      
  }
    
}
