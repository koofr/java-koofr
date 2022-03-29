package net.koofr.api.rest.v2.data;

import net.koofr.api.json.JsonBase;

import java.io.Serializable;

public class Self implements JsonBase, Serializable {
  private static final long serialVersionUID = 1L;

  public String id;
  public String firstName, lastName;
  public String email;
  public Integer level;
  
  public Self() {    
  }
  
}
