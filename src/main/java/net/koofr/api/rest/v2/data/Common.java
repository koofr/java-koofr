package net.koofr.api.rest.v2.data;

import java.io.Serializable;
import java.util.ArrayList;

public class Common {

  private Common() {    
  }
  
  public static class StringList extends ArrayList<String> implements Serializable {
    private static final long serialVersionUID = 1L;
  }
  
}
