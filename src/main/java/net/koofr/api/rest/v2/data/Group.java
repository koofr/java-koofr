package net.koofr.api.rest.v2.data;

import java.util.List;

import net.koofr.api.json.JsonBase;;

public class Group implements JsonBase {

  public String id;
  public String name;
  public List<User> users;
  public Permissions permissions;
  
}
