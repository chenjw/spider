package com.chenjw.client.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.activation.MimetypesFileTypeMap;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.MemoryCacheImageInputStream;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.ByteArrayPartSource;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.httpclient.params.HttpConnectionParams;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.httpclient.params.HttpParams;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.io.IOUtils;

import com.chenjw.client.HttpClient;
import com.chenjw.client.exception.ErrorCodeEnum;
import com.chenjw.client.exception.HttpClientException;
import com.chenjw.client.result.Result;
import com.chenjw.spider.location.HttpUrl;
import com.sun.imageio.plugins.bmp.BMPImageReader;
import com.sun.imageio.plugins.gif.GIFImageReader;
import com.sun.imageio.plugins.jpeg.JPEGImageReader;
import com.sun.imageio.plugins.png.PNGImageReader;

public class SimpleHttpClient implements HttpClient {
	private int connectionTimeout = 3000;
	private int timeoutInMilliseconds = 5000;
	private int httpConnectionTimeout = 100000;
	private int socketTimeout = 100000;

	private int maxTotalConnections = 100;
	private MultiThreadedHttpConnectionManager connManager = null;
	private HttpMethodRetryHandler httpRequestRetryHandler = new DefaultHttpMethodRetryHandler(
			3, true);

	private HttpParams httpParams;

	public void init() {
		HttpConnectionParams httpParams = new HttpConnectionParams();
		httpParams.setConnectionTimeout(httpConnectionTimeout);
		/* 连接超时 */
		httpParams.setSoTimeout(socketTimeout);

		Protocol myhttps = new Protocol("https", new MySSLSocketFactory(), 443);
		Protocol.registerProtocol("https", myhttps);
		connManager = new MultiThreadedHttpConnectionManager();
		connManager.setMaxTotalConnections(maxTotalConnections);
		connManager.setMaxConnectionsPerHost(40);

	}

	@Override
	public Result get(HttpUrl url, String encoding, String cookie,
			Map<String, String> headers) {
		String realUrl = url.toUrlString();
		GetMethod method = new GetMethod(realUrl);
		Result result = excuteMethod(method, cookie, headers);
		result.setResultEncoding(encoding);
		return result;
	}

	@Override
	public Result post(HttpUrl url, String encoding, String cookie,
			Map<String, String> headers) {
		String realUrl = url.getRenderedUrl();
		PostMethod method = new PostMethod(realUrl);
		if (url.getQueryParam() != null) {
			for (Entry<String, String> entry : url.getQueryParam().entrySet()) {
				method.addParameter(new NameValuePair(entry.getKey(), entry
						.getValue()));
			}
		}
		Result result = excuteMethod(method, cookie, headers);
		result.setResultEncoding(encoding);
		return result;
	}

	@Override
	public Result multPartPost(HttpUrl url, String encoding, String cookie,
			Map<String, String> headers, String fileParamName, String fileName,
			byte[] fileBytes) {
		String realUrl = url.getRenderedUrl();
		PostMethod method = new PostMethod(realUrl);
		List<Part> partList = new ArrayList<Part>();

		if (url.getQueryParam() != null) {
			for (Entry<String, String> entry : url.getQueryParam().entrySet()) {
				partList.add(new StringPart(entry.getKey(), entry.getValue()));
			}
		}
		if (fileBytes != null) {
			partList.add(new FilePart(fileParamName, new ByteArrayPartSource(
					fileName, fileBytes), getContentType(fileName, fileBytes), "UTF-8"));
		}
		method.setRequestEntity(new MultipartRequestEntity(partList
				.toArray(new Part[partList.size()]), method.getParams()));
		Result result = excuteMethod(method, cookie, headers);
		result.setResultEncoding(encoding);
		return result;
	}

	private Result excuteMethod(HttpMethod method, String cookie,
			Map<String, String> headers) {
		Result result = new Result();
		result.setSuccess(true);
		int responseCode = -1;
		try {
			// String cookie =
			// "240=240; CurLoginUserGUID=a44e6f49751b4d8b9659636ae20540d6; strCertificate=410df02a24354be8bb194aeec0829589;";
			if (cookie != null) {
				method.addRequestHeader("Cookie", cookie);
			}
			if (headers != null) {
				for (Entry<String, String> entry : headers.entrySet()) {
					method.addRequestHeader(entry.getKey(), entry.getValue());
				}
			}
			method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
					httpRequestRetryHandler);
			org.apache.commons.httpclient.HttpClient client = this
					.createHttpClient();
			client.executeMethod(method);
			responseCode = method.getStatusCode();
			if (responseCode != HttpStatus.SC_OK) {
				result.setSuccess(false);
				result.setErrorCode(ErrorCodeEnum.NOT_FOUND_ERROR);
				result.setMessage("responseCode:" + responseCode);
				result.setResponseCode(responseCode);
			}
			InputStream is = method.getResponseBodyAsStream();
			try {
				result.setResultBytes(IOUtils.toByteArray(is));
			} finally {
				IOUtils.closeQuietly(is);
			}

		} catch (Exception e) {
			result.setSuccess(false);
			result.setErrorCode(ErrorCodeEnum.SYSTEM_ERROR);
			result.setMessage(e.getMessage());
			result.setCause(e);
		} finally {
			result.setMethodStatusCode(method.getStatusCode());
			method.releaseConnection();
		}
		return result;
	}

	// private String handleResult(HttpResponse response,
	// HttpUriRequest httpMethod, String encoding)
	// throws HttpClientException {
	// int responseCode = response.getStatusLine().getStatusCode();
	// if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
	//
	// HttpEntity entity = response.getEntity();
	//
	// if (entity != null) {
	// String result = null;
	// try {
	// result = EntityUtils.toString(entity, encoding);
	// } catch (ParseException e) {
	// throw new HttpClientException(ErrorCodeEnum.SYSTEM_ERROR,
	// "ParseException", e);
	// } catch (IOException e) {
	// throw new HttpClientException(ErrorCodeEnum.SYSTEM_ERROR,
	// "IOException", e);
	// }
	// return result;
	// }
	// }
	// throw new HttpClientException(ErrorCodeEnum.NOT_FOUND_ERROR,
	// "responseCode : " + responseCode);
	//
	// }

	private org.apache.commons.httpclient.HttpClient createHttpClient() {
		HttpClientParams clientParams = new HttpClientParams();
		clientParams.setCookiePolicy(CookiePolicy.IGNORE_COOKIES);
		org.apache.commons.httpclient.HttpClient httpClient = new org.apache.commons.httpclient.HttpClient(
				clientParams, connManager);
		return httpClient;
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
			c.get(new HttpUrl(
					"http://res.gsjy.net/PLWeb/Common/DownAttach.aspx?ResID=68004ee21f4c489d9c0c14da482d4ed4&flag=Yes&GID=240",
					new HashMap()), "UTF-8", cookie, null);

		}

		// System.out.println(s);
	}

	/**
	 * 计算文件的contextType
	 * 
	 * @param fileName
	 * @param mapObj
	 * @return
	 */
	private static String getContentType(String fileName, byte[] mapObj) {
		String contextType = null;
		if (fileName != null) {
			contextType = new MimetypesFileTypeMap().getContentType(fileName);
		}
		if (contextType != null) {
			return contextType;
		}
		ByteArrayInputStream bais = null;
		MemoryCacheImageInputStream mcis = null;
		try {
			bais = new ByteArrayInputStream(mapObj);
			mcis = new MemoryCacheImageInputStream(bais);
			Iterator<ImageReader> itr = ImageIO.getImageReaders(mcis);
			while (itr.hasNext()) {
				ImageReader reader = itr.next();
				if (reader instanceof GIFImageReader) {
					contextType = "image/gif";
				} else if (reader instanceof JPEGImageReader) {
					contextType = "image/jpeg";
				} else if (reader instanceof PNGImageReader) {
					contextType = "image/png";
				} else if (reader instanceof BMPImageReader) {
					contextType = "application/x-bmp";
				}
			}
		} finally {
			if (bais != null) {
				try {
					bais.close();
				} catch (IOException ioe) {

				}
			}
			if (mcis != null) {
				try {
					mcis.close();
				} catch (IOException ioe) {

				}
			}
		}
		return contextType;
	}

}
