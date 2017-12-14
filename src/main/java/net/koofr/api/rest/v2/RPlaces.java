package net.koofr.api.rest.v2;

import java.io.IOException;

import net.koofr.api.json.JsonException;
import net.koofr.api.rest.v2.data.Places;

public class RPlaces extends Resource {

  public RPlaces(Api parent) {
    super(parent, "/places");
  }
  
  public Places get() throws JsonException, IOException {
    return getResult(Places.class);
  }
    
}
