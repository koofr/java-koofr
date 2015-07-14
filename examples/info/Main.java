package info;

import net.koofr.api.v2.StorageApi;
import net.koofr.api.v2.DefaultClientFactory;
import net.koofr.api.v2.StorageApiException;

public class Main {
  public static void main(String[] args) throws StorageApiException {
    StorageApi api = null;
    if(args.length == 2) { // using code
      String host = args[0];
      String code = args[1];
      api = DefaultClientFactory.create(host);
      api.setOAuthCode(code);
    } else if (args.length == 3) { // using access/refresh token
      String host = args[0];
      String access = args[1];
      String refresh = args[2];
      api = DefaultClientFactory.create(host);
      api.setOAuthRefreshToken(refresh);
    } else {
      System.exit(1);
    }

    System.out.println(api.getUserInfo());
  }
}
