package net.koofr.api.rest.v2.data;

import java.util.List;
import java.util.Map;

import net.koofr.api.json.JsonBase;

public class Devices implements JsonBase {
  
  public List<Device> devices;
  
  public static class Device implements JsonBase {
    public String id;
    public String apiKey;
    public String name;
    public String status;
    public Long spaceTotal, spaceUsed, spaceFree;
    public Integer version;
    public DeviceProvider provider;
    public Boolean canEdit, canRemove, searchEnabled;
    public String rootMountId;
  }

  public static class DeviceProvider implements JsonBase {
    public String name;
    public Map<String, String> data;
  }
  
  public static class DeviceInfo implements JsonBase {
    public String id;
    public String name;
    public Integer version;
  }
  
}
