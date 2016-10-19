package net.koofr.api.auth.koofr;

import net.koofr.api.auth.Authenticator;
import net.koofr.api.http.Request;

public class KoofrTokenAuthenticator implements Authenticator {
  
  String token;
  
  public KoofrTokenAuthenticator(String token) {
    this.token = token;
  }

  @Override
  public void arm(Request request) {
    request.addHeader("Authorization", "Token token=\"" + token + "\"");
  }
  
}
