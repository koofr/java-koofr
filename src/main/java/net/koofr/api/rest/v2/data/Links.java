package net.koofr.api.rest.v2.data;

import java.io.Serializable;
import java.util.List;

import net.koofr.api.json.JsonBase;

public class Links implements JsonBase, Serializable {
  private static final long serialVersionUID = 1L;

  public static class Link extends BaseLink implements Serializable {
    private static final long serialVersionUID = 1L;

    public Boolean passwordRequired;
  }

  public List<Link> links;  
  
}
