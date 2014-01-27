package com.chenjw.client.impl;

import java.io.InputStream;
import java.security.KeyStore;
import java.security.cert.X509Certificate;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.io.IOUtils;

/**
 * Provide a custom socket factory that implements
 * org.apache.commons.httpclient.protocol.ProtocolSocketFactory interface. The
 * socket factory is responsible for opening a socket to the target server using
 * either the standard or a third party SSL library and performing any required
 * initialization such as performing the connection handshake. Generally the
 * initialization is performed automatically when the socket is created.
 * 
 * @author sinaWeibo
 */
public class MySSLSocketFactory {


	public static SSLContext createSSLContext() {
		SSLContext ctx = null;
		try {
			ctx = SSLContext.getInstance("TLS");
			ctx.init(getKeyManagers(), getTrustManagers(), null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ctx;
	}

	private  static KeyManager[] getKeyManagers() {
		InputStream in = null;
		KeyStore ks = null;
		try {
			in = SimpleHttpClient.class
					.getResourceAsStream("/junwen.chenjw.p12");
			ks = KeyStore.getInstance("pkcs12");
			ks.load(in, "123456".toCharArray());

			KeyManagerFactory kmfactory = KeyManagerFactory
					.getInstance(KeyManagerFactory.getDefaultAlgorithm());
			kmfactory.init(ks, "123456".toCharArray());
			KeyManager[] keyManagers = kmfactory.getKeyManagers();
			return keyManagers;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			IOUtils.closeQuietly(in);
		}
	}

	private static TrustManager[] getTrustManagers() {
		X509TrustManager tm = new X509TrustManager() {
			public void checkClientTrusted(X509Certificate[] xcs, String string) {
			}

			public void checkServerTrusted(X509Certificate[] xcs, String string) {
			}

			public X509Certificate[] getAcceptedIssuers() {
				return null;
			}

		};
		return new TrustManager[] { tm };
	}





}
