package net.koofr.api.http;

import java.io.IOException;
import java.net.MalformedURLException;

public interface Client {

  Request get(String url) throws MalformedURLException, IOException;
  Request put(String url) throws MalformedURLException, IOException;
  Request post(String url) throws MalformedURLException, IOException;
  Request delete(String url) throws MalformedURLException, IOException;

}
