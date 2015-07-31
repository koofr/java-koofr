package net.koofr.api.v2.resources;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonBase {
  private static ObjectMapper mapper = new ObjectMapper();
  
  public JsonBase() {
  }

  @Override
  public String toString() {
    try {
      return mapper.writeValueAsString(this);
    }
    catch(Exception ex) {
      return super.toString();
    }
  }
  
}
