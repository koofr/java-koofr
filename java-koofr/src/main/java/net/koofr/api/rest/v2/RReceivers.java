package net.koofr.api.rest.v2;

import java.io.IOException;

import net.koofr.api.json.JsonBase;
import net.koofr.api.json.JsonException;
import net.koofr.api.rest.v2.data.Receivers.Receiver;
import net.koofr.api.rest.v2.data.Receivers;

public class RReceivers extends RGenericLinks<Receivers, Receivers.Receiver> {
  
  public RReceivers(RMounts.RMount parent) {
    super(parent, Receivers.class, Receivers.Receiver.class, "/receivers");
  }

  public static class RReceiver extends RGenericLink<Receivers.Receiver> {
    public RReceiver(RReceivers parent, String id) {
      super(parent, id);
    }

    public static class ReceiverAlert implements JsonBase {
      public Boolean alert;
    }

    private static class RReceiverAlert extends Resource {
      public RReceiverAlert(RReceiver parent) {
        super(parent, "/alert");
      }

      public Receiver set(boolean alert) throws IOException, JsonException {
        ReceiverAlert ra = new ReceiverAlert();
        ra.alert = alert;

        return putJsonResult(ra, Receiver.class);
      }
    }

    public Receiver setAlert(boolean alert) throws IOException, JsonException {
      return new RReceiverAlert(this).set(alert);
    }

  }

  public RReceiver receiver(String id) {
    return new RReceiver(this, id);
  }
}
