package net.koofr.api.v2.util;

import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.restlet.Client;
import org.restlet.ext.httpclient.HttpClientHelper;

@SuppressWarnings("deprecation")
public class HttpsClientHelper extends HttpClientHelper {

	public HttpsClientHelper(Client client) {
		super(client);
	}

	protected void configure(SchemeRegistry schemeRegistry) {
		schemeRegistry.register(new Scheme("https", Https.getFactory(), 443));
		schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
	}
}
