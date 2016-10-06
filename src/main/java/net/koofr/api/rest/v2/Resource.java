package net.koofr.api.rest.v2;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import net.koofr.api.auth.Authenticator;
import net.koofr.api.http.Body;
import net.koofr.api.http.Client;
import net.koofr.api.http.HttpException;
import net.koofr.api.http.Request;
import net.koofr.api.http.Response;
import net.koofr.api.http.content.JsonBody;
import net.koofr.api.json.JsonException;
import net.koofr.api.json.Transmogrifier;
import net.koofr.api.util.Log;
import net.koofr.api.util.StdLog;

public abstract class Resource {

  Authenticator auth;
  Client httpClient;
  String url;

  Log log = new StdLog();
  boolean debugContent = false;

  public Resource(Authenticator auth, Client httpClient) {
    this.auth = auth;
    this.httpClient = httpClient;
  }
  
  public Resource(Resource r) {
    this.auth = r.auth;
    this.httpClient = r.httpClient;
    this.url = r.url;
  }

  public Resource(Resource r, String morePath) {
    this(r);
    this.url = r.url + morePath;
  }
  
  protected Response httpGet() throws IOException {
    Request r = httpClient.get(url);
    if(auth != null) {
      auth.arm(r);
    }
    return r.execute();
  }

  protected <T> T getResult(Class<T> c) throws JsonException, IOException {
    return mapJsonResponse(httpGet(), c);
  }

  protected void getNoResult() throws JsonException, IOException {
    mapNoResponse(httpGet());
  }
  
  protected Response httpPut() throws IOException {
    Request r = httpClient.put(url);
    if(auth != null) {
      auth.arm(r);
    }
    return r.execute();    
  }

  protected Response httpPut(Body body) throws IOException {
    Request r = httpClient.put(url);
    if(auth != null) {
      auth.arm(r);
    }
    return r.execute(body);    
  }  
  
  protected void putJsonNoResult(Object body) throws JsonException, IOException {
    mapNoResponse(httpPut(new JsonBody(Transmogrifier.mapObject(body))));
  }
  
  protected void mapNoResponse(Response r) throws IOException {
    if(debugContent) {
      logResponse(r, log);
      if(r.getStatus()/100 != 2) {
        throw new HttpException(r);
      }      
    } else {
      if(r.getStatus()/100 != 2) {
        throw new HttpException(r);
      }
    }
  }
  
  protected <T> T mapJsonResponse(Response r, Class<T> c) throws JsonException, IOException {
    String contentType = r.getHeader("Content-Type");
    Map<String, List<String>> headers = r.getHeaders();
    if(debugContent) {
      for(String h: headers.keySet()) {
        log.debug("Header: " + h);
        for(String v: headers.get(h)) {
          log.debug("  " + v);
        }
      }
    }
    if(contentType == null || !contentType.startsWith("application/json")) {
      throw new BadContentTypeException();
    }
    if(debugContent) {
      String body = logResponse(r, log);
      return Transmogrifier.mappedJsonResponse(new ByteArrayInputStream(body.getBytes("UTF-8")), c);
    } else {
      return Transmogrifier.mappedJsonResponse(r.getInputStream(), c);
    }
  }

  protected String logResponse(Response r, Log l) {
    try {
      l.debug("Response status code: " + r.getStatus());
      StringBuilder b = new StringBuilder();
      byte[] buf = new byte[1024];
      InputStream i = r.getInputStream();
      int n;
      while((n = i.read(buf)) >= 0) {
        byte[] buf2 = Arrays.copyOf(buf, n);
        b.append(new String(buf2, "UTF-8"));
      }
      if(b.length() > 0) {
        l.debug("Response body: " + b.toString());
        return b.toString();
      } else {
        l.debug("No body.");
      }
    } catch(IOException ex) {
      l.debug("Request failed: " + ex);
    }
    return null;    
  }
  
}
