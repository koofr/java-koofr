package net.koofr.api.http.content;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class UrlEncodedBody extends ByteArrayBody {

  public UrlEncodedBody(String... kvs) throws UnsupportedEncodingException {
    if(kvs.length % 2 != 0) {
      throw new IllegalArgumentException("You need to supply an even number of parameters, duh!");
    }
    
    StringBuilder b = new StringBuilder();
    for(int i = 0; i < kvs.length; i += 2) {
      b.append(kvs[i]).append("=").append(URLEncoder.encode(kvs[i + 1], "UTF-8"));
      if(i < kvs.length - 2) {
        b.append("&");
      }
    }

    content = b.toString().getBytes("UTF-8");
    contentType = "application/x-www-form-urlencoded";
  }
  
}
