package net.koofr.api.v2.util;

import org.apache.http.conn.ssl.SSLSocketFactory;

public class Https {

	private static SSLSocketFactory factory;

	public synchronized static SSLSocketFactory getFactory() {
		if (factory == null) {
			try {
				factory = new IgnorantSSLSocketFactory();
			} catch (Exception e) { // Gotta Catch 'Em All
				e.printStackTrace();
			}
		}

		return factory;
	}

}
