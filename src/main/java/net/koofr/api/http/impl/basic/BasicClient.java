package net.koofr.api.http.impl.basic;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import net.koofr.api.http.Client;
import net.koofr.api.http.Request;

public class BasicClient extends Client<BasicRequest> {
  
  @Override
  protected BasicRequest createRequest(String url, Request.Method method)
      throws MalformedURLException, IOException, IllegalArgumentException {
    URL u = new URL(url);
    BasicRequest r = new BasicRequest((HttpURLConnection)u.openConnection());
    if(method == Request.Method.GET) {
      r.cnx.setRequestMethod("GET");
      r.cnx.setDoInput(true);      
    } else if(method == Request.Method.PUT) {
      r.cnx.setRequestMethod("PUT");
      r.cnx.setDoInput(true);
      r.cnx.setDoOutput(true);      
    } else if(method == Request.Method.POST) {
      r.cnx.setRequestMethod("POST");
      r.cnx.setDoInput(true);
      r.cnx.setDoOutput(true);      
    } else if(method == Request.Method.DELETE) {
      r.cnx.setRequestMethod("DELETE");
      r.cnx.setDoInput(true);      
    }
    r.addHeader("X-Koofr-Version", "2.1");
    return r;
  }

}
