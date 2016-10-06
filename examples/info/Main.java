package info;

import java.util.List;
import java.util.Map;

import net.koofr.api.auth.Authenticator;
import net.koofr.api.auth.basic.HttpBasicAuthenticator;
import net.koofr.api.auth.oauth2.OAuth2Authenticator;
import net.koofr.api.http.Client;
import net.koofr.api.http.impl.basic.BasicClient;
import net.koofr.api.json.Transmogrifier;
import net.koofr.api.rest.v2.Api;
import net.koofr.api.rest.v2.data.ConnectionList;
import net.koofr.api.rest.v2.data.Group;
import net.koofr.api.rest.v2.data.Permissions;
import net.koofr.api.rest.v2.data.Self;
import net.koofr.api.rest.v2.data.User;

public class Main {

  private static void dumpPermissions(Permissions p, String pfx) {
    for(String k: p.keySet()) {
      System.out.println(pfx + k + ": " + p.get(k));
    }
  }
  
  private static void dumpUser(User u, String pfx) {
    System.out.println(pfx + "[" + u.id + "]: " + u.name + " (" + u.email + ")");
    if(null != u.permissions) {
      dumpPermissions(u.permissions, pfx + "  ");
    }
  }
  
  private static void dumpGroup(Group g, String pfx) {
    System.out.println(pfx + "[" + g.id + "]: " + g.name);
    if(null != g.permissions) {
      dumpPermissions(g.permissions, pfx + "  ");
    }
    if(g.users != null) {
      for(User u: g.users) {
        dumpUser(u, pfx + "  ");
      }
    }
  }
  
  private static void dumpList(List<Object> l, String pfx) {
    for(Object o: l) {
      if(o instanceof List) {
        dumpList((List)o, pfx + "  ");
      } else if(o instanceof Map) {
        dumpMap((Map)o, pfx + "  ");
      } else {
        System.out.println(pfx + o);
      }
    }
  }
  
  private static void dumpMap(Map<String, Object> m, String pfx) {
    for(String k: m.keySet()) {
      Object v = m.get(k);
      System.out.print(pfx + k + ": ");
      if(v instanceof Map) {
        System.out.println();
        dumpMap((Map<String, Object>)v, pfx + "  ");
      } else if(v instanceof List) {
        System.out.println();
        dumpList((List)v, pfx + "  ");
      } else {
        System.out.println(v);
      }
    }
  }
  
  public static void main(String[] args) throws Exception {
    Client c = new BasicClient();
    Authenticator a = null;
    if(args.length == 4) {
      a = new OAuth2Authenticator(c, args[0], args[1], args[2], "urn:ietf:wg:oauth:2.0:oob");
      ((OAuth2Authenticator)a).setGrantCode(args[3]);
      System.out.println("Refresh token: " + ((OAuth2Authenticator)a).getRefreshToken());
      System.out.println("Access token: " + ((OAuth2Authenticator)a).getAccessToken());
      System.out.println("Expiration: " + ((OAuth2Authenticator)a).getExpirationDate());
    } else if(args.length == 2) {
      a = new HttpBasicAuthenticator(args[0], args[1]);
    } else {
      System.err.println("Usage: info [<token-url> <client-id> <client-secret> <grant-code>|<username> <password>]");
      System.exit(42);
    }
    Api api = new Api("https://stage.koofr.net/api/v2", a, c);
    Self self = api.user().get();
    Transmogrifier.dumpObject(self); System.out.println();
    ConnectionList cl = api.user().connections().get();
    Transmogrifier.dumpObject(cl); System.out.println();
    String oldF = self.firstName;
    String oldL = self.lastName;
    api.user().edit("bu", "ba");
    self = api.user().get();
    Transmogrifier.dumpObject(self); System.out.println();
    api.user().edit(oldF, oldL);
    Map<String, Object> attributes = api.user().attributes().get();
    Transmogrifier.dumpObject(attributes); System.out.println();
    Map<String, Object> appConfig = api.user().appConfig().get();
    Transmogrifier.dumpObject(appConfig); System.out.println();
    Transmogrifier.dumpObject(api.user().settings().branding().get()); System.out.println();
    Transmogrifier.dumpObject(api.user().settings().notifications().get()); System.out.println();
    Transmogrifier.dumpObject(api.user().settings().security().get()); System.out.println();
    Transmogrifier.dumpObject(api.user().settings().seen().get()); System.out.println();
    Transmogrifier.dumpObject(api.user().settings().language().get()); System.out.println();
  }
}
