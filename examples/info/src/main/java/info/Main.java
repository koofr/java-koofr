package info;

import net.koofr.api.auth.Authenticator;
import net.koofr.api.auth.basic.HttpBasicAuthenticator;
import net.koofr.api.auth.oauth2.OAuth2Authenticator;
import net.koofr.api.http.Client;
import net.koofr.api.http.impl.basic.BasicClient;
import net.koofr.api.http.impl.basic.BasicRequest;
import net.koofr.api.rest.v2.Api;
import net.koofr.api.rest.v2.data.Self;

public class Main {

  public static void main(String[] args) throws Exception {
    Client c = new BasicClient();
    c.setRequestDecorator(new Client.RequestDecorator<BasicRequest>() {
      @Override
      public BasicRequest decorate(BasicRequest r) {
        return r;
      }
    });
    Authenticator a = null;
    if(args.length == 5) {
      a = new OAuth2Authenticator(c, args[1], args[2], args[3], "urn:ietf:wg:oauth:2.0:oob");
      ((OAuth2Authenticator)a).setGrantCode(args[4]);
    } else if(args.length == 3) {
      a = new HttpBasicAuthenticator(args[1], args[2]);
    } else {
      System.err.println("Usage: info <server> [<token-url> <client-id> <client-secret> <grant-code>|<username> <password>]");
      System.exit(42);
    }
    Api api = new Api("https://" + args[0] + "/api/v2", a, c);
    Self s = api.self().get();
    System.out.println("You are " + s.firstName + " " + s.lastName + " at " + s.email + ", level " + s.level);
  }

}
