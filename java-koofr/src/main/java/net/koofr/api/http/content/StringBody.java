package net.koofr.api.http.content;

import java.io.UnsupportedEncodingException;

public class StringBody extends ByteArrayBody {

  public StringBody(String content, String contentType) throws UnsupportedEncodingException {
    super(content.getBytes("UTF-8"), contentType);
  }
  
  public StringBody(String content) throws UnsupportedEncodingException {
    this(content, "text/plain");
  }
  
}
