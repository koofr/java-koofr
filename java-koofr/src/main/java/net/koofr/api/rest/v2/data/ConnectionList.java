package net.koofr.api.rest.v2.data;

import net.koofr.api.json.JsonBase;
import net.koofr.api.rest.v2.data.Groups.Group;
import net.koofr.api.util.U;

import java.io.Serializable;
import java.util.List;

public class ConnectionList implements JsonBase, Serializable {
  private static final long serialVersionUID = 1L;

  public List<User> users;
  public List<Group> groups;
  
  public ConnectionList() {
  }

  @Override
  public boolean equals(Object obj) {
    if(obj == null || !(obj instanceof ConnectionList)) {
      return false;
    }
    ConnectionList o = (ConnectionList)obj;
    return U.safeEq(users, o.users) && U.safeEq(groups, o.groups);
  }    

}
