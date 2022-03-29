package net.koofr.api.rest.v2;

import net.koofr.api.auth.Authenticator;
import net.koofr.api.http.Body;
import net.koofr.api.http.Client;
import net.koofr.api.http.Request;
import net.koofr.api.http.Request.TransferCallback;
import net.koofr.api.http.Response;
import net.koofr.api.http.content.JsonBody;
import net.koofr.api.http.errors.BadContentTypeException;
import net.koofr.api.http.errors.HttpException;
import net.koofr.api.json.JsonException;
import net.koofr.api.json.Transmogrifier;
import net.koofr.api.rest.v2.data.Error;
import net.koofr.api.rest.v2.data.Files.DownloadResult;
import net.koofr.api.util.Log;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Resource {

  Authenticator auth;
  Client httpClient;
  String url;
  
  protected static Log log = null;
  private static boolean shouldLogHttp() { return log != null; }
  public static void setHttpLog(Log log) { Resource.log = log; }

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
      if(Resource.shouldLogHttp()) {
        if(body instanceof JsonBody) {
          Resource.log.debug(body.toString());
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
    return httpArmAndExecute(httpClient.post(urlWithParameters(params)), null, null);
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

  protected <T> T deleteResult(Class<T> c, String... params) throws JsonException, IOException {
    return resolveJsonResult(httpDelete(params), c);
  }
  
  protected <T> T putJsonResult(Object body, Class<T> c, String... params) throws JsonException, IOException {
    return resolveJsonResult(httpPut(new JsonBody(Transmogrifier.mapObject(body)), params), c);
  }
  
  protected <T> T putJsonResult(Class<T> c, String... params) throws JsonException, IOException {
    return resolveJsonResult(httpPut(params), c);
  }
  
  protected void putJsonNoResult(Object body, String... params) throws JsonException, IOException {
    resolveNoResult(httpPut(new JsonBody(Transmogrifier.mapObject(body)), params));
  }

  protected void putJsonNoResult(String... params) throws JsonException, IOException {
    resolveNoResult(httpPut(params));
  }
  
  protected <T> T postJsonResult(Object body, Class<T> c, String... params) throws JsonException, IOException {
    return resolveJsonResult(httpPost(new JsonBody(Transmogrifier.mapObject(body)), params), c);
  }

  protected <T> T postJsonResult(Class<T> c, String... params) throws JsonException, IOException {
    return resolveJsonResult(httpPost(params), c);
  }
  
  protected void postJsonNoResult(Object body, String... params) throws JsonException, IOException {
    resolveNoResult(httpPost(new JsonBody(Transmogrifier.mapObject(body)), params));
  }

  protected void postJsonNoResult(String... params) throws JsonException, IOException {
    resolveNoResult(httpPost(params));
  }

  public static Response checkResponse(Response rs) throws IOException {
    int code = rs.getStatus();
    if(code / 100 == 2) {
      return rs;
    }
    Error error = null;
    try {
      if(shouldLogHttp()) {
        String body = Resource.logResponse(rs);
        error = Transmogrifier.mappedJsonResponse(new ByteArrayInputStream(body.getBytes("UTF-8")), Error.class);
      } else {
        error = Transmogrifier.mappedJsonResponse(rs.getErrorStream(), Error.class);
      }
    } catch(Exception ex) {
    }
    switch(code) {
      case 404:
        throw new HttpException.NotFound(error);
      case 401:
        throw new HttpException.Unauthorized(error);
      case 403:
        throw new HttpException.Forbidden(error);
      case 409:
        throw new HttpException.Conflict(error);
      default:
        throw new HttpException(error, code);
    }
  }


  protected void resolveNoResult(Response r) throws IOException {
    try {
      Resource.checkResponse(r);
      if(Resource.shouldLogHttp()) {
        Resource.logResponse(r);
      }
    } finally {
      r.close();
    }
  }
  
  protected <T> T resolveJsonResult(Response r, Class<T> c) throws JsonException, IOException {
    try {
      if(r.getStatus()/100 != 2) {
        Resource.checkResponse(r);
      }
      if(r.getStatus() != 204) {
        String contentType = r.getHeader("Content-Type");
        Map<String, List<String>> headers = r.getHeaders();
        if(Resource.shouldLogHttp()) {
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
        if(Resource.shouldLogHttp()) {
          String body = Resource.logResponse(r);
          return Transmogrifier.mappedJsonResponse(new ByteArrayInputStream(body.getBytes("UTF-8")), c);
        } else {
          return Transmogrifier.mappedJsonResponse(r.getInputStream(), c);
        }
      } else {
        throw HttpException.noContent();
      }
    } finally {
      r.close();
    }
  }

  protected DownloadResult resolveDownload(Response r) throws IOException {
    try {
      Resource.checkResponse(r);
      DownloadResult rv = new DownloadResult(r);
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
    } catch(IOException ex) {
      r.close();
      throw ex;
    }
  }

  protected String contentUrl(String u) {
    return u.replaceFirst("/api/v2/", "/content/api/v2/");
  }
  
  private static String logResponse(Response r) {
    try {
      int status = r.getStatus();
      Resource.log.debug("Response status code: " + status);
      StringBuilder b = new StringBuilder();
      byte[] buf = new byte[1024];
      InputStream i;
      if(status/100 == 2) {
        i = r.getInputStream();
      } else {
        i = r.getErrorStream();
      }
      int n;
      while((n = i.read(buf)) >= 0) {
        byte[] buf2 = Arrays.copyOf(buf, n);
        b.append(new String(buf2, "UTF-8"));
      }
      if(b.length() > 0) {
        Resource.log.debug("Response body: " + b.toString());
        return b.toString();
      } else {
        Resource.log.debug("No body.");
      }
    } catch(IOException ex) {
      Resource.log.debug("Request failed: " + ex);
    }
    return null;    
  }
  
}
