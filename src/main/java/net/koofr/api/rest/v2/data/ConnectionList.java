package net.koofr.api.rest.v2.data;

import java.io.Serializable;
import java.util.List;

import net.koofr.api.json.JsonBase;
import net.koofr.api.rest.v2.data.Groups.Group;

public class ConnectionList implements JsonBase, Serializable {
  private static final long serialVersionUID = 1L;

  public List<User> users;
  public List<Group> groups;
  
  public ConnectionList() {
  }

}
