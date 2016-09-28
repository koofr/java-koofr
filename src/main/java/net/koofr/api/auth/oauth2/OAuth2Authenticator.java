package net.koofr.api.auth.oauth2;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;

import net.koofr.api.http.Client;
import net.koofr.api.http.HttpException;
import net.koofr.api.http.Request;
import net.koofr.api.http.Response;
import net.koofr.api.http.content.UrlEncodedBody;

public class OAuth2Authenticator {

  Client httpClient;
  String tokenUrl, clientId, clientSecret, redirectUri;
  
  String refreshToken;
  String accessToken;
  Date accessExpiration;
  
  public OAuth2Authenticator(Client httpClient, String tokenUrl,
      String clientId, String clientSecret, String redirectUri) {
    this.httpClient = httpClient;
    this.tokenUrl = tokenUrl;
    this.clientId = clientId;
    this.clientSecret = clientSecret;
    this.redirectUri = redirectUri;
  }
  
  private void parseTokenResponse(Response rs) throws IOException {
    if(rs.getStatus() != 200) {
      throw new HttpException(rs, "Failed to fetch token with authorization code.");
    }
    JsonObject o = Json.parse(new InputStreamReader(rs.getInputStream(), "UTF-8")).asObject();
    try {
      String r = o.getString("refresh_token", null);
      String a = o.getString("access_token", null);
      long x = o.getLong("expires_in", 0);
      if(r != null && a != null && x > 0) {
        refreshToken = r;
        accessToken = a;
        accessExpiration = new Date(new Date().getTime() + x);
      }      
    } catch(Exception ex) {
      throw new HttpException("Bad token response: " + o.toString(), ex);
    }
    /*
{
  "access_token": "2ZCAMGFTWKSYMXSGN5OKBPX6LTU7O5UJ24KYS4NPW4TH4NVBMPWHY6CX3DRDLWXP", 
  "scope": "private", 
  "token_type": "Bearer", 
  "expires_in": 3600, 
  "refresh_token": "MN5M6RRGMBYTLOSEYWPH25XNUK3BAFA2ZPWZT6RR7I37RQTNGN4KEAPLRXN6P7XZ"
}
    */    
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
/*
client_secret=JKOD6QDN46QZB7EIQIBUZVAF2DS2R6SCSWK5DGF2NOLQ3FQEMWOVWMOR2H2DWLYK&
grant_type=refresh_token&
refresh_token=MN5M6RRGMBYTLOSEYWPH25XNUK3BAFA2ZPWZT6RR7I37RQTNGN4KEAPLRXN6P7XZ&
client_id=HWGJ3SQXFGTCBAMOTTMPOAMR5F52YZ2Z
 */
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
    if(refreshToken != null) {
      throw new IOException("Missing refresh token.");
    }
    if(accessToken == null || accessExpiration == null ||
        new Date().before(accessExpiration)) {
      setRefreshToken(refreshToken);
    }
    request.addHeader("Authorization", "Bearer " + accessToken);
  }

}
