package net.koofr.api.v2;

import net.koofr.api.v2.util.HttpsClientHelper;

import org.restlet.Client;
import org.restlet.Context;
import org.restlet.data.Protocol;
import org.restlet.engine.Engine;
import org.restlet.ext.jackson.JacksonConverter;

/**
 * Produces new API clients with minimal configuration and sane defaults.
 */
public class DefaultClientFactory {

    public static StorageApi create(String hostname) {
        Engine.getInstance().getRegisteredConverters().clear();
        Engine.getInstance().getRegisteredConverters().add(new JacksonConverter());

        Engine.getInstance().getRegisteredClients().clear();
        Engine.getInstance().getRegisteredClients().add(new HttpsClientHelper(null));
        
        Context restletContext = new Context();
        restletContext.getParameters().set("maxTotalConnections", "16");
        restletContext.getParameters().set("maxConnectionsPerHost", "8");        
        Client client = new Client(restletContext, java.util.Arrays.asList(Protocol.HTTPS), HttpsClientHelper.class.getName());

        return new StorageApi("https://" + hostname, client);
    }

}
