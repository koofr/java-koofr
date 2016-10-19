package net.koofr.api.http;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

public interface Response {
  
  int getStatus() throws IOException;
  Map<String, List<String>> getHeaders();
  String getHeader(String name);
  InputStream getInputStream() throws IOException;

}
