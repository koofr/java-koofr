package net.koofr.api.json;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.koofr.api.util.Log;
import net.koofr.api.util.StdLog;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonObject.Member;
import com.eclipsesource.json.JsonValue;

public class Transmogrifier {
  
  static Log log = new StdLog();
  
  protected static boolean jsonHasField(JsonValue o, String f) {
    if(o.isObject()) {
      for(String n: o.asObject().names()) {
        if(f.equals(n)) {
          return true;
        }
      }
    }
    return false;    
  }
  
  public static <T> T mapJsonResponse(JsonValue src, Class<T> c) throws JsonException {
    try {
      return mapJsonResponseUnsafe(src, c, null);
    } catch(JsonException ex) {
      throw ex;
    } catch(Exception ex) {
      throw new JsonException("Transmogrification bug. Check ur code.", ex);
    }
  }
  
  public static <T> T mapJsonResponse(InputStream is, Class<T> c) throws IOException, JsonException {
    JsonValue src = Json.parse(new InputStreamReader(is, "UTF-8"));
    return mapJsonResponse(src, c);
  }

  @SuppressWarnings({ "unchecked", "rawtypes" })
  public static <T> T mapJsonResponseUnsafe(JsonValue src, Class<T> c, Class pc)
      throws InvocationTargetException, NoSuchMethodException, IllegalAccessException, InstantiationException,
      JsonException {
    // log.debug("Value: " + src + "; type: " + c.getName());
    if(src.isNull()) {
      return null;
    } else if(c.equals(Integer.class) || c.equals(Integer.TYPE)) {
      if(!src.isNumber()) {
        throw new JsonException("Value not an integer.");
      }
      Constructor<T> cns = c.getConstructor(Integer.TYPE);
      return cns.newInstance(src.asInt());
    } else if(c.equals(Long.class) || c.equals(Long.TYPE)) {
      if(!src.isNumber()) {
        throw new JsonException("Value not a long.");
      }
      Constructor<T> cns = c.getConstructor(Long.TYPE);
      return cns.newInstance(src.asLong());
    } else if(c.equals(Double.class) || c.equals(Double.TYPE)) {
      if(!src.isNumber()) {
        throw new JsonException("Value not a double.");
      }
      Constructor<T> cns = c.getConstructor(Double.TYPE);
      return cns.newInstance(src.asDouble());
    } else if(c.equals(Boolean.class) || c.equals(Boolean.TYPE)) {
      if(!src.isBoolean()) {
        throw new JsonException("Value not a boolean.");
      }
      Constructor<T> cns = c.getConstructor(Boolean.TYPE);
      return cns.newInstance(src.asBoolean());
    } else if(c.equals(String.class)) {
      if(!src.isString()) {
        throw new JsonException("Value not a string.");
      }
      Constructor<T> cns = c.getConstructor(String.class);
      return cns.newInstance(src.asString());
    } else if(c.isArray()) { 
      if(!src.isArray()) {
        throw new JsonException("Value not an array.");
      }
      JsonArray arr = src.asArray();
      Class<?> styp = c.getComponentType();
      int size = arr.size();
      Object array = Array.newInstance(styp, size);
      for(int i = 0; i < size; i++) {
        Array.set(array, i, mapJsonResponseUnsafe(arr.get(i), styp, null));
      }
      return c.cast(array);
    } else if(List.class.isAssignableFrom(c)) {
      if(!src.isArray()) {
        throw new JsonException("Value not an array.");
      }
      if(pc == null) {
        throw new JsonException("Type parameter class not passed in.");
      }
      JsonArray arr = src.asArray();
      int size = arr.size();
      T rv;
      if(c.equals(List.class)) {
        rv = c.cast(new ArrayList<>());
      } else {
        Constructor<T> cns = c.getConstructor();
        rv = cns.newInstance();        
      }
      for(int i = 0; i < size; i++) {
        ((List)rv).add(mapJsonResponseUnsafe(arr.get(i), pc, null));
      }
      return rv;
    } else if(c.equals(Map.class)) {
      if(!src.isObject()) {
        throw new JsonException("Value not an object.");
      }
      if(pc == null) {
        throw new JsonException("Type parameter class not passed in.");
      }
      JsonObject obj = src.asObject();
      T rv;
      if(c.equals(Map.class)) {
        rv = c.cast(new HashMap<>());
      } else {
        Constructor<T> cns = c.getConstructor();
        rv = cns.newInstance();        
      }
      Iterator<Member> i = obj.iterator();
      while(i.hasNext()) {
        Member m = i.next();
        ((HashMap)rv).put(m.getName(), mapJsonResponseUnsafe(m.getValue(), pc, null));
      }
      return rv;
      
    } else if(JsonBase.class.isAssignableFrom(c)) {
      if(!src.isObject()) {
        throw new JsonException("Value not an object.");
      }
      JsonObject obj = src.asObject();
      Constructor<T> cns = c.getConstructor();
      T rv = cns.newInstance();
      Field[] fields = c.getFields();
      for(Field f: fields) {
        String name = f.getName();
        Class<?> typ = f.getType();
        if(!jsonHasField(obj, name)) {
          f.set(rv, null);
        } else {
          if(typ.equals(List.class)) {
            f.set(rv, mapJsonResponseUnsafe(obj.get(name), typ,
                (Class)((ParameterizedType)f.getGenericType()).getActualTypeArguments()[0]));
          } else if(typ.equals(Map.class)) {
            f.set(rv, mapJsonResponseUnsafe(obj.get(name), typ,
                (Class)((ParameterizedType)f.getGenericType()).getActualTypeArguments()[1]));
          } else {
            f.set(rv, mapJsonResponseUnsafe(obj.get(name), typ, null));
          }
        }
      }
      return rv;
    } else {
      throw new JsonException("Unsupported type: " + c.getSimpleName());
    }
  }

}
