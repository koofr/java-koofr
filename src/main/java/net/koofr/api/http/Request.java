package net.koofr.api.http;

import java.io.IOException;
import java.util.Map;

public interface Request {

  public interface TransferCallback {
    public void progress(Long transferred, Long total);
    public boolean isCancelled();
  }
  
  void setHeaders(Map<String, String> headers);
  void addHeader(String name, String value);

  Response execute() throws IOException;
  Response execute(Body body) throws IOException;
  Response execute(Body body, TransferCallback cb) throws IOException;

}
