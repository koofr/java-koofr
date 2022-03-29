package net.koofr.api.http.content;

import com.eclipsesource.json.JsonValue;

import java.io.UnsupportedEncodingException;

public class JsonBody extends StringBody {
  
  public JsonBody(String json) throws UnsupportedEncodingException {
    super(json, "application/json; charset=utf-8");
  }
  
  public JsonBody(JsonValue json) throws UnsupportedEncodingException {
    this(json.toString());
  }

}
