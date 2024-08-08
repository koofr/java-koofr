package net.koofr.api.rest.v2.data;

import net.koofr.api.json.JsonBase;
import net.koofr.api.util.U;

import java.io.Serializable;

public class Self implements JsonBase, Serializable {
  private static final long serialVersionUID = 1L;

  public String id;
  public String firstName, lastName;
  public String email;
  public Integer level;
  
  public Self() {    
  }

  @Override
  public boolean equals(Object obj) {
    if(obj == null || !(obj instanceof Self)) {
      return false;
    }
    Self o = (Self)obj;
    return U.safeEq(id, o.id) &&
      U.safeEq(firstName, o.firstName) &&
      U.safeEq(lastName, o.lastName) &&
      U.safeEq(email, o.email) &&
      U.safeEq(level, o.level);
  }
    
}
