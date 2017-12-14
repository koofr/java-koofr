package net.koofr.api.rest.v2;

import net.koofr.api.auth.Authenticator;
import net.koofr.api.http.Client;

public class Api extends Resource {
  
  public Api(String baseUrl, Authenticator auth, Client client) {
    super(auth, client, baseUrl);
  }
  
  public RSelf self() {
    return new RSelf(this);
  }
  
  public RGroups groups() {
    return new RGroups(this);
  }
  
  public RDevices devices() {
    return new RDevices(this);
  }
  
  public RMounts mounts() {
    return new RMounts(this);
  }
  
  public RShared shared() {
    return new RShared(this);
  }
  
  public RPlaces places() {
    return new RPlaces(this);
  }
  
  public RJobs jobs() {
    return new RJobs(this);
  }
  
  public RSearch search() {
    return new RSearch(this);
  }
  
  public RTrash trash() {
    return new RTrash(this);
  }

  public RUsers users() {
    return new RUsers(this);
  }
  
}
