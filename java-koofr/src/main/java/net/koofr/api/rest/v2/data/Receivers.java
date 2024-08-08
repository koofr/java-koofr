package net.koofr.api.rest.v2.data;

import net.koofr.api.json.JsonBase;
import net.koofr.api.util.U;

import java.io.Serializable;
import java.util.List;

public class Receivers implements JsonBase, Serializable {
  private static final long serialVersionUID = 1L;

  public static class Receiver extends BaseLink implements Serializable {
    private static final long serialVersionUID = 1L;
    
    public Boolean alert;

    @Override
    public boolean equals(Object obj) {
      if(obj == null || !(obj instanceof Receiver)) {
        return false;
      }
      Receiver o = (Receiver)obj;
      return super.equals(obj) && U.safeEq(alert, o.alert);
    }  
  }
  
  public List<Receiver> receivers;

  @Override
  public boolean equals(Object obj) {
    if(obj == null || !(obj instanceof Receivers)) {
      return false;
    }
    Receivers o = (Receivers)obj;
    return U.safeEq(receivers, o.receivers);
  }
}
