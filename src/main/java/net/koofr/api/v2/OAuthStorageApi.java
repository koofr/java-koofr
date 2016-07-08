package net.koofr.api.v2;

import java.util.Date;

import net.koofr.api.v2.util.OAuthClientResource;
import net.koofr.api.v2.util.TokenClientResource;

import org.apache.http.message.AbstractHttpMessage;
import org.json.JSONException;
import org.restlet.Client;
import org.restlet.data.Reference;
import org.restlet.ext.oauth.AccessTokenClientResource;
import org.restlet.ext.oauth.GrantType;
import org.restlet.ext.oauth.OAuthParameters;
import org.restlet.ext.oauth.internal.Token;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;

public class OAuthStorageApi extends StorageApi {

  private String clientId, clientSecret;
  private OAuthToken oauthToken;
  
  public OAuthStorageApi(String baseUrl, Client client) {
    super(baseUrl, client);
  }

  public void setClientCredentials(String id, String secret) {
    clientId = id;
    clientSecret = secret;
  }
  
  public class OAuthToken {
    public String refresh;
    public String access;
    public long expires;    
    
    protected Token token;
    
    public OAuthToken(String refresh) {
      this.refresh = refresh;
      this.access = null;
      this.expires = 0L;
      this.token = null;
    }
    
    protected OAuthToken(Token token) {
      this.token = token;
      refresh = token.getRefreshToken();
      access  = token.getAccessToken();
      expires = new Date().getTime() + token.getExpirePeriod()*1000;
    }
  }
  
  public String getOAuthAccessToken() {
    if(null != oauthToken) {
      return oauthToken.access;
    } else {
      return null;
    }
  }

  public String getOAuthRefreshToken() {
    if(null != oauthToken) {
      return oauthToken.refresh;
    } else {
      return null;
    }
  }
  
  @Override
  protected void prepareRequest() throws StorageApiException {
    renewIfNeccessary();
  }
  
  @Override
  protected void prepareHttpMessage(AbstractHttpMessage m) throws StorageApiException  {
    if(oauthToken != null) {
      m.addHeader("Authorization", "Bearer " + oauthToken.access);
    }
  }
  
  @Override
  protected ClientResource createResource(Reference ref) throws StorageApiException  {
    OAuthClientResource res = new OAuthClientResource(ref);
    if(oauthToken != null) {
      res.setToken(oauthToken.token);
    }
    return res;
  }
  
  private long EXPIRATION_THRESHOLD = 5*60*1000L;
  
  private boolean renewIfNeccessary() throws ResourceException {
    if(oauthToken == null || oauthToken.refresh == null) {
      throw new ResourceException(401);
    }
    if(oauthToken.access != null &&
       oauthToken.token != null &&
       oauthToken.expires - EXPIRATION_THRESHOLD > new Date().getTime()) {
      return false;
    }
    setOAuthRefreshToken(oauthToken.refresh);
    return true;
  }
  
  public void clearOAuthTokens() {
    oauthToken = null;
  }
  
  public OAuthToken setOAuthCode(String code, String redirectUri) throws ResourceException {
    Reference ref = new Reference(new Reference(baseUrl), "/oauth2/token");
    AccessTokenClientResource res = new AccessTokenClientResource(ref);
    OAuthParameters params = new OAuthParameters();
    params.code(code);
    params.grantType(GrantType.authorization_code);
    params.add(OAuthParameters.CLIENT_ID, clientId);
    params.add(OAuthParameters.CLIENT_SECRET, clientSecret);
    params.redirectURI(redirectUri);
    try {
      Token token = res.requestToken(params);   
      oauthToken = new OAuthToken(token);
      return oauthToken;
    } catch(Exception ex) {
      throw new ResourceException(ex);
    }
  }
  
  public OAuthToken setOAuthRefreshToken(String refresh) throws ResourceException {
    Reference ref = new Reference(new Reference(baseUrl), "/oauth2/token");
    AccessTokenClientResource res = new AccessTokenClientResource(ref);
    OAuthParameters params = new OAuthParameters();
    params.refreshToken(refresh);
    params.grantType(GrantType.refresh_token);
    params.add(OAuthParameters.CLIENT_ID, clientId);
    params.add(OAuthParameters.CLIENT_SECRET, clientSecret);
    try {
      Token token = res.requestToken(params);   
      oauthToken = new OAuthToken(token);
      return oauthToken;
    } catch(Exception ex) {
      throw new ResourceException(ex);
    }   
  }

}
