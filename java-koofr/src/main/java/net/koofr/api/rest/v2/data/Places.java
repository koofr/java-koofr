package net.koofr.api.rest.v2.data;

import net.koofr.api.json.JsonBase;

import java.io.Serializable;
import java.util.List;

public class Places implements JsonBase, Serializable {
  private static final long serialVersionUID = 1L;

  public List<Mounts.Mount> places;  
}
