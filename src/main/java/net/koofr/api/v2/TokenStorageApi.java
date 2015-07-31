package net.koofr.api.v2;

import net.koofr.api.v2.util.TokenClientResource;
import net.koofr.api.v2.util.KoofrTokenAuthenticator;

import org.apache.http.message.AbstractHttpMessage;
import org.restlet.Client;
import org.restlet.data.ChallengeResponse;
import org.restlet.data.Header;
import org.restlet.data.Reference;
import org.restlet.engine.header.HeaderConstants;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;
import org.restlet.util.Series;

public class TokenStorageApi extends StorageApi {

  public static final String HEADER_KOOFR_USERNAME = "X-Koofr-Email";
  public static final String HEADER_KOOFR_PASSWORD = "X-Koofr-Password";
  public static final String HEADER_KOOFR_TOKEN    = "X-Koofr-Token";
  
  private String token;

  public TokenStorageApi(String baseUrl, Client client) {
    super(baseUrl, client);
  }

  @Override
  protected ClientResource createResource(Reference ref) {
    ClientResource res = new TokenClientResource(ref);   
    setAuthenticationHeader(res);
    return res;
  }
  
  @Override
  protected void prepareHttpMessage(AbstractHttpMessage m) {
    if(token != null) {
      m.addHeader("Authorization", "Token token=\"" + token + "\"");
    }
  }
  
  @Override
  protected void prepareRequest() {
  }

  public void setAuthToken(String token) {
    this.token = token;
  }

  public String getAuthToken() {
    return this.token;
  }

  @SuppressWarnings("unchecked")
  public String authenticate(String tokenUrl, String username, String password)
      throws StorageApiException {
    Reference ref = new Reference(tokenUrl);
    ClientResource res = new TokenClientResource(ref);
    Series<Header> headers = (Series<Header>)res.getRequestAttributes().
        get(HeaderConstants.ATTRIBUTE_HEADERS);
    if(headers == null) {
      headers = new Series<Header>(Header.class);
      res.getRequestAttributes().put(HeaderConstants.ATTRIBUTE_HEADERS,
          headers);
    }
    headers.set(HEADER_KOOFR_PASSWORD, password);
    headers.set(HEADER_KOOFR_USERNAME, username);
    res.setNext(client);
    try {
      res.get();
      headers = (Series<Header>)res.getResponseAttributes().
          get(HeaderConstants.ATTRIBUTE_HEADERS);
      String rv = headers.getValues(HEADER_KOOFR_TOKEN);
      return rv;
    }
    catch(ResourceException ex) {
      fireExceptionHandler(ex);
      throw new StorageApiException(ex);
    }
  }

  protected void setAuthenticationHeader(ClientResource res) {
    res.setChallengeResponse(new ChallengeResponse(KoofrTokenAuthenticator.KOOFR_CHALLENGE_SCHEME));
  }
  
}
