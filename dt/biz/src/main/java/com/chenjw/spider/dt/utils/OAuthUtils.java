package com.chenjw.spider.dt.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import weibo4j.Oauth;
import weibo4j.model.WeiboException;

public class OAuthUtils {
	private static Oauth oauth = new Oauth();

	public static String getAuthorizeUrl() {
		try {
			return oauth.authorize("code", "", "");
		} catch (WeiboException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String getAccessTokenByCode(String code) {
		try {
			return oauth.getAccessTokenByCode(code).getAccessToken();
		} catch (WeiboException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static void main(String[] args) throws WeiboException, IOException {

		// BareBonesBrowserLaunch.openURL(getAuthorizeUrl());
		System.out.println(getAuthorizeUrl());
		System.out.print("Hit enter when it's done.[Enter]:");
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String code = br.readLine();
		System.out.println(getAccessTokenByCode(code));

	}

}