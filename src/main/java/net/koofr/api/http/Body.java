package net.koofr.api.http;

import java.io.IOException;
import java.io.InputStream;

public interface Body {
  
  String getContentType();
  Long getContentLength();
  InputStream getInputStream() throws IOException;

}
