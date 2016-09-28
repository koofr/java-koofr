package net.koofr.api.http;

import java.io.IOException;
import java.io.InputStream;

public interface Response {
  
  int getStatus() throws IOException;
  InputStream getInputStream() throws IOException;

}
