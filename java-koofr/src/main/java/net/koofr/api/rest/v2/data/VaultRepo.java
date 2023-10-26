package net.koofr.api.rest.v2.data;

import java.io.Serializable;

import net.koofr.api.json.JsonBase;

public class VaultRepo implements JsonBase, Serializable {
  private static final long serialVersionUID = 1L;

  public String id;
  public String mountId;
  public String name;
  public String path;
  public Long added;
}
