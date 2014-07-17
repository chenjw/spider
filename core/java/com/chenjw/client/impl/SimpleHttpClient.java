package com.chenjw.client.impl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.ConnectionPoolTimeoutException;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import com.chenjw.client.exception.ErrorCodeEnum;
import com.chenjw.client.exception.HttpClientException;
import com.chenjw.logger.Logger;

public class SimpleHttpClient implements com.chenjw.client.HttpClient {
    public static final Logger       LOGGER                = Logger
                                                               .getLogger(SimpleHttpClient.class);
    private int                      httpConnectionTimeout = 100000;
    private int                      socketTimeout         = 100000;

    private int                      maxTotalConnections   = 30;
    private int                      defaultMaxPerRoute   = 10;
    private HttpClientBuilder        httpClientBuilder;
    private Map<String, CookieStore> cookieStores          = new ConcurrentHashMap<String, CookieStore>();
    private RequestConfig            requestConfig;

    /** 位置key */

    public void init() {

        LayeredConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
            MySSLSocketFactory.createSSLContext(),
            SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        Registry<ConnectionSocketFactory> r = RegistryBuilder.<ConnectionSocketFactory> create()
            .register("http", PlainConnectionSocketFactory.getSocketFactory())
            .register("https", sslsf).build();

        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(r);
        cm.setMaxTotal(maxTotalConnections);
        cm.setDefaultMaxPerRoute(defaultMaxPerRoute);
        httpClientBuilder = HttpClients.custom().setConnectionManager(cm);


        requestConfig = RequestConfig.custom().setSocketTimeout(socketTimeout)
            .setConnectTimeout(httpConnectionTimeout).build();
    }

    private CloseableHttpClient getHttpClient() {
        return httpClientBuilder.build();
    }

    private String prepareUrl(String url, Map<String, String> params, String encoding) {
        boolean isUrlHasParam = url.indexOf("?") != -1;
        StringBuffer sb = new StringBuffer(url);
        if (params != null) {
            boolean isFirst = true;
            for (Entry<String, String> entry : params.entrySet()) {
                if (!StringUtils.isBlank(entry.getValue())) {
                    if (!isUrlHasParam && isFirst) {
                        sb.append('?');
                        isFirst = false;
                    } else {
                        sb.append('&');
                    }
                    try {
                        sb.append(entry.getKey()).append('=')
                            .append(URLEncoder.encode(entry.getValue(), encoding));
                    } catch (UnsupportedEncodingException e) {
                        //
                    }
                }
            }
        }
        return sb.toString();
    }

    /**
     * 设置http头
     * @param request
     */
    private void setHeaders(HttpUriRequest request) {
        request.setHeader("Accept",
            "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        request.setHeader("Accept-Encoding", "gzip, deflate");
        request.setHeader("Accept-Language", "zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3");
        request.setHeader("Connection", "keep-alive");
        request.setHeader("User-Agent",
            "Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:26.0) Gecko/20100101 Firefox/26.0");
        request
            .setHeader(
                "Referer",
                "https://login.alibaba-inc.com/ssoLogin.htm?APP_NAME=EHR&BACK_URL=https%3A%2F%2Fehr.alibaba-inc.com%2Femployee%2F%2FempInfo.htm%3FworkNo%3D2&CANCEL_CERT=true&CONTEXT_PATH=employee");

    }

    @Override
    public String get(String sessionId, String url, Map<String, String> params, String encoding)
                                                                                                throws HttpClientException {
        String realUrl = prepareUrl(url, params, encoding);
        HttpGet request = new HttpGet(realUrl);
        request.setConfig(requestConfig);
        HttpRequestResult result = excuteMethod(sessionId, request, encoding);
        return result.getContent();
    }

    @Override
    public String post(String sessionId, String url, Map<String, String> params, String encoding)
                                                                                                 throws HttpClientException {
        String realUrl = url;
        HttpPost request = new HttpPost(realUrl);
        request.setConfig(requestConfig);
        if (params != null) {
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            for (Entry<String, String> entry : params.entrySet()) {
                nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
            try {
                request.setEntity(new UrlEncodedFormEntity(nvps, encoding));
            } catch (UnsupportedEncodingException e) {
                LOGGER.error("encoding not support " + encoding, e);
            }
        }
        HttpRequestResult result = excuteMethod(sessionId, request, encoding);
        String r = result.getContent();
        LOGGER.debug(r);
        return r;
    }

    /**
     * 更新http上下文
     * @param sessionId
     * @param context
     */
    protected void updateHttpContext(String sessionId, HttpContext context) {
        if (StringUtils.isBlank(sessionId)) {
            return;
        }
        CookieStore cookieStore = (CookieStore) context
            .getAttribute(HttpClientContext.COOKIE_STORE);
        cookieStores.put(sessionId, cookieStore);
    }

    private void logRequest(HttpUriRequest request, HttpContext context) {
        LOGGER.debug("******request************");
        LOGGER.debug(request.getRequestLine().toString());
        for (Header header : request.getAllHeaders()) {
            LOGGER.debug(header.toString());
        }
        CookieStore cookieStore = (CookieStore) context
            .getAttribute(HttpClientContext.COOKIE_STORE);
        for (Cookie cookie : cookieStore.getCookies()) {
            LOGGER.debug("[COOKIE] " + cookie);
        }

        LOGGER.debug("******request************");
    }

    private void logResponse(HttpResponse response) {
        LOGGER.debug("******response************");
        LOGGER.debug(response.getStatusLine().toString());
        for (Header header : response.getAllHeaders()) {
            LOGGER.debug(header.toString());
        }
        LOGGER.debug("******response************");
    }

    public HttpRequestResult excuteMethod(String sessionId, HttpUriRequest request, String encoding)
                                                                                                    throws HttpClientException {
        HttpRequestResult result = new HttpRequestResult();
        HttpContext context = null;
        if (sessionId != null) {
            context = getHttpContext(sessionId);
        }
        CloseableHttpClient httpClient = getHttpClient();
        CloseableHttpResponse response = null;
        try {
            setHeaders(request);
            logRequest(request, context);

            response = httpClient.execute(request, context);
            logResponse(response);

            HttpEntity entity = response.getEntity();
            result.setContentType(ContentType.getOrDefault(entity));
            result.setBytes(EntityUtils.toByteArray(entity));
            EntityUtils.consume(entity);
            updateHttpContext(sessionId, context);
        } catch (ConnectionPoolTimeoutException e) {
            LOGGER.error("执行HTTP请求失败,从线程池中获取连接超时", e);
            throw new HttpClientException(ErrorCodeEnum.SYSTEM_ERROR, e);
        } catch (ConnectTimeoutException e) {
            LOGGER.error("执行HTTP请求失败,连接超时", e);
            throw new HttpClientException(ErrorCodeEnum.SYSTEM_ERROR, e);
        } catch (Exception e) {
            if (e instanceof HttpClientException) {
                HttpClientException c = (HttpClientException) e;
                throw c;
            }
            LOGGER.error("执行HTTP请求失败", e);
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    LOGGER.error("关闭response失败", e);
                }
            }
        }
        return result;
    }

    protected HttpContext getHttpContext(String sessionId) {
        HttpContext context = new BasicHttpContext();
        if (!StringUtils.isBlank(sessionId)) {
            CookieStore newCookieStore = new BasicCookieStore();
            CookieStore cookieStore = cookieStores.get(sessionId);
            if (cookieStore != null) {
                for (Cookie cookie : cookieStore.getCookies()) {
                    newCookieStore.addCookie(cookie);
                }
            }

            context.setAttribute(HttpClientContext.COOKIE_STORE, newCookieStore);
        }
        return context;
    }

    @Override
    public String getInPage(String sessionId, String url, Map<String, String> params,
                            String encoding) throws HttpClientException {
        return this.get(sessionId, url, params, encoding);
    }

    @Override
    public String postInPage(String sessionId, String url, Map<String, String> params,
                             String encoding) throws HttpClientException {
        return this.postInPage(sessionId, url, params, encoding);
    }

}
