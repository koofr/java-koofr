package net.koofr.api.http.errors;

import net.koofr.api.rest.v2.data.Error;

import java.io.IOException;

public class HttpException extends IOException {
  static final long serialVersionUID = 1L;

  int code;
  Error error;

  public int getCode() {
    return code;
  }

  public Error getError() {
    return error;
  }
  
  public static class Conflict extends HttpException {
    private static final long serialVersionUID = 1L;
    
    public Conflict(Error error) {
      super(error, 409, "Conflict");
    }
  }
  
  public static class Unauthorized extends HttpException {
    private static final long serialVersionUID = 1L;

    public Unauthorized(Error error) {
      super(error, 401, "Unauthorized");
    }
  }

  public static class NotFound extends HttpException {
    private static final long serialVersionUID = 1L;

    public NotFound(Error error) {
      super(error, 404, "Not found");
    }
  }
  
  public static class Forbidden extends HttpException {
    private static final long serialVersionUID = 1L;

    public Forbidden(Error error) {
      super(error, 403, "Forbidden");
    }
  }

  public static class NoContent extends HttpException {
    private static final long serialVersionUID = 1L;

    public NoContent(Error error) {
      super(error, 204, "No content");
    }
  }

  public HttpException(Error error, int code) {
    super("Server responded with: " + code);
    this.code = code;
    this.error = error;
  }

  public HttpException(Error error, int code, String message) {
    super(message);
    this.code = code;
    this.error = error;
  }

  public static NoContent noContent() {
    return new NoContent(null);
  }
  
}
