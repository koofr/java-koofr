package info;

import net.koofr.api.v2.StorageApi;
import net.koofr.api.v2.DefaultClientFactory;
import net.koofr.api.v2.StorageApiException;

public class Main {
  public static void main(String[] args) throws StorageApiException {
    StorageApi api = null;
    if(args.length == 2) { //using token
        String host = args[0];
        String token = args[1];
        api = DefaultClientFactory.createToken(host, token);
    } else if (args.length == 3) { // using username/pass
        String host = args[0];
        String username = args[1];
        String password = args[2];
        api = DefaultClientFactory.createToken(host, username, password);
    } else {
        System.out.println("Pass in hostname and auth token or username/pass");
        System.exit(1);
    }

    System.out.println(api.getUserInfo());
  }
}
