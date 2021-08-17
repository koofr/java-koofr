package net.koofr.api.http;

import java.io.IOException;
import java.net.MalformedURLException;

import net.koofr.api.http.impl.basic.BasicRequest;

public abstract class Client<R extends Request> {

  abstract protected R createRequest(String url, Request.Method method)
      throws MalformedURLException, IOException, IllegalArgumentException;

  public interface RequestDecorator<R extends Request> {
    R decorate(R r);
  }

  private RequestDecorator<R> decorator;

  public void setRequestDecorator(RequestDecorator<R> decorator) {
    this.decorator = decorator;
  }
  
  private R createAndDecorateRequest(String url, Request.Method method)
      throws MalformedURLException, IOException, IllegalArgumentException {
    R r = createRequest(url, method);
    if(decorator != null) {
      r = decorator.decorate(r);
    }
    return r;
  }
  
  public R get(String url) throws MalformedURLException, IOException {
    R r = createAndDecorateRequest(url, Request.Method.GET);
    return r;
  }

  public R put(String url) throws MalformedURLException, IOException {
    R r = createAndDecorateRequest(url, Request.Method.PUT);
    return r;
  }

  public R post(String url) throws MalformedURLException, IOException {
    R r = createAndDecorateRequest(url, Request.Method.POST);
    return r;
  }

  public Request delete(String url) throws MalformedURLException, IOException {
    R r = createAndDecorateRequest(url, Request.Method.DELETE);
    return r;
  }

}

