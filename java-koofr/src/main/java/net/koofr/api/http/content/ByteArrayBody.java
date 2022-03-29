package net.koofr.api.http.content;

import net.koofr.api.http.Body;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

public class ByteArrayBody implements Body {

  byte[] content; 
  String contentType;
  
  protected ByteArrayBody() {    
  }
  
  public ByteArrayBody(byte[] content, String contentType) {
    this.content = content;
    this.contentType = contentType;
  }
  
  @Override
  public String getContentType() {
    return contentType;
  }

  @Override
  public Long getContentLength() {
    return new Long(content.length);
  }

  @Override
  public InputStream getInputStream() throws IOException {
    return new ByteArrayInputStream(content);
  }

  public String toString() {
    try {
      return new String(content, "UTF-8");
    } catch(UnsupportedEncodingException ex) {
      return super.toString();
    }
  }
  
}
