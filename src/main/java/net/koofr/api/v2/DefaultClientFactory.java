package net.koofr.api.v2;

import net.koofr.api.v2.util.HttpsClientHelper;
import net.koofr.api.v2.util.KoofrTokenAuthenticator;

import org.restlet.Client;
import org.restlet.Context;
import org.restlet.data.Protocol;
import org.restlet.engine.Engine;
import org.restlet.ext.jackson.JacksonConverter;

/**
 * Produces new API clients with minimal configuration and sane defaults.
 */
public class DefaultClientFactory {

  private static Client prepareClient() {
    Engine.getInstance().getRegisteredConverters().clear();
    Engine.getInstance().getRegisteredConverters().add(new JacksonConverter());

    Engine.getInstance().getRegisteredClients().clear();
    Engine.getInstance().getRegisteredClients().add(new HttpsClientHelper(null));
    
    Context restletContext = new Context();
    restletContext.getParameters().set("maxTotalConnections", "16");
    restletContext.getParameters().set("maxConnectionsPerHost", "8");        
    Client client = new Client(restletContext, java.util.Arrays.asList(Protocol.HTTPS),
        HttpsClientHelper.class.getName());

    return client;
  }
  
    public static OAuthStorageApi createOAuth(String hostname) {
      Client client = prepareClient();

      return new OAuthStorageApi("https://" + hostname, client);
    }

    private static TokenStorageApi createToken(String hostname, KoofrTokenAuthenticator authenticator) {
      Engine.getInstance().getRegisteredAuthenticators().clear();
      Engine.getInstance().getRegisteredAuthenticators().add(authenticator);      

      Client client = prepareClient();      
      
      return new TokenStorageApi("https://" + hostname, client);
    }
    
    /**
     * Produce a client and configure it using an existing authentication token.
     * This is the recommended way of creating clients.
     *
     * @param hostname Just the hostname without protocol or any slashes
     * @param authToken Existing authentication token
     * @return Authenticated client
     */
    public static StorageApi createToken(String hostname, String authToken) {
        KoofrTokenAuthenticator authenticator = new KoofrTokenAuthenticator();
        TokenStorageApi api = createToken(hostname, authenticator);
        authenticator.setToken(authToken);
        api.setAuthToken(authToken);
        return api;
    }

    /**
     * Produce a client, then do an authentication request to create a token and
     * then use this token to configure said client.
     * Note that this incurs additional overhead as it's performing authentication
     * and producing new tokens every time. It's recommended you save this token
     * for later use when creating new clients.
     *
     * @param hostname Just the hostname without protocol or any slashes
     * @param username Username of user logging in
     * @param password Password of user logging in
     * @return Authenticated client
     * @throws StorageApiException
     */
    public static StorageApi createToken(String hostname, String username, String password) throws StorageApiException {
        KoofrTokenAuthenticator authenticator = new KoofrTokenAuthenticator();
        TokenStorageApi api = createToken(hostname, authenticator);
        String token = api.authenticate("https://" + hostname + "/token", username, password);
        api.setAuthToken(token);
        authenticator.setToken(token);
        return api;
    }
    
    
}
