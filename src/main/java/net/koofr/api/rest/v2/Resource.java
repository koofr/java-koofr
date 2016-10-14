package net.koofr.api.rest.v2;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import net.koofr.api.auth.Authenticator;
import net.koofr.api.http.Body;
import net.koofr.api.http.Client;
import net.koofr.api.http.Request;
import net.koofr.api.http.Request.TransferCallback;
import net.koofr.api.http.Response;
import net.koofr.api.http.content.JsonBody;
import net.koofr.api.http.errors.HttpException;
import net.koofr.api.json.JsonException;
import net.koofr.api.json.Transmogrifier;
import net.koofr.api.rest.v2.data.Files.DownloadResult;
import net.koofr.api.util.Log;
import net.koofr.api.util.StdLog;

public class Resource {

  Authenticator auth;
  Client httpClient;
  String url;

  Log log = new StdLog();
  boolean debugContent = false;

  public Resource(Authenticator auth, Client httpClient, String url) {
    this.auth = auth;
    this.httpClient = httpClient;
    this.url = url;
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
  
  protected Response httpArmAndExecute(Request r, Body body, TransferCallback cb) throws IOException {
    if(auth != null) {
      auth.arm(r);
    }  
    if(body != null) {
      if(debugContent) {
        if(body instanceof JsonBody) {
          log.debug(body.toString());
        }
      }      
      return r.execute(body, cb);
    } else {
      return r.execute();
    }
  }

  protected String urlEncode(String s) {
    try {
      return URLEncoder.encode(s, "UTF-8");
    } catch(UnsupportedEncodingException ex) {
      return s;
    }
  }
  
  protected String urlWithParameters(String... params) {
    if(params.length == 0) {
      return url;
    }
    if(params.length % 2 != 0) {
      throw new IllegalArgumentException("Parameters are not an even-sized array of names and values.");
    }
    StringBuilder b = new StringBuilder(url).append("?");
    for(int i = 0; i < params.length; i += 2) {
      if(i > 0) {
        b.append("&");
      }
      b.append(params[i]).append("=").append(urlEncode(params[i + 1]));
    }
    return b.toString();
  }

  protected Response httpGet(String... params) throws IOException {
    return httpArmAndExecute(httpClient.get(urlWithParameters(params)), null, null);
  }
  
  protected Response httpPut(String... params) throws IOException {
    return httpArmAndExecute(httpClient.put(urlWithParameters(params)), null, null);
  }

  protected Response httpPut(Body body, String... params) throws IOException {
    return httpArmAndExecute(httpClient.put(urlWithParameters(params)), body, null);    
  }  
    
  protected Response httpPost(String... params) throws IOException {
    return httpArmAndExecute(httpClient.post(urlWithParameters(url)), null, null);
  }

  protected Response httpPost(Body body, String... params) throws IOException {
    return httpArmAndExecute(httpClient.post(urlWithParameters(params)), body, null);
  }

  protected Response httpPost(Body body, TransferCallback cb, String... params) throws IOException {
    return httpArmAndExecute(httpClient.post(urlWithParameters(params)), body, cb);
  }
  
  protected Response httpDelete(String... params) throws IOException {
    return httpArmAndExecute(httpClient.delete(urlWithParameters(params)), null, null);
  }
  
  protected <T> T getResult(Class<T> c, String... params) throws JsonException, IOException {
    return resolveJsonResult(httpGet(params), c);
  }

  protected <T> T getResult(Class<T> c) throws JsonException, IOException {
    return resolveJsonResult(httpGet(), c);
  }

  protected void getNoResult(String... params) throws IOException {
    resolveNoResult(httpGet());
  }
  
  protected void deleteNoResult(String... params) throws IOException {
    resolveNoResult(httpDelete(params));
  }

  protected <T> T putJsonResult(Object body, Class<T> c, String... params) throws JsonException, IOException {
    return resolveJsonResult(httpPut(new JsonBody(Transmogrifier.mapObject(body)), params), c);
  }
  
  protected void putJsonNoResult(Object body, String... params) throws JsonException, IOException {
    resolveNoResult(httpPut(new JsonBody(Transmogrifier.mapObject(body)), params));
  }

  protected <T> T postJsonResult(Object body, Class<T> c, String... params) throws JsonException, IOException {
    return resolveJsonResult(httpPost(new JsonBody(Transmogrifier.mapObject(body)), params), c);
  }
  
  protected void postJsonNoResult(Object body, String... params) throws JsonException, IOException {
    resolveNoResult(httpPost(new JsonBody(Transmogrifier.mapObject(body)), params));
  }
    
  protected void resolveNoResult(Response r) throws IOException {
    if(debugContent) {
      logResponse(r, log);
      HttpException.checkResponse(r);
    } else {
      HttpException.checkResponse(r);
    }
  }
  
  protected <T> T resolveJsonResult(Response r, Class<T> c) throws JsonException, IOException {
    if(r.getStatus()/100 != 2) {
      if(debugContent) {
        logResponse(r, log);
      }
      HttpException.checkResponse(r);
    }
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

  protected DownloadResult resolveDownload(Response r) throws IOException {
    HttpException.checkResponse(r);
    DownloadResult rv = new DownloadResult();
    rv.contentType = r.getHeader("Content-Type");
    rv.contentLength = null;
    try {
      String l = r.getHeader("Content-Length");
      if(l != null) {
        rv.contentLength = Long.parseLong(l);
      }
    } catch(NumberFormatException ex) {      
    }
    rv.downloadStream = r.getInputStream();
    return rv;    
  }
  
  protected String contentUrl(String u) {
    return u.replaceFirst("/api/v2/", "/content/api/v2/");
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
