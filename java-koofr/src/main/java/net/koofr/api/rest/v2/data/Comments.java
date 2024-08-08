package net.koofr.api.rest.v2.data;

import net.koofr.api.json.JsonBase;
import net.koofr.api.util.U;

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

    @Override
    public boolean equals(Object obj) {
      if(obj == null || !(obj instanceof Comment)) {
        return false;
      }
      Comment o = (Comment)obj;
      return U.safeEq(id, o.id) &&
        U.safeEq(user, o.user) &&
        U.safeEq(content, o.content) &&
        U.safeEq(added, o.added);
    }

  }
  
  public List<Comment> comments;

  @Override
  public boolean equals(Object obj) {
    if(obj == null || !(obj instanceof Comments)) {
      return false;
    }
    Comments o = (Comments)obj;
    return U.safeEq(comments, o.comments);
  }
}
