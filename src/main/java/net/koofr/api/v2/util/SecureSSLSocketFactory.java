package net.koofr.api.v2.util;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import org.apache.http.conn.ssl.SSLSocketFactory;

@SuppressWarnings("deprecated")
public class SecureSSLSocketFactory extends SSLSocketFactory {
	SSLContext sslContext = SSLContext.getInstance("TLS");

	public SecureSSLSocketFactory() throws NoSuchAlgorithmException,
		KeyManagementException, KeyStoreException,
		UnrecoverableKeyException {
		super((KeyStore)null);

		TrustManagerFactory f = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
		f.init((KeyStore)null);
		X509TrustManager utm = null;
		for (TrustManager tm: f.getTrustManagers()) {
			if (tm instanceof X509TrustManager) {
				utm = (X509TrustManager) tm;
				break;
			}
		}
		if(null == utm) {
			throw new KeyManagementException("Failed to get default trust manager.");
		}
		final X509TrustManager upstream = utm;
		TrustManager tm = new X509TrustManager() {
			public void checkClientTrusted(X509Certificate[] chain,
					String authType) throws CertificateException {
			}

			public void checkServerTrusted(X509Certificate[] chain,
					String authType) throws CertificateException {
				upstream.checkServerTrusted(chain, authType);
			}

			public X509Certificate[] getAcceptedIssuers() {
				return upstream.getAcceptedIssuers();
			}
		};

		sslContext.init(null, new TrustManager[] { tm }, null);
		setHostnameVerifier(SSLSocketFactory.STRICT_HOSTNAME_VERIFIER);
	}
	
	public SecureSSLSocketFactory(SSLContext context)
			throws KeyManagementException, NoSuchAlgorithmException,
			KeyStoreException, UnrecoverableKeyException {
		super((KeyStore)null);
		sslContext = context;
	}

	@Override
	public Socket createSocket(Socket socket, String host, int port,
			boolean autoClose) throws IOException, UnknownHostException {
		return sslContext.getSocketFactory().createSocket(socket, host, port,
				autoClose);
	}

	@Override
	public Socket createSocket() throws IOException {
		return sslContext.getSocketFactory().createSocket();
	}
}
