package net.koofr.api.rest.v2.data;

import java.io.Serializable;

import net.koofr.api.json.JsonBase;

public class Self implements JsonBase, Serializable {
  private static final long serialVersionUID = 1L;

  public String id;
  public String firstName, lastName;
  public String email;
  
  public Self() {    
  }
  
}
