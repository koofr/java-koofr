package net.koofr.api.json;

public class JsonException extends Exception {
  private static final long serialVersionUID = 1L;

  public JsonException(String msg) {
    super(msg);
  }
  
  public JsonException(String msg, Throwable cause) {
    super(msg, cause);
  }

}
