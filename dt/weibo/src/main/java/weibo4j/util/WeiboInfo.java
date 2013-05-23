package weibo4j.util;

public class WeiboInfo {
	private String redirectUrl;
	private String baseUrl;
	private String accessTokenUrl;
	private String rmUrl;
	private String appUrl;
	private String authorizeUrl;
	private String httpClientImpl;

	public String getClientId() {
		ClientInfo clientInfo = WeiboConfig.clientInfo.get();
		return clientInfo.getClientId();
	}

	public String getClientSecret() {
		ClientInfo clientInfo = WeiboConfig.clientInfo.get();
		return clientInfo.getClientSecret();
	}

	public String getHttpClientImpl() {
		return httpClientImpl;
	}

	public void setHttpClientImpl(String httpClientImpl) {
		this.httpClientImpl = httpClientImpl;
	}

	public String getAuthorizeUrl() {
		return authorizeUrl;
	}

	public void setAuthorizeUrl(String authorizeUrl) {
		this.authorizeUrl = authorizeUrl;
	}

	public String getRedirectUrl() {
		return redirectUrl;
	}

	public void setRedirectUrl(String redirectUrl) {
		this.redirectUrl = redirectUrl;
	}

	public String getBaseUrl() {
		return baseUrl;
	}

	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	public String getAccessTokenUrl() {
		return accessTokenUrl;
	}

	public void setAccessTokenUrl(String accessTokenUrl) {
		this.accessTokenUrl = accessTokenUrl;
	}

	public String getRmUrl() {
		return rmUrl;
	}

	public void setRmUrl(String rmUrl) {
		this.rmUrl = rmUrl;
	}

	public String getAppUrl() {
		return appUrl;
	}

	public void setAppUrl(String appUrl) {
		this.appUrl = appUrl;
	}

}
