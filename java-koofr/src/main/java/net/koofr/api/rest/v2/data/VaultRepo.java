package net.koofr.api.rest.v2.data;

import java.io.Serializable;

import net.koofr.api.json.JsonBase;
import net.koofr.api.util.U;

public class VaultRepo implements JsonBase, Serializable {
  private static final long serialVersionUID = 1L;

  public String id;
  public String mountId;
  public String name;
  public String path;
  public Long added;

  @Override
  public boolean equals(Object obj) {
    if(obj == null || !(obj instanceof VaultRepo)) {
      return false;
    }
    VaultRepo o = (VaultRepo)obj;
    return  U.safeEq(id, o.id) &&
      U.safeEq(mountId, o.mountId) &&
      U.safeEq(name, o.name) &&
      U.safeEq(path, o.path) &&
      U.safeEq(added, o.added);
  }

}
