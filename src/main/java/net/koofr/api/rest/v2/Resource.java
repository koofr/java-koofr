package net.koofr.api.rest.v2;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import net.koofr.api.auth.Authenticator;
import net.koofr.api.http.Client;
import net.koofr.api.http.Request;
import net.koofr.api.http.Response;
import net.koofr.api.util.Log;

public abstract class Resource {

  Authenticator auth;
  Client httpClient;
  String url;
  
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
  
  public Response get() throws IOException {
    Request r = httpClient.get(url);
    if(auth != null) {
      auth.arm(r);
    }
    return r.execute();
  }
  
  protected void logResponse(Response r, Log l) {
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
      } else {
        l.debug("No body.");
      }
    } catch(IOException ex) {
      l.debug("Request failed: " + ex);
    }
  }
  
}
