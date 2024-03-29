package net.koofr.api.http.impl.basic;

import net.koofr.api.http.Body;
import net.koofr.api.http.Request;
import net.koofr.api.http.Response;
import net.koofr.api.http.errors.CancelledException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.Map;

public class BasicRequest implements Request {

  static final int BUFFER_SIZE = 64*1024;
  
  HttpURLConnection cnx;
  
  protected BasicRequest(HttpURLConnection cnx) {
    this.cnx = cnx;
  }

  @Override
  public void setConnectTimeout(int t) {
    cnx.setConnectTimeout(t);
  }
  
  @Override
  public void setReadTimeout(int t) {
    cnx.setReadTimeout(t);
  }
  
  @Override
  public void setHeaders(Map<String, String> headers) {
    throw new RuntimeException("Operation not supported.");
  }

  @Override
  public void addHeader(String name, String value) {
    cnx.setRequestProperty(name, value);
  }

  @Override
  public Response execute() {
    cnx.setRequestProperty("Accept-Encoding", "gzip");
    return new BasicResponse(cnx);
  }

  @Override
  public Response execute(Body body) throws IOException {
    return execute(body, null);
  }
  
  @Override
  public Response execute(Body body, TransferCallback cb) throws IOException {
    String contentType = body.getContentType();
    Long contentLength = body.getContentLength();
    cnx.setRequestProperty("Accept-Encoding", "gzip");
    if(contentType != null) {
      cnx.setRequestProperty("Content-Type", contentType);
    }
    if(contentLength != null) {
      cnx.setRequestProperty("Content-Length", contentLength.toString());
      cnx.setFixedLengthStreamingMode(contentLength);
    } else {
      cnx.setChunkedStreamingMode(0);      
    }
    OutputStream o = cnx.getOutputStream();
    InputStream i = body.getInputStream();
    byte b[] = new byte[BUFFER_SIZE];
    int n = 0;
    long transferred = 0;
    while((n = i.read(b)) >= 0) {
      if(cb != null && cb.isCancelled()) {
        throw new CancelledException();
      }
      if(n > 0) {
        o.write(b, 0, n);
        if(cb != null) {
          transferred += n;
          cb.progress(transferred, contentLength);
        }
      }
    }
    return new BasicResponse(cnx);
  }

}
