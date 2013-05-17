package weibo4j.http;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import weibo4j.model.Configuration;
import weibo4j.model.Paging;
import weibo4j.model.PostParameter;
import weibo4j.model.WeiboException;
import weibo4j.org.json.JSONException;
import weibo4j.org.json.JSONObject;
import weibo4j.util.WeiboConfig;

import com.chenjw.client.HttpClient;
import com.chenjw.client.result.Result;
import com.chenjw.spider.location.HttpUrl;

/**
 * @author sinaWeibo
 * 
 */
public class WeiboHttpClient implements java.io.Serializable {
	private static HttpClient httpClient;
	static {
		// 初始化httpClient
		String className = WeiboConfig.getValue("http_client_impl");
		try {
			httpClient= (HttpClient) Class.forName(className).newInstance();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		httpClient.init();

	}
	private static final long serialVersionUID = -176092625883595547L;
	private static final int OK = 200; // OK: Success!
	private static final int NOT_MODIFIED = 304; // Not Modified: There was no
													// new data to return.
	private static final int BAD_REQUEST = 400; // Bad Request: The request was
												// invalid. An accompanying
												// error message will explain
												// why. This is the status code
												// will be returned during rate
												// limiting.
	private static final int NOT_AUTHORIZED = 401; // Not Authorized:
													// Authentication
													// credentials were missing
													// or incorrect.
	private static final int FORBIDDEN = 403; // Forbidden: The request is
												// understood, but it has been
												// refused. An accompanying
												// error message will explain
												// why.
	private static final int NOT_FOUND = 404; // Not Found: The URI requested is
												// invalid or the resource
												// requested, such as a user,
												// does not exists.
	private static final int NOT_ACCEPTABLE = 406; // Not Acceptable: Returned
													// by the Search API when an
													// invalid format is
													// specified in the request.
	private static final int INTERNAL_SERVER_ERROR = 500;// Internal Server
															// Error: Something
															// is broken. Please
															// post to the group
															// so the Weibo team
															// can investigate.
	private static final int BAD_GATEWAY = 502;// Bad Gateway: Weibo is down or
												// being upgraded.
	private static final int SERVICE_UNAVAILABLE = 503;// Service Unavailable:
														// The Weibo servers are
														// up, but overloaded
														// with requests. Try
														// again later. The
														// search and trend
														// methods use this to
														// indicate when you are
														// being rate limited.

	private static String proxyHost = Configuration.getProxyHost();
	private static int proxyPort = Configuration.getProxyPort();
	private static String proxyAuthUser = Configuration.getProxyUser();
	private static String proxyAuthPassword = Configuration.getProxyPassword();
	private String token;

	// private static final Account ACCOUNT = new Account();
	// private final AtomicLong REMAIN_HITS = new AtomicLong(0);

	public String getProxyHost() {
		return proxyHost;
	}

	public int getProxyPort() {
		return proxyPort;
	}

	public String getProxyAuthUser() {
		return proxyAuthUser;
	}

	public String getProxyAuthPassword() {
		return proxyAuthPassword;
	}

	public String setToken(String token) {
		this.token = token;
		return this.token;
	}

	private final static boolean DEBUG = Configuration.getDebug();
	static Logger log = Logger.getLogger(WeiboHttpClient.class.getName());

	/**
	 * log调试
	 * 
	 */
	private static void log(String message) {
		if (DEBUG) {
			log.debug(message);
		}
	}

	/**
	 * 处理http getmethod 请求
	 * 
	 */

	public Response get(String url) throws WeiboException {

		return get(url, new PostParameter[0]);

	}

	// private void waitForRateLimit() throws WeiboException {
	// if (this != ACCOUNT.client) {
	// while (true) {
	// if (REMAIN_HITS.get() <= 0) {
	// RateLimitStatus rateLimitStatus;
	// synchronized (ACCOUNT) {
	// ACCOUNT.setToken(token);
	// rateLimitStatus = ACCOUNT.getAccountRateLimitStatus();
	// }
	// long remainHits = rateLimitStatus.getRemainingUserHits();
	// if (remainHits == 0) {
	// log.info("sleep to " + rateLimitStatus.getResetTime());
	// try {
	// Thread.sleep(30 * 1000);
	// } catch (InterruptedException e) {
	// e.printStackTrace();
	// }
	// } else {
	// REMAIN_HITS.set(remainHits);
	// }
	// } else {
	// long r = REMAIN_HITS.decrementAndGet();
	// log.info("remain " + r);
	// return;
	// }
	// }
	// }
	// }

	public Response get(String url, PostParameter[] params)
			throws WeiboException {
		log("Request:");
		log("GET:" + url);
		Map<String, String> queryParams = null;
		if (params != null) {
			queryParams = new HashMap<String, String>();
			for (PostParameter param : params) {
				queryParams.put(param.getName(), param.getValue());
			}
		}

		HttpUrl httpUrl = new HttpUrl(url, queryParams);
		Result result = null;
		result = httpClient.get(httpUrl, null, null,
				addTokenToHeaders(null, true));

		if (!result.isSuccess()) {
			handleException(result);
		}
		Response response = new Response();
		response.setResponseAsString(result.getResultString());
		return response;
	}

	public Response get(String url, PostParameter[] params, Paging paging)
			throws WeiboException {
		if (null != paging) {
			List<PostParameter> pagingParams = new ArrayList<PostParameter>(4);
			if (-1 != paging.getMaxId()) {
				pagingParams.add(new PostParameter("max_id", String
						.valueOf(paging.getMaxId())));
			}
			if (-1 != paging.getSinceId()) {
				pagingParams.add(new PostParameter("since_id", String
						.valueOf(paging.getSinceId())));
			}
			if (-1 != paging.getPage()) {
				pagingParams.add(new PostParameter("page", String
						.valueOf(paging.getPage())));
			}
			if (-1 != paging.getCount()) {
				if (-1 != url.indexOf("search")) {
					// search api takes "rpp"
					pagingParams.add(new PostParameter("rpp", String
							.valueOf(paging.getCount())));
				} else {
					pagingParams.add(new PostParameter("count", String
							.valueOf(paging.getCount())));
				}
			}
			PostParameter[] newparams = null;
			PostParameter[] arrayPagingParams = pagingParams
					.toArray(new PostParameter[pagingParams.size()]);
			if (null != params) {
				newparams = new PostParameter[params.length
						+ pagingParams.size()];
				System.arraycopy(params, 0, newparams, 0, params.length);
				System.arraycopy(arrayPagingParams, 0, newparams,
						params.length, pagingParams.size());
			} else {
				if (0 != arrayPagingParams.length) {
					String encodedParams = encodeParameters(arrayPagingParams);
					if (-1 != url.indexOf("?")) {
						url += "&" + encodedParams;
					} else {
						url += "?" + encodedParams;
					}
				}
			}
			return get(url, newparams);
		} else {
			return get(url, params);
		}
	}

	/**
	 * 处理http post请求
	 * 
	 */

	public Response post(String url, PostParameter[] params)
			throws WeiboException {
		return post(url, params, true);

	}

	public Response post(String url, PostParameter[] params,
			Boolean WithTokenHeader) throws WeiboException {
		log("Request:");
		log("POST" + url);

		Map<String, String> queryParams = new HashMap<String, String>();
		for (PostParameter param : params) {
			queryParams.put(param.getName(), param.getValue());
		}
		HttpUrl httpUrl = new HttpUrl(url, queryParams);
		Result result = null;
		result = httpClient.post(httpUrl, null, null,
				addTokenToHeaders(null, WithTokenHeader));

		if (!result.isSuccess()) {
			handleException(result);
		}
		Response response = new Response();
		response.setResponseAsString(result.getResultString());
		return response;
	}

	/**
	 * 支持multipart方式上传图片
	 * 
	 */
	public Response multPartURL(String url, PostParameter[] params,
			ImageItem item) throws WeiboException {
		return multPartURL(item.getName(), url, params, item.getName(),
				item.getContent());
	}

	public Response multPartURL(String fileParamName, String url,
			PostParameter[] params, File file, boolean authenticated)
			throws WeiboException {
		try {
			return multPartURL(fileParamName, url, params, file.getName(),
					FileUtils.readFileToByteArray(file));
		} catch (IOException e) {
			throw new WeiboException("file cant read", e, -1);
		}
	}

	private Response multPartURL(String fileParamName, String url,
			PostParameter[] params, String fileName, byte[] fileBytes)
			throws WeiboException {
		Map<String, String> queryParams = new HashMap<String, String>();
		for (PostParameter param : params) {
			queryParams.put(param.getName(), param.getValue());
		}
		HttpUrl httpUrl = new HttpUrl(url, queryParams);
		Result result = null;
		result = httpClient.multPartPost(httpUrl, null, null,
				addTokenToHeaders(null, true), fileParamName, fileName,
				fileBytes);

		if (!result.isSuccess()) {
			handleException(result);
		}
		Response response = new Response();
		response.setResponseAsString(result.getResultString());
		return response;
	}

	private Map<String, String> addTokenToHeaders(Map<String, String> headers,
			boolean isAdd) throws WeiboException {
		if (!isAdd) {
			return headers;
		}
		if (headers == null) {
			headers = new HashMap<String, String>();
		}
		if (token == null) {
			throw new WeiboException("Oauth2 token is not set!");
		}
		InetAddress ipaddr;
		try {
			ipaddr = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			throw new WeiboException("UnknownHost", e);
		}
		headers.put("Authorization", "OAuth2 " + token);
		headers.put("API-RemoteIP", ipaddr.getHostAddress());
		for (Entry<String, String> hd : headers.entrySet()) {
			log(hd.getKey() + ": " + hd.getValue());
		}
		return headers;
	}

	private void handleException(Result result) throws WeiboException {
		if (!result.isSuccess()) {
			try {
				throw new WeiboException(getCause(result.getResponseCode()),
						new JSONObject(result.getResultString()),
						result.getMethodStatusCode());
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	/*
	 * 对parameters进行encode处理
	 */
	public static String encodeParameters(PostParameter[] postParams) {
		StringBuffer buf = new StringBuffer();
		for (int j = 0; j < postParams.length; j++) {
			if (j != 0) {
				buf.append("&");
			}
			try {
				buf.append(URLEncoder.encode(postParams[j].getName(), "UTF-8"))
						.append("=")
						.append(URLEncoder.encode(postParams[j].getValue(),
								"UTF-8"));
			} catch (java.io.UnsupportedEncodingException neverHappen) {
			}
		}
		return buf.toString();
	}

	private static String getCause(int statusCode) {
		String cause = null;
		switch (statusCode) {
		case NOT_MODIFIED:
			break;
		case BAD_REQUEST:
			cause = "The request was invalid.  An accompanying error message will explain why. This is the status code will be returned during rate limiting.";
			break;
		case NOT_AUTHORIZED:
			cause = "Authentication credentials were missing or incorrect.";
			break;
		case FORBIDDEN:
			cause = "The request is understood, but it has been refused.  An accompanying error message will explain why.";
			break;
		case NOT_FOUND:
			cause = "The URI requested is invalid or the resource requested, such as a user, does not exists.";
			break;
		case NOT_ACCEPTABLE:
			cause = "Returned by the Search API when an invalid format is specified in the request.";
			break;
		case INTERNAL_SERVER_ERROR:
			cause = "Something is broken.  Please post to the group so the Weibo team can investigate.";
			break;
		case BAD_GATEWAY:
			cause = "Weibo is down or being upgraded.";
			break;
		case SERVICE_UNAVAILABLE:
			cause = "Service Unavailable: The Weibo servers are up, but overloaded with requests. Try again later. The search and trend methods use this to indicate when you are being rate limited.";
			break;
		default:
			cause = "";
		}
		return statusCode + ":" + cause;
	}

	public String getToken() {
		return token;
	}

}