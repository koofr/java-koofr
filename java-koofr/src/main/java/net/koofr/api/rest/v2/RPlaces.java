package net.koofr.api.rest.v2;

import net.koofr.api.json.JsonException;
import net.koofr.api.rest.v2.data.Places;

import java.io.IOException;

public class RPlaces extends Resource {

  public RPlaces(Api parent) {
    super(parent, "/places");
  }
  
  public Places get() throws JsonException, IOException {
    return getResult(Places.class);
  }
    
}
