package net.koofr.api.rest.v2.data;

import java.util.List;

import net.koofr.api.json.JsonBase;

public class Comments implements JsonBase {

  public static class Comment implements JsonBase {
    public String id;
    public User user;
    public String content;
    public Long added;
  }
  
  public List<Comment> comments;
}
