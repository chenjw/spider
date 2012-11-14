package com.chenjw.client.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.KeyStore;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.conn.SchemeRegistryFactory;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.chenjw.client.HttpClient;
import com.chenjw.client.exception.ErrorCodeEnum;
import com.chenjw.client.exception.HttpClientException;

public class SimpleHttpClient implements HttpClient {
	private int connectionTimeout = 3000;
	private int timeoutInMilliseconds = 5000;
	private int httpConnectionTimeout = 100000;
	private int socketTimeout = 100000;

	private int maxTotalConnections = 100;
	private ThreadSafeClientConnManager connManager = null;
	private HttpRequestRetryHandler httpRequestRetryHandler = new DefaultHttpRequestRetryHandler(
			3, true);

	private HttpParams httpParams;

	public void init() {
		httpParams = new BasicHttpParams();
		/* 连接超时 */
		HttpConnectionParams.setConnectionTimeout(httpParams,
				httpConnectionTimeout);
		/* 请求超时 */
		HttpConnectionParams.setSoTimeout(httpParams, socketTimeout);

		SchemeRegistry schemeRegistry = SchemeRegistryFactory.createDefault();
		SSLSocketFactory socketFactory = null;
		try {
			socketFactory = new SSLSocketFactory(createSSLContext());
		} catch (Exception e) {
			e.printStackTrace();
		}
		Scheme sch = new Scheme("https", 443, socketFactory);
		schemeRegistry.register(sch);
		connManager = new ThreadSafeClientConnManager(schemeRegistry);
		connManager.setMaxTotal(maxTotalConnections);
		connManager.setDefaultMaxPerRoute(40);

	}

	private String prepareUrl(String url, Map<String, String> params,
			String encoding) {
		StringBuffer sb = new StringBuffer(url);
		if (params != null) {
			boolean isFirst = true;
			for (Entry<String, String> entry : params.entrySet()) {
				if (!StringUtils.isBlank(entry.getValue())) {
					if (isFirst) {
						sb.append('?');
						isFirst = false;
					} else {
						sb.append('&');
					}
					try {
						sb.append(entry.getKey())
								.append('=')
								.append(URLEncoder.encode(entry.getValue(),
										encoding));
					} catch (UnsupportedEncodingException e) {
						//
					}
				}
			}
		}
		return sb.toString();
	}

	@Override
	public String get(String url, Map<String, String> params, String encoding,
			String cookie) throws HttpClientException {
		String realUrl = prepareUrl(url, params, encoding);
		// System.out.println("url=" + realUrl);
		HttpGet method = new HttpGet(realUrl);
		if (cookie != null) {
			method.addHeader("Cookie", cookie);
		}

		method.setParams(httpParams);
		String result = excuteMethod(method, encoding);
		// System.out.println(result);
		return result;
	}

	@Override
	public String post(String url, Map<String, String> params, String encoding,
			String cookie) throws HttpClientException {
		String realUrl = url;
		// System.out.println("url=" + realUrl);
		HttpPost method = new HttpPost(realUrl);
		List<NameValuePair> postParams = new ArrayList<NameValuePair>();
		for (Entry<String, String> entry : params.entrySet()) {
			postParams.add(new BasicNameValuePair(entry.getKey(), entry
					.getValue()));
		}
		try {
			method.setEntity(new UrlEncodedFormEntity(postParams, HTTP.UTF_8));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		// String cookie =
		// "240=240; CurLoginUserGUID=a44e6f49751b4d8b9659636ae20540d6; strCertificate=410df02a24354be8bb194aeec0829589;";
		if (cookie != null) {
			method.addHeader("Cookie", cookie);
		}
		method.setParams(httpParams);
		String result = excuteMethod(method, encoding);
		// System.out.println(result);
		return result;
	}

	/**
	 * 执行http方法
	 * 
	 * @param httpMethod
	 * @return
	 */
	private String excuteMethod(HttpUriRequest httpMethod, String encoding)
			throws HttpClientException {
		HttpResponse response = null;
		org.apache.http.client.HttpClient client = this.createHttpClient();
		try {
			response = client.execute(httpMethod);
			// for (Header h : response.getAllHeaders()) {
			// System.out.println(h.getName() + " = " + h.getValue());
			// }
		} catch (ClientProtocolException e) {
			throw new HttpClientException(ErrorCodeEnum.SYSTEM_ERROR,
					"ClientProtocolException", e);
		} catch (IOException e) {
			throw new HttpClientException(ErrorCodeEnum.SYSTEM_ERROR,
					"IOException", e);
		}

		String result = handleResult(response, httpMethod, encoding);
		return result;
	}

	/**
	 * 处理http请求的返回结果
	 * 
	 * @param responseCode
	 * @param method
	 * @return
	 * @throws IOException
	 */
	private String handleResult(HttpResponse response,
			HttpUriRequest httpMethod, String encoding)
			throws HttpClientException {
		int responseCode = response.getStatusLine().getStatusCode();
		if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {

			HttpEntity entity = response.getEntity();

			if (entity != null) {
				String result = null;
				// try {
				// entity.getContent();
				// } catch (IllegalStateException e) {
				// // TODO Auto-generated catch block
				// e.printStackTrace();
				// } catch (IOException e) {
				// // TODO Auto-generated catch block
				// e.printStackTrace();
				// }
				try {
					result = EntityUtils.toString(entity, encoding);
				} catch (ParseException e) {
					throw new HttpClientException(ErrorCodeEnum.SYSTEM_ERROR,
							"ParseException", e);
				} catch (IOException e) {
					throw new HttpClientException(ErrorCodeEnum.SYSTEM_ERROR,
							"IOException", e);
				}
				return result;
			}
		}
		throw new HttpClientException(ErrorCodeEnum.NOT_FOUND_ERROR,
				"responseCode : " + responseCode);

	}

	private org.apache.http.client.HttpClient createHttpClient() {
		DefaultHttpClient httpClient = new DefaultHttpClient(connManager);
		httpClient.setHttpRequestRetryHandler(httpRequestRetryHandler);
		// System.out.println("[pool]" + connManager.getConnectionsInPool());
		// httpClient = WebClientDevWrapper.wrapClient(httpClient);
		return httpClient;
	}

	private SSLContext createSSLContext() {
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

	private KeyManager[] getKeyManagers() {
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

	private TrustManager[] getTrustManagers() {
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

	public static void main(String[] args) throws HttpClientException {
		SimpleHttpClient c = new SimpleHttpClient();
		c.init();
		String cookie = "240=240; CurLoginUserGUID=a44e6f49751b4d8b9659636ae20540d6; strCertificate=410df02a24354be8bb194aeec0829589;";
		// String s = c
		// .get("http://res.gsjy.net/PLWeb/protect.aspx?AuthorizedInfo=%3cUserInfo%3e%3cCertificate%3e410df02a24354be8bb194aeec0829589%3c%2fCertificate%3e%3cUserId%3eFaa3rxGTExepUXV%2b2x3qLA%3d%3d%3c%2fUserId%3e%3cExpiredDate%3eqgTJ2eNxOozYDJJ6WlK7pee%2fNtTHmkegIfDookIcqQM%3d%3c%2fExpiredDate%3e%3cAuthorizedDate%3e2%2bkqOdYv2461V2Y6IEgpLICxMaD%2bv766ByLgMZGJiwY%3d%3c%2fAuthorizedDate%3e%3c%2fUserInfo%3e",
		// new HashMap(), "UTF-8");
		// System.out.println(s);
		for (int i = 0; i < 111; i++) {
			// c.get("http://res.gsjy.net/PLWeb/GroupManage.aspx?GroupInfoID=240",
			// new HashMap(), "UTF-8");
			c.get("http://res.gsjy.net/PLWeb/Common/DownAttach.aspx?ResID=68004ee21f4c489d9c0c14da482d4ed4&flag=Yes&GID=240",
					new HashMap(), "UTF-8", cookie);

		}

		// System.out.println(s);
	}
}
