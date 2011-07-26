package com.cp.util;

import weibo4j.Weibo;

public class WeiboHelper {
	public static Weibo weibo=null;
	public static Weibo getWeibo(boolean isOauth,String ... args) {
		System.setProperty("weibo4j.oauth.consumerKey", Weibo.CONSUMER_KEY);
    	System.setProperty("weibo4j.oauth.consumerSecret", Weibo.CONSUMER_SECRET);
    	
		weibo = new Weibo();
		if(isOauth) {//oauth验证方式 args[0]:访问的token；args[1]:访问的密匙
			weibo.setToken(args[0], args[1]);
		}else {//用户登录方式
    		weibo.setUserId(args[0]);//用户名/ID
    		weibo.setPassword(args[1]);//密码
		}
		return weibo;
	}
	
	public static Weibo getWeibo(String accessToken,String accessTokenSecret){
		
		weibo=WeiboHelper.getWeibo(true, new String[]{accessToken,accessTokenSecret});
		return weibo;
	}
	public static Weibo getEmptyWeibo(){
		Weibo w = new Weibo();
		return w;
	}
	
}
