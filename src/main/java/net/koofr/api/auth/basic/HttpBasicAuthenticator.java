package net.koofr.api.auth.basic;

import net.koofr.api.auth.Authenticator;
import net.koofr.api.http.Request;
import net.koofr.api.util.Base64;

public class HttpBasicAuthenticator implements Authenticator {

  String encoded;

  public HttpBasicAuthenticator(String username, String password) {
    this.encoded = Base64.encode((username + ":" + password).getBytes());
  }
  
  @Override
  public void arm(Request request) {
    request.addHeader("Authorization", "Basic " + encoded);
  }
  
}
