package info;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.koofr.api.auth.Authenticator;
import net.koofr.api.auth.basic.HttpBasicAuthenticator;
import net.koofr.api.auth.oauth2.OAuth2Authenticator;
import net.koofr.api.http.Client;
import net.koofr.api.http.Request;
import net.koofr.api.http.Response;
import net.koofr.api.http.errors.HttpException;
import net.koofr.api.http.errors.HttpException.*;
import net.koofr.api.http.impl.basic.BasicClient;
import net.koofr.api.http.impl.basic.BasicRequest;
import net.koofr.api.json.JsonException;
import net.koofr.api.json.Transmogrifier;
import net.koofr.api.rest.v2.Api;
import net.koofr.api.rest.v2.RMounts.RMount;
import net.koofr.api.rest.v2.RSearch.QueryParameters;
import net.koofr.api.rest.v2.data.ConnectionList;
import net.koofr.api.rest.v2.data.Groups.*;
import net.koofr.api.rest.v2.data.Devices.*;
import net.koofr.api.rest.v2.data.Mounts.*;
import net.koofr.api.rest.v2.data.Files.*;
import net.koofr.api.rest.v2.data.Jobs.*;
import net.koofr.api.rest.v2.data.Mounts;
import net.koofr.api.rest.v2.data.Permissions;
import net.koofr.api.rest.v2.data.SearchResult;
import net.koofr.api.rest.v2.data.Trash;
import net.koofr.api.rest.v2.data.SearchResult.SearchHit;
import net.koofr.api.rest.v2.data.Self;
import net.koofr.api.rest.v2.data.User;
import net.koofr.api.rest.v2.data.Bookmarks.Bookmark;

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
    System.out.println("You are " + s.firstName + " " + s.lastName + " at " + s.email);
  }

}
