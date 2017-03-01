package net.koofr.api.http.impl.basic;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import net.koofr.api.http.Response;

public class BasicResponse implements Response {

  HttpURLConnection cnx;
  
  protected BasicResponse(HttpURLConnection cnx) {
    this.cnx = cnx;
  }
  
  @Override
  public int getStatus() throws IOException {
    return cnx.getResponseCode();
  }

  @Override
  public InputStream getInputStream() throws IOException {
    String ce = cnx.getHeaderField("Content-Encoding");
    if("gzip".equals(ce)) {
      return new GZIPInputStream(cnx.getInputStream());
    } else {
      return cnx.getInputStream();
    }
  }

  public Map<String, List<String>>getHeaders() {
    return cnx.getHeaderFields();    
  }
  
  public String getHeader(String name) {
    return cnx.getHeaderField(name);
  }
  
  public void close() {
    cnx.disconnect();
  }
  
}
