package net.koofr.api.http;

import java.io.IOException;

public class HttpException extends IOException {
  static final long serialVersionUID = 1L;

  int code;
  
  private void from(Response rs) {
    try {
      this.code = rs.getStatus();
    } catch(IOException ex) {
      this.initCause(ex);
    }    
  }
  
  public HttpException(Response rs) {
    from(rs);
  }
  
  public HttpException(Response rs, String message) {
    super(message);
    from(rs);
  }
  
  public HttpException(String message, Throwable cause) {
    super(message, cause);
  }
  
}
