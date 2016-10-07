package net.koofr.api.rest.v2;

import net.koofr.api.auth.Authenticator;
import net.koofr.api.http.Client;

public class Api extends Resource {
  
  public Api(String baseUrl, Authenticator auth, Client client) {
    super(auth, client);
    url = baseUrl; /* i.e. https://stage.koofr.net/api/v2 */
  }
  
  public RUser user() {
    return new RUser(this);
  }
  
  public RGroups groups() {
    return new RGroups(this);
  }
  
}
