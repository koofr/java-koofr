package net.koofr.api.rest.v2.data;

import net.koofr.api.json.JsonBase;

import java.io.Serializable;
import java.util.List;

public class Comments implements JsonBase, Serializable {
  private static final long serialVersionUID = 1L;

  public static class Comment implements JsonBase, Serializable {
    private static final long serialVersionUID = 1L;

    public String id;
    public User user;
    public String content;
    public Long added;
  }
  
  public List<Comment> comments;
}
