package info;

import net.koofr.api.v2.StorageApi;
import net.koofr.api.v2.DefaultClientFactory;
import net.koofr.api.v2.StorageApiException;

public class Main {
    public static void main(String[] args) throws StorageApiException {
        String host = args[0];
        String username = args[1];
        String password = args[2];
        StorageApi api = DefaultClientFactory.create(host, username, password);

        System.out.println(api.getUserInfo());
    }
}