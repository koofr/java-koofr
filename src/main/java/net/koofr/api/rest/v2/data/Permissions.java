package net.koofr.api.rest.v2.data;

import java.util.HashMap;

public class Permissions extends HashMap<String, Boolean> {
  private static final long serialVersionUID = 1L;
  
  public static enum P {
    READ("READ"),
    WRITE("WRITE"),
    COMMENT("COMMENT"),
    OWNER("OWNER"),
    MOUNT("MOUNT"),
    CREATE_RECEIVER("CREATE_RECEIVER"),
    CREATE_LINK("CREATE_LINK"),
    CREATE_ACTION("CREATE_ACTION");
    
    private final String p;
    private P(String p) {
      this.p = p;
    }
    public String toString() {
      return p;
    }
  };
    
  public Permissions() {
  }
  
}
