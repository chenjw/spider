package weibo4j.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class WeiboConfig {
	private static WeiboInfo weiboInfo;
	public static ThreadLocal<ClientInfo> clientInfo = new ThreadLocal<ClientInfo>();

	public static void setClientInfo(ClientInfo clientInfo) {
		WeiboConfig.clientInfo.set(clientInfo);
	}

	public WeiboConfig() {
	}

	private static Properties props = new Properties();

	
	public static void load(WeiboInfo weiboInfo) {
		WeiboConfig.weiboInfo=weiboInfo;
	}
	
	public static void loadFromPath(String path) {
		try {
			props.load(Thread.currentThread().getContextClassLoader()
					.getResourceAsStream(path));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static WeiboInfo getWeiboInfo() {
		return weiboInfo;
	}


}
