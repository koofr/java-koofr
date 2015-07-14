package net.koofr.api.v2.test;

import java.util.Arrays;
import java.util.Random;

import net.koofr.api.v2.StorageApi;
import net.koofr.api.v2.StorageApiException;
import net.koofr.api.v2.util.HttpsClientHelper;
import net.koofr.api.v2.util.Log;

import org.apache.commons.codec.binary.Base64;
import org.restlet.Client;
import org.restlet.Context;
import org.restlet.data.Protocol;
import org.restlet.engine.Engine;
import org.restlet.ext.jackson.JacksonConverter;

public class TestApi {
	Random rnd;
	
	private TestApi() {
		rnd = new Random();
	}

	private void testApi() throws StorageApiException {
		Engine.getInstance().getRegisteredConverters().clear();
		Engine.getInstance().getRegisteredConverters()
				.add(new JacksonConverter());
	
		Engine.getInstance().getRegisteredClients().clear();
		Engine.getInstance().getRegisteredClients().
				add(new HttpsClientHelper(null));
		
		Context restletContext = new Context();
		restletContext.getParameters().set("maxTotalConnections", "16");
		restletContext.getParameters().set("maxConnectionsPerHost", "8");
		Client client = new Client(restletContext, Arrays.asList(Protocol.HTTPS),
				HttpsClientHelper.class.getName());
		
		StorageApi api = new StorageApi("BASE URL", client);
		api.setLog(new Log() {			
			@Override
			public void debug(String tag, String msg, Throwable t) {
				System.err.println(tag + ": " + msg);
				t.printStackTrace(System.err);
			}
			
			@Override
			public void debug(String tag, String msg) {
				System.err.println(tag + ": " + msg);
			}
		});
    api.setClientCredentials("CLIENT ID", "CLIENT SECRET");
		
		try {
  		// api.setOAuthCode("AUTHORIZATION CODE", "REDIRECT URI");
  		api.setOAuthRefreshToken("REFRESH TOKEN");

      System.out.println("User info: " + api.getUserInfo());
      System.out.println("Mounts: " + api.getMounts());
		} catch(Exception e) {
		  System.err.println("Failed: " + e.getMessage());
		  e.printStackTrace(System.err);
		}
				
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) throws StorageApiException {
		(new TestApi()).testApi();
	}

}
