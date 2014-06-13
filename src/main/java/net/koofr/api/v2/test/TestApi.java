package net.koofr.api.v2.test;

import java.util.Arrays;
import java.util.Random;

import net.koofr.api.v2.KoofrAuthenticator;
import net.koofr.api.v2.StorageApi;
import net.koofr.api.v2.StorageApiException;
import net.koofr.api.v2.resources.NotificationSettings;
import net.koofr.api.v2.resources.UserInfo;
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

	private String randomString() {
		byte[] src = new byte[16];
		rnd.nextBytes(src);
		return Base64.encodeBase64String(src);
	}

	private void testApi() throws StorageApiException {
		KoofrAuthenticator authenticator = new KoofrAuthenticator();

		Engine.getInstance().getRegisteredConverters().clear();
		Engine.getInstance().getRegisteredConverters()
				.add(new JacksonConverter());
	
		Engine.getInstance().getRegisteredClients().clear();
		Engine.getInstance().getRegisteredClients().
				add(new HttpsClientHelper(null));
		
		Engine.getInstance().getRegisteredAuthenticators().clear();
		Engine.getInstance().getRegisteredAuthenticators().
				add(authenticator);
		
		Context restletContext = new Context();
		restletContext.getParameters().set("maxTotalConnections", "16");
		restletContext.getParameters().set("maxConnectionsPerHost", "8");
		Client client = new Client(restletContext, Arrays.asList(Protocol.HTTPS),
				HttpsClientHelper.class.getName());
		
		StorageApi api = new StorageApi("https://stage.koofr.net/", client);
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
		
		String token = api.authenticate("https://stage.koofr.net/token", "jaka@koofr.net", "jakaneka");
		authenticator.setToken(token);
		
		System.out.println("User info: " + api.getUserInfo());
		
		String s1 = randomString(), s2 = randomString();
		api.updateUserInfo("Jaka", "Močnik");
		UserInfo i = api.getUserInfo();
		assert(i.getFirstName().equals(s1));
		assert(i.getFirstName().equals(s2));		

		NotificationSettings ns = api.getNotificationSettings(), ns2;
		System.out.println("Notification settings: " + ns);
		ns.setDeviceOffline(!ns.isDeviceOffline());
		ns.setNewComment(!ns.isNewComment());
		ns.setShared(!ns.isShared());
		api.updateNotificationSettings(ns);
		ns2 = api.getNotificationSettings();
		System.out.println("Notification settings: " + ns2);
		assert(ns.isDeviceOffline() == ns2.isDeviceOffline());
		assert(ns.isShared() == ns2.isShared());
		assert(ns.isNewComment() == ns2.isNewComment());
		
		System.out.println("Mounts: " + api.getMounts());
		
		/* TODO: convert to junit, complete tests */
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) throws StorageApiException {
		(new TestApi()).testApi();
	}

}
