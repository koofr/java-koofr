package net.koofr.api.rest.v2.data;

import net.koofr.api.json.JsonBase;
import net.koofr.api.util.U;

import java.io.Serializable;
import java.util.List;

public class Places implements JsonBase, Serializable {
  private static final long serialVersionUID = 1L;

  public List<Mounts.Mount> places;
  
  @Override
  public boolean equals(Object obj) {
    if(obj == null || !(obj instanceof Places)) {
      return false;
    }
    Places o = (Places)obj;
    return U.safeEq(places, o.places);
  }  
}
