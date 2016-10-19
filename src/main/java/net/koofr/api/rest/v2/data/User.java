package net.koofr.api.rest.v2.data;

import net.koofr.api.json.JsonBase;;

public class User implements JsonBase {

  public String id;
  public String name;
  public String email;
  public Permissions permissions;  
  
}
