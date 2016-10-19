package net.koofr.api.json;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
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
  
  public static JsonValue mapObject(Object o) throws JsonException {
    try {
      return mapObjectUnsafe(o);
    } catch(Exception ex) {
      throw new JsonException("Transmogrification bug. Check ur code.", ex);
    }
  }
  

  @SuppressWarnings("rawtypes")
  public static void dumpObject(Object o) {
    if(o == null) {
      System.out.print("null");
    } else if((o instanceof Integer) || (o instanceof Long) || (o instanceof Double) || (o instanceof Boolean) ||
        (o instanceof String)) {
      System.out.print(o);
    } else if(o.getClass().isArray()) {
      System.out.print("[");
      int len = Array.getLength(o);
      for(int i = 0; i < len; i++) {
        dumpObject(o);
        System.out.print(",");
      }
      System.out.print("]");
    } else if(o instanceof JsonBase) {
      Field[] fields = o.getClass().getFields();
      System.out.print("{");
      for(Field f: fields) {
        if((f.getModifiers() & Modifier.PUBLIC) == 0 ||
            (f.getModifiers() & Modifier.STATIC) != 0 ||
            (f.getModifiers() & Modifier.TRANSIENT) != 0 ||
            (f.getModifiers() & Modifier.VOLATILE) != 0 ||
            (f.getModifiers() & Modifier.NATIVE) != 0) {
          continue;
        }
        System.out.print(f.getName() + ":");
        try {
          dumpObject(f.get(o));
        } catch(Exception ex) {
          System.out.print("INACCESSIBLE");
        }
        System.out.print(",");
      }
      System.out.print("}");
    } else if(List.class.isAssignableFrom(o.getClass())) {
      System.out.print("[");
      for(Object o1: (List)o) {
        dumpObject(o1);
        System.out.print(",");
      }
      System.out.print("]");
    } else if(Map.class.isAssignableFrom(o.getClass())) {
      System.out.print("{");
      Map m = Map.class.cast(o);
      for(Object k: m.keySet()) {
        System.out.print(k + ":");
        dumpObject(m.get(k));
        System.out.print(",");
      }
      System.out.print("}");
    } else {
      System.out.print(o);
    }    
  }
  
  @SuppressWarnings("rawtypes")
  protected static JsonValue mapObjectUnsafe(Object o) throws JsonException, IllegalAccessException {
    if(o == null) {
      return null;
    } else if(o instanceof Integer) {
      return Json.value((Integer)o);
    } else if(o instanceof Long) {
      return Json.value((Long)o);      
    } else if(o instanceof Double) {
      return Json.value((Double)o);
    } else if(o instanceof Boolean) {
      return Json.value((Boolean)o);
    } else if(o instanceof String) {
      return Json.value((String)o);
    } else if(o.getClass().isArray()) {
      JsonArray rv = new JsonArray();
      int len = Array.getLength(o);
      for(int i = 0; i < len; i++) {
        JsonValue v = mapObjectUnsafe(Array.get(o, i));
        if(v != null) {
          rv.add(v);
        }
      }
      return rv;
    } else if(o instanceof JsonBase) {
      JsonObject rv = new JsonObject();
      Field[] fields = o.getClass().getFields();
      for(Field f: fields) {
        if((f.getModifiers() & Modifier.PUBLIC) == 0 ||
            (f.getModifiers() & Modifier.STATIC) != 0 ||
            (f.getModifiers() & Modifier.TRANSIENT) != 0 ||
            (f.getModifiers() & Modifier.VOLATILE) != 0 ||
            (f.getModifiers() & Modifier.NATIVE) != 0) {
          continue;
        }
        JsonValue v = mapObjectUnsafe(f.get(o));
        if(v != null) {
          rv.add(f.getName(), v);          
        }
      }
      return rv;
    } else if(List.class.isAssignableFrom(o.getClass())) {
      JsonArray rv = new JsonArray();
      for(Object e: List.class.cast(o)) {
        JsonValue v = mapObjectUnsafe(e);
        if(v != null) {
          rv.add(v);
        }
      }
      return rv;
    } else if(Map.class.isAssignableFrom(o.getClass())) {
      JsonObject rv = new JsonObject();
      Map m = Map.class.cast(o);
      for(Object k: m.keySet()) {
        JsonValue v = mapObjectUnsafe(m.get(k));
        if(v != null) {
          rv.add((String)k, v);
        }
      }
      return rv;
    } else {
      throw new JsonException("Unsupported source type: " + o.getClass().getName());
    }
  }
  
  public static <T> T mappedJsonResponse(JsonValue src, Class<T> c) throws JsonException {
    try {
      return mappedJsonResponseUnsafe(src, c, null);
    } catch(JsonException ex) {
      throw ex;
    } catch(Exception ex) {
      throw new JsonException("Transmogrification bug. Check ur code.", ex);
    }
  }
  
  public static <T> T mappedJsonResponse(InputStream is, Class<T> c) throws IOException, JsonException {
    JsonValue src = Json.parse(new InputStreamReader(is, "UTF-8"));
    return mappedJsonResponse(src, c);
  }

  @SuppressWarnings("unchecked")
  public static Map<String, Object> genericJsonResponse(JsonObject v) throws JsonException {
    return (Map<String, Object>)genericJsonResponse((JsonValue)v);
  }

  @SuppressWarnings("unchecked")
  public static List<Object> genericJsonResponse(JsonArray v) throws JsonException {
    return (List<Object>)genericJsonResponse((JsonValue)v);
  }
  
  public static Object genericJsonResponse(JsonValue v) throws JsonException {
    if(v.isNumber()) {
      return v.asDouble();
    } else if(v.isString()) {
      return v.asString();        
    } else if(v.isArray()) {
      List<Object> rv = new ArrayList<Object>();
      JsonArray a = v.asArray();
      int len = a.size();
      for(int i = 0; i < len; i++) {
        rv.add(genericJsonResponse(a.get(i)));
      }
      return rv;
    } else if(v.isBoolean()) {
      return v.asBoolean();
    } else if(v.isObject()) {
      Iterator<Member> i = v.asObject().iterator();
      HashMap<String, Object> rv = new HashMap<String, Object>();
      while(i.hasNext()) {
        Member m = i.next();
        JsonValue sv = m.getValue();
        rv.put(m.getName(), genericJsonResponse(sv));
      }
      return rv;
    } else {
      throw new JsonException("Unsupported type: " + v + " (" + v.getClass().getName() + ")");
    }
  }
  
  @SuppressWarnings({ "unchecked", "rawtypes" })
  protected static <T> T mappedJsonResponseUnsafe(JsonValue src, Class<T> c, Class pc)
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
        Array.set(array, i, mappedJsonResponseUnsafe(arr.get(i), styp, null));
      }
      return c.cast(array);
    } else if(List.class.isAssignableFrom(c)) {
      if(!src.isArray()) {
        throw new JsonException("Value not an array.");
      }
      JsonArray arr = src.asArray();
      int size = arr.size();
      T rv;
      if(c.equals(List.class)) {
        rv = c.cast(new ArrayList<Object>());
      } else {
        Constructor<T> cns = c.getConstructor();
        rv = cns.newInstance();        
      }
      for(int i = 0; i < size; i++) {
        if(pc != null) {
          ((List)rv).add(mappedJsonResponseUnsafe(arr.get(i), pc, null));
        } else {
          ((List)rv).add(genericJsonResponse(arr.get(i)));
        }
      }
      return rv;
    } else if(Map.class.isAssignableFrom(c)) {
      if(!src.isObject()) {
        throw new JsonException("Value not an object.");
      }
      JsonObject obj = src.asObject();
      T rv;
      if(c.equals(Map.class)) {
        rv = c.cast(new HashMap<String, Object>());
      } else {
        Constructor<T> cns = c.getConstructor();
        rv = cns.newInstance();        
      }
      Iterator<Member> i = obj.iterator();
      while(i.hasNext()) {
        Member m = i.next();
        if(pc != null) {
          ((HashMap)rv).put(m.getName(), mappedJsonResponseUnsafe(m.getValue(), pc, null));
        } else {
          ((HashMap)rv).put(m.getName(), genericJsonResponse(m.getValue()));
        }
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
        if((f.getModifiers() & Modifier.PUBLIC) == 0 ||
            (f.getModifiers() & Modifier.STATIC) != 0 ||
            (f.getModifiers() & Modifier.TRANSIENT) != 0 ||
            (f.getModifiers() & Modifier.VOLATILE) != 0 ||
            (f.getModifiers() & Modifier.NATIVE) != 0) {
          continue;
        }
        String name = f.getName();
        Class<?> typ = f.getType();
        if(!jsonHasField(obj, name)) {
          f.set(rv, null);
        } else {
          if(typ.equals(List.class)) {            
            f.set(rv, mappedJsonResponseUnsafe(obj.get(name), typ,
                (Class)((ParameterizedType)f.getGenericType()).getActualTypeArguments()[0]));
          } else if(typ.equals(Map.class)) {
            f.set(rv, mappedJsonResponseUnsafe(obj.get(name), typ,
                (Class)(((ParameterizedType)f.getGenericType()).getActualTypeArguments()[1])));
          } else {
            f.set(rv, mappedJsonResponseUnsafe(obj.get(name), typ, null));
          }
        }
      }
      return rv;
    } else {
      throw new JsonException("Unsupported type: " + c.getSimpleName());
    }
  }
  
}
