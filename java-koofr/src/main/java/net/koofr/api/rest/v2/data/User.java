package net.koofr.api.rest.v2.data;

import net.koofr.api.json.JsonBase;
import net.koofr.api.util.U;

import java.io.Serializable;

public class User implements JsonBase, Serializable {
  private static final long serialVersionUID = 1L;

  public String id;
  public String name;
  public String email;
  public Permissions permissions;  
  
  @Override
  public boolean equals(Object obj) {
    if(obj == null || !(obj instanceof User)) {
      return false;
    }
    User o = (User)obj;
    return U.safeEq(id, o.id) &&
      U.safeEq(name, o.name) &&
      U.safeEq(email, o.email) &&
      U.safeEq(permissions, o.permissions);
  }

}
