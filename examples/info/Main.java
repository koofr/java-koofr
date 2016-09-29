package info;

import net.koofr.api.auth.oauth2.OAuth2Authenticator;
import net.koofr.api.http.Client;
import net.koofr.api.http.impl.basic.BasicClient;
import net.koofr.api.rest.v2.Api;

public class Main {
  public static void main(String[] args) throws Exception {
    if(args.length != 4) {
      System.err.println("Usage: info <token-url> <client-id> <client-secret> <grant-code>");
    }
    Client c = new BasicClient();
    OAuth2Authenticator a = new OAuth2Authenticator(c, args[0], args[1], args[2], "urn:ietf:wg:oauth:2.0:oob");
    a.setGrantCode(args[3]);
    System.out.println("Refresh token: " + a.getRefreshToken());
    System.out.println("Access token: " + a.getAccessToken());
    System.out.println("Expiration: " + a.getExpirationDate());
    Api api = new Api("https://stage.koofr.net/api/v2", a, c);
    api.user().info();
  }
}


