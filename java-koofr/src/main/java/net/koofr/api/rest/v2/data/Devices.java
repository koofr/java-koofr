package net.koofr.api.rest.v2.data;

import net.koofr.api.json.JsonBase;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class Devices implements JsonBase, Serializable {
  private static final long serialVersionUID = 1L;
  
  public List<Device> devices;
  
  public static class Device implements JsonBase, Serializable {
    private static final long serialVersionUID = 1L;

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

  public static class DeviceProvider implements JsonBase, Serializable {
    private static final long serialVersionUID = 1L;

    public String name;
    public Map<String, String> data;
  }
  
  public static class DeviceInfo implements JsonBase, Serializable {
    private static final long serialVersionUID = 1L;

    public String id;
    public String name;
    public Integer version;
  }
  
}
