package com.chenjw.client.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import javax.net.ssl.SSLContext;

import org.apache.commons.lang.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.ConnectionPoolTimeoutException;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import com.chenjw.client.TheHttpClient;
import com.chenjw.client.exception.ErrorCodeEnum;
import com.chenjw.client.exception.HttpClientException;
import com.chenjw.logger.Logger;


public class SimpleHttpClient implements TheHttpClient {
    public static final Logger LOGGER = Logger.getLogger(SimpleHttpClient.class);
    private int                         httpConnectionTimeout = 100000;
    private int                         socketTimeout         = 100000;

    private int                         maxTotalConnections   = 100;
    private PoolingClientConnectionManager connManager           = null;
    private Map<String, CookieStore>    cookieStores          = new ConcurrentHashMap<String, CookieStore>();
    /** 位置key */
    public static final String          LOCATION_KEY = "Location";
    public void init() {
        connManager = new PoolingClientConnectionManager();
        connManager.setMaxTotal(maxTotalConnections);
        connManager.setDefaultMaxPerRoute(40);
        SSLContext ctx = MySSLSocketFactory.createSSLContext();
        SSLContext.setDefault(ctx);
        SSLSocketFactory ssf = new SSLSocketFactory(ctx);
        SchemeRegistry sr = connManager.getSchemeRegistry();
        //注册支持的http连接模式
        sr.register(new Scheme("https", 443, ssf));
        sr.register(new Scheme("http", 80, PlainSocketFactory.getSocketFactory()));

    }

    private HttpClient getHttpClient() {
        HttpClient httpClient = new DefaultHttpClient(connManager);
        HttpParams params = httpClient.getParams();
        HttpConnectionParams.setConnectionTimeout(params, httpConnectionTimeout);
        HttpConnectionParams.setSoTimeout(params, socketTimeout);
        return httpClient;
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
        request.setHeader("Content-Type", "application/x-www-form-urlencoded");
        request.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;");
        request.setHeader("Accept-Language", "zh-cn");
        request
            .setHeader("User-Agent",
                "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.0.3) Gecko/2008092417 Firefox/3.0.3");
        request.setHeader("Accept-Charset", "UTF-8");
        request.setHeader("Keep-Alive", "600");
        request.setHeader("Connection", "Keep-Alive");
        request.setHeader("Cache-Control", "no-cache");
    }
    
    @Override
	public String get(String sessionId,String url, Map<String, String> params, String encoding) throws HttpClientException {
		String realUrl = prepareUrl(url, params, encoding);
		HttpGet method = new HttpGet(realUrl);
		HttpRequestResult result = excuteMethod(sessionId,method, encoding);
		return result.getContent();
	}

    @Override
    public String post(String sessionId, String url, Map<String, String> params, String encoding) throws HttpClientException  {      
        String realUrl = url;
        HttpPost request = new HttpPost(realUrl);
        if(params!=null){
            for(Entry<String,String> entry:params.entrySet()){
                request.getParams().setParameter(entry.getKey(), entry.getValue());
            }
        }
        HttpRequestResult result = excuteMethod(sessionId,request, encoding);
        return result.getContent();        
    }
    
    
    /**
     * 更新http上下文
     * @param sessionId
     * @param context
     */
    public void updateHttpContext(String sessionId, HttpContext context) {
        if (StringUtils.isBlank(sessionId)) {
            return;
        }
        CookieStore cookieStore = (CookieStore) context.getAttribute(ClientContext.COOKIE_STORE);
        cookieStores.put(sessionId, cookieStore);
    }
    
    private void logRequest(HttpUriRequest request,HttpContext context){
        LOGGER.debug("******request************");
        LOGGER.debug(request.getRequestLine().toString());
        for(Header header:request.getAllHeaders()){
            LOGGER.debug(header.toString());
        }
        CookieStore cookieStore = (CookieStore) context.getAttribute(ClientContext.COOKIE_STORE);
        for(Cookie cookie:cookieStore.getCookies()){
            LOGGER.debug("[COOKIE] "+cookie);
        }
       
        LOGGER.debug("******request************");
    }
    
    private void logResponse(HttpResponse response){
        LOGGER.debug("******response************");
        LOGGER.debug(response.getStatusLine().toString());
        for(Header header:response.getAllHeaders()){
            LOGGER.debug(header.toString());
        }
        LOGGER.debug("******response************");
    }

    public HttpRequestResult excuteMethod(String sessionId,HttpUriRequest request, String encoding) throws HttpClientException {
        HttpRequestResult result=new HttpRequestResult();
        HttpContext context =null;
        if(sessionId!=null){
            context=getHttpContext(sessionId);
        }
        HttpClient httpClient = getHttpClient();
        try {
            setHeaders(request);
            logRequest(request,context);
            
            HttpResponse response = httpClient.execute(request, context);
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
        } 
        return result;
    }

    public HttpContext getHttpContext(String sessionId) {
        HttpContext context = new BasicHttpContext();
        if (!StringUtils.isBlank(sessionId)) {
            CookieStore cookieStore=cookieStores.get(sessionId);
            if(cookieStore==null){
                cookieStore = new BasicCookieStore();
            }
            context.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
        }
        return context;
    }


}
