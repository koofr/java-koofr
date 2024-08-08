package net.koofr.api.rest.v2.data;

import net.koofr.api.json.JsonBase;
import net.koofr.api.util.U;

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

    @Override
    public boolean equals(Object obj) {
      if(obj == null || !(obj instanceof Device)) {
        return false;
      }
      Device o = (Device)obj;
      return U.safeEq(id, o.id) &&
        U.safeEq(apiKey, o.apiKey) &&
        U.safeEq(name, o.name) &&
        U.safeEq(status, o.status) &&
        U.safeEq(spaceTotal, o.spaceTotal) &&
        U.safeEq(spaceUsed, o.spaceUsed) &&
        U.safeEq(spaceFree, o.spaceFree) &&
        U.safeEq(version, o.version) &&
        U.safeEq(provider, o.provider) &&
        U.safeEq(canEdit, o.canEdit) &&
        U.safeEq(canRemove, o.canRemove) &&
        U.safeEq(searchEnabled, o.searchEnabled) &&
        U.safeEq(rootMountId, o.rootMountId);
    }  
  }

  public static class DeviceProvider implements JsonBase, Serializable {
    private static final long serialVersionUID = 1L;

    public String name;
    public Map<String, String> data;

    @Override
    public boolean equals(Object obj) {
      if(obj == null || !(obj instanceof DeviceProvider)) {
        return false;
      }
      DeviceProvider o = (DeviceProvider)obj;
      return U.safeEq(name, o.name) &&
        U.safeEq(data, o.data);
    }  
  }
  
  public static class DeviceInfo implements JsonBase, Serializable {
    private static final long serialVersionUID = 1L;

    public String id;
    public String name;
    public Integer version;

    @Override
    public boolean equals(Object obj) {
      if(obj == null || !(obj instanceof DeviceInfo)) {
        return false;
      }
      DeviceInfo o = (DeviceInfo)obj;
      return U.safeEq(id, o.id) &&
        U.safeEq(name, o.name) &&
        U.safeEq(version, o.version);
    }  
  }

  @Override
  public boolean equals(Object obj) {
    if(obj == null || !(obj instanceof Devices)) {
      return false;
    }
    Devices o = (Devices)obj;
    return U.safeEq(devices, o.devices);
  }
    
}
