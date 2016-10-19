package net.koofr.api.rest.v2.data;

import java.util.List;

import net.koofr.api.json.JsonBase;
import net.koofr.api.rest.v2.data.Groups.Group;

public class ConnectionList implements JsonBase {

  public List<User> users;
  public List<Group> groups;
  
  public ConnectionList() {
  }

}
