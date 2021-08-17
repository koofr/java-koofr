package net.koofr.api.util;

import java.util.Date;

public class StdLog implements Log {

  @Override
  public void debug(String message) {
    System.err.println(new Date().toString() + ": " + message);
  }
  
  @Override
  public void debug(String message, Throwable t) {
    System.err.println(new Date().toString() + ": " + message);
    t.printStackTrace(System.err);
  }
  
}
