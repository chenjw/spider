package weibo4j;

import weibo4j.http.WeiboHttpClient;

public class Weibo implements java.io.Serializable {

	private static final long serialVersionUID = 4282616848978535016L;

	public final WeiboHttpClient client = new WeiboHttpClient();

	public void setToken(String token) {
		client.setToken(token);
	}

}