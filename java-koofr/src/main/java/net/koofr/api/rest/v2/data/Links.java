package net.koofr.api.rest.v2.data;

import net.koofr.api.json.JsonBase;
import net.koofr.api.util.U;

import java.io.Serializable;
import java.util.List;

public class Links implements JsonBase, Serializable {
  private static final long serialVersionUID = 1L;

  public static class Link extends BaseLink implements Serializable {
    private static final long serialVersionUID = 1L;
  }

  public List<Link> links;  

  @Override
  public boolean equals(Object obj) {
    if(obj == null || !(obj instanceof Links)) {
      return false;
    }
    Links o = (Links)obj;
    return U.safeEq(links, o.links);
  }
}
