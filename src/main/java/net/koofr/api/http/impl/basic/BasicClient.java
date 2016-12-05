package net.koofr.api.http.impl.basic;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import net.koofr.api.http.Client;
import net.koofr.api.http.Request;

public class BasicClient implements Client {
  
  private BasicRequest createRequest(String url) throws MalformedURLException, IOException {
    URL u = new URL(url);
    BasicRequest r = new BasicRequest((HttpURLConnection)u.openConnection());
    r.addHeader("X-Koofr-Version", "2.1");
    return r;
  }
  
  @Override
  public Request get(String url) throws MalformedURLException, IOException {
    BasicRequest r = createRequest(url);
    r.cnx.setRequestMethod("GET");
    r.cnx.setDoInput(true);
    return r;
  }

  @Override
  public Request put(String url) throws MalformedURLException, IOException {
    BasicRequest r = createRequest(url);
    r.cnx.setRequestMethod("PUT");
    r.cnx.setDoInput(true);
    r.cnx.setDoOutput(true);
    return r;
  }

  @Override
  public Request post(String url) throws MalformedURLException, IOException {
    BasicRequest r = createRequest(url);
    r.cnx.setRequestMethod("POST");
    r.cnx.setDoInput(true);
    r.cnx.setDoOutput(true);
    return r;
  }

  @Override
  public Request delete(String url) throws MalformedURLException, IOException {
    BasicRequest r = createRequest(url);
    r.cnx.setRequestMethod("DELETE");
    r.cnx.setDoInput(true);
    return r;
  }

}
