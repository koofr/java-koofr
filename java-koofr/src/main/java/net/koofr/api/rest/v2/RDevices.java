package net.koofr.api.rest.v2;

import net.koofr.api.json.JsonBase;
import net.koofr.api.json.JsonException;
import net.koofr.api.rest.v2.data.Devices;
import net.koofr.api.rest.v2.data.Devices.Device;

import java.io.IOException;

public class RDevices extends Resource {
  
  public RDevices(Api parent) {
    super(parent, "/devices");
  }
  
  public Devices get() throws JsonException, IOException {
    return getResult(Devices.class);    
  }

  public static class DeviceCreate implements JsonBase {
    public String name;
    public String providerName;
  }
  
  public Device create(String name, String providerName) throws JsonException, IOException {
    DeviceCreate c = new DeviceCreate();
    c.name = name;
    c.providerName = providerName;
    return postJsonResult(c, Device.class);
  }
  
  public static class RDevice extends Resource {
    public RDevice(RDevices parent, String id) {
      super(parent, "/" + id);
    }
    
    public Device get() throws JsonException, IOException {
      return getResult(Device.class);
    }
    
    public static class DeviceEdit implements JsonBase {
      public String name;
    }
    
    public void edit(String newName) throws JsonException, IOException {
      DeviceEdit e = new DeviceEdit();
      e.name = newName;
      putJsonNoResult(e);
    }
    
    public void delete() throws JsonException, IOException {
      deleteNoResult();
    }
    
    private static class RDeviceSearch extends Resource {
      public RDeviceSearch(RDevice parent) {
        super(parent, "/search");
      }
    }
    
    public static class DeviceSearchEdit implements JsonBase {
      public Boolean enabled;
    }
    
    public void setSearchEnabled(boolean searchable) throws JsonException, IOException {
      DeviceSearchEdit e = new DeviceSearchEdit();
      e.enabled = searchable;
      new RDeviceSearch(this).putJsonNoResult(e);
    }    
  }
  
  public RDevice device(String id) {
    return new RDevice(this, id);
  }
    
}
