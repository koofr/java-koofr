package net.koofr.api.rest.v2;

import net.koofr.api.json.JsonBase;
import net.koofr.api.json.JsonException;
import net.koofr.api.rest.v2.RMounts.RMount;
import net.koofr.api.rest.v2.data.Comments;
import net.koofr.api.rest.v2.data.Comments.Comment;

import java.io.IOException;

public class RComments extends Resource {

  public RComments(RMount parent) {
    super(parent, "/comments");
  }
  
  public Comments get() throws IOException, JsonException {
    return getResult(Comments.class);
  }

  public static class RComment extends Resource {
    public RComment(RComments parent, String id) {
      super(parent, "/" + id);
    }
    
    public Comment get() throws IOException, JsonException {
      return getResult(Comment.class);
    }
    
    public void delete() throws IOException, JsonException {
      deleteNoResult();
    }
  }
  
  public RComment comment(String id) {
    return new RComment(this, id);
  }
  
  public static class CommentCreate implements JsonBase {
    public String content;
  }
  
  public Comment create(String content) throws IOException, JsonException {
    CommentCreate c = new CommentCreate();
    c.content = content;
    return postJsonResult(c, Comment.class);
  }
  
  private static class RCommentsRange extends Resource {
    public RCommentsRange(RComments parent) {
      super(parent, "/range");
    }
    
    public Comments get(Integer from, Integer limit) throws IOException, JsonException {
      int args = 0;
      if(from != null) args += 2;
      if(limit != null) args += 2;
      String[] params = new String[args];
      args = 0;
      if(from != null) {
        params[args++] = "from";
        params[args++] = from.toString();
      }
      if(limit != null) {
        params[args++] = "limit";
        params[args++] = limit.toString();
      }
      return getResult(Comments.class, params);
    }
  }
  
  public Comments range(Integer from, Integer limit) throws IOException, JsonException {
    return new RCommentsRange(this).get(from, limit);
  }
  
}
