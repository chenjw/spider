package com.chenjw.client.impl;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.StringUtils;
import org.apache.http.client.CookieStore;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.w3c.css.sac.CSSException;
import org.w3c.css.sac.CSSParseException;
import org.w3c.css.sac.ErrorHandler;

import com.chenjw.client.exception.ErrorCodeEnum;
import com.chenjw.client.exception.HttpClientException;
import com.chenjw.logger.Logger;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.CookieManager;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;

public class HtmlUnitHttpClient extends SimpleHttpClient {
    public static final Logger     LOGGER     = Logger.getLogger(HtmlUnitHttpClient.class);

    private Map<String, CookieManager> cookieManagers = new ConcurrentHashMap<String, CookieManager>();

    @Override
    protected void updateHttpContext(String sessionId, HttpContext context) {
        if (StringUtils.isBlank(sessionId)) {
            return;
        }
        CookieStore cookieStore = (CookieStore) context.getAttribute(HttpClientContext.COOKIE_STORE);
        if(cookieStore!=null){
            CookieManager cookieManager=cookieManagers.get(sessionId);
            if(cookieManager==null){
                cookieManager=new CookieManager();
            }
            for(org.apache.http.cookie.Cookie cookie:cookieStore.getCookies()){
                cookieManager.addCookie(new Cookie(cookie));
            }
            cookieManagers.put(sessionId, cookieManager);
        }
      
    }

    @Override
    protected HttpContext getHttpContext(String sessionId) {
        HttpContext context = new BasicHttpContext();
        if (!StringUtils.isBlank(sessionId)) {
            CookieStore newCookieStore = new BasicCookieStore();
            CookieManager cookieManager=cookieManagers.get(sessionId);
            if (cookieManager != null) {
                for (Cookie cookie : cookieManager.getCookies()) {
                    newCookieStore.addCookie(cookie.toHttpClient());
                }
            }
            context.setAttribute(HttpClientContext.COOKIE_STORE, newCookieStore);
        }
        return context;
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

    private WebClient createWebClient(String sessionId) {
        CookieManager cookieManager = cookieManagers.get(sessionId);
        if (cookieManager == null) {
            cookieManager=new CookieManager();
            cookieManagers.put(sessionId, cookieManager);
        }
        WebClient webClient = new WebClient(BrowserVersion.FIREFOX_17);
        webClient.setCssErrorHandler(new ErrorHandler(){

            @Override
            public void warning(CSSParseException cssparseexception) throws CSSException {
            }

            @Override
            public void error(CSSParseException cssparseexception) throws CSSException {
            }

            @Override
            public void fatalError(CSSParseException cssparseexception) throws CSSException {
            }
            
        });
    
        webClient.getOptions().setUseInsecureSSL(true);
        webClient.setCookieManager(cookieManager);
        return webClient;
    }

    @Override
    public  String getInPage(String sessionId, String url, Map<String, String> params, String encoding)
                                                                                                throws HttpClientException {
        try {

            WebRequest request = new WebRequest(new URL(prepareUrl(url, params, encoding)),
                HttpMethod.GET);

            WebClient webClient = createWebClient(sessionId);
           
            logRequest(request, webClient);
 
            //WebResponse response = webClient.loadWebResponse(request);

                        Page page = webClient.getPage(request);
                        WebResponse response=page.getWebResponse();
            logResponse(response);
            return response.getContentAsString(encoding);
        } catch (Exception e) {
            throw new HttpClientException(ErrorCodeEnum.SYSTEM_ERROR, e);
        }

    }

    @Override
    public  String postInPage(String sessionId, String url, Map<String, String> params, String encoding)
                                                                                                 throws HttpClientException {
        try {
            WebRequest request = new WebRequest(new URL(url), HttpMethod.POST);
            if (params != null) {
                List<NameValuePair> pairs = new ArrayList<NameValuePair>();
                for (Entry<String, String> entry : params.entrySet()) {
                    pairs.add(new NameValuePair(entry.getKey(), entry.getValue()));
                }
                request.setRequestParameters(pairs);
            }
            WebClient webClient = createWebClient(sessionId);
            logRequest(request, webClient);
            //WebResponse response=webClient.loadWebResponse(request);

            Page page = webClient.getPage(request);
            WebResponse response = page.getWebResponse();
            logResponse(response);
            return response.getContentAsString(encoding);
        } catch (Exception e) {
            throw new HttpClientException(ErrorCodeEnum.SYSTEM_ERROR, e);
        }
    }

    private void logRequest(WebRequest request, WebClient webClient) {

        LOGGER.debug("******request************");
        LOGGER.debug(request.toString());

        for (Entry<String, String> entry : request.getAdditionalHeaders().entrySet()) {
            LOGGER.debug(entry.getKey() + " = " + entry.getValue());
        }

        for (Cookie cookie : webClient.getCookieManager().getCookies(request.getUrl())) {
            LOGGER.debug("[COOKIE] " + cookie.toHttpClient());
        }

        LOGGER.debug("******request************");
    }

    private void logResponse(WebResponse response) {
        LOGGER.debug("******response************");
        LOGGER.debug(response.getStatusCode() + " " + response.getStatusMessage());

        for (NameValuePair pair : response.getResponseHeaders()) {
            LOGGER.debug(pair.getName() + " = " + pair.getValue());
        }
        LOGGER.debug("******response************");
    }


}
