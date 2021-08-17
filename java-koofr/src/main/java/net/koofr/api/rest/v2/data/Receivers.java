package net.koofr.api.rest.v2.data;

import java.io.Serializable;
import java.util.List;

import net.koofr.api.json.JsonBase;

public class Receivers implements JsonBase, Serializable {
  private static final long serialVersionUID = 1L;

  public static class Receiver extends BaseLink implements Serializable {
    private static final long serialVersionUID = 1L;
    
    public Boolean alert;
  }
  
  public List<Receiver> receivers;
    
}
