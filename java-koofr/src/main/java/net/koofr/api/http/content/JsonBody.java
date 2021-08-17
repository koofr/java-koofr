package net.koofr.api.http.content;

import java.io.UnsupportedEncodingException;

import com.eclipsesource.json.JsonValue;

public class JsonBody extends StringBody {
  
  public JsonBody(String json) throws UnsupportedEncodingException {
    super(json, "application/json; charset=utf-8");
  }
  
  public JsonBody(JsonValue json) throws UnsupportedEncodingException {
    this(json.toString());
  }

}
