package net.koofr.api.http;

import java.io.IOException;
import java.util.Map;

public interface Request {

  void setHeaders(Map<String, String> headers);
  void addHeader(String name, String value);

  Response execute();
  Response execute(Body body) throws IOException;

}
