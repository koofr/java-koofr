package net.koofr.api.rest.v2.data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import net.koofr.api.json.JsonBase;
import net.koofr.api.rest.v2.data.Links.*;
import net.koofr.api.rest.v2.data.Receivers.*;
import net.koofr.api.rest.v2.data.Common.StringList;
import net.koofr.api.rest.v2.data.Mounts.Mount;

public class Places implements JsonBase, Serializable {
  private static final long serialVersionUID = 1L;

  public List<Mounts.Mount> places;  
}
