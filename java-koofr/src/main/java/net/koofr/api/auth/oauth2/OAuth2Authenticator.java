package net.koofr.api.auth.oauth2;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;

import net.koofr.api.auth.Authenticator;
import net.koofr.api.http.Client;
import net.koofr.api.http.Request;
import net.koofr.api.http.Response;
import net.koofr.api.http.content.UrlEncodedBody;
import net.koofr.api.rest.v2.Resource;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;

public class OAuth2Authenticator implements Authenticator {

  Client httpClient;
  String tokenUrl, clientId, clientSecret, redirectUri;
  
  String refreshToken;
  String accessToken;
  Date accessExpiration;
  
  private static final int EXPIRATION_GUARD = 60*1000;
  
  public OAuth2Authenticator(Client httpClient, String tokenUrl,
      String clientId, String clientSecret, String redirectUri) {
    this.httpClient = httpClient;
    this.tokenUrl = tokenUrl;
    this.clientId = clientId;
    this.clientSecret = clientSecret;
    this.redirectUri = redirectUri;
  }
  
  private void parseTokenResponse(Response rs) throws IOException {
    Resource.checkResponse(rs);
    JsonObject o = Json.parse(new InputStreamReader(rs.getInputStream(), "UTF-8")).asObject();
    try {
      String r = o.getString("refresh_token", null);
      String a = o.getString("access_token", null);
      long x = o.getLong("expires_in", 0);
      if(r != null && a != null && x > 0) {
        refreshToken = r;
        accessToken = a;
        accessExpiration = new Date(new Date().getTime() + x*1000 - EXPIRATION_GUARD);
      }      
    } catch(Exception ex) {
      throw new IOException("Bad token response: " + o.toString(), ex);
    }
  }
  
  public void setGrantCode(String grantCode) throws IOException {
    Request rq = httpClient.post(tokenUrl);
    UrlEncodedBody b = new UrlEncodedBody(
        "client_id", clientId,
        "client_secret", clientSecret,
        "grant_type", "authorization_code",
        "code", grantCode,
        "redirect_uri", redirectUri        
    );
    Response rs = rq.execute(b);
    parseTokenResponse(rs);
  }
  
  public void setRefreshToken(String refreshToken) throws IOException {
    Request rq = httpClient.post(tokenUrl);
    UrlEncodedBody b = new UrlEncodedBody(
        "client_id", clientId,
        "client_secret", clientSecret,
        "grant_type", "refresh_token",
        "refresh_token", refreshToken      
    );
    Response rs = rq.execute(b);
    parseTokenResponse(rs);
  }

  public void arm(Request request) throws IOException {
    String t = getAccessToken();
    if(t != null) {
      request.addHeader("Authorization", "Bearer " + t);
    } else {
      /* well, we'll try without authentication then */
    }
  }

  public String getRefreshToken() {
    return refreshToken;
  }
  
  public String getAccessToken() throws IOException {
    if(refreshToken != null) {
      if(accessToken == null || accessExpiration == null ||
          new Date().after(accessExpiration)) {
        setRefreshToken(refreshToken);
      }
    }
    return accessToken;
  }
  
  public Date getExpirationDate() {
    return accessExpiration;
  }
}
