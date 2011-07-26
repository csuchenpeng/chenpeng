package com.cp.util;

import weibo4j.Weibo;

public class WeiboHelper {
	public static Weibo weibo=null;
	public static Weibo getWeibo(boolean isOauth,String ... args) {
		System.setProperty("weibo4j.oauth.consumerKey", Weibo.CONSUMER_KEY);
    	System.setProperty("weibo4j.oauth.consumerSecret", Weibo.CONSUMER_SECRET);
    	
		weibo = new Weibo();
		if(isOauth) {//oauth��֤��ʽ args[0]:���ʵ�token��args[1]:���ʵ��ܳ�
			weibo.setToken(args[0], args[1]);
		}else {//�û���¼��ʽ
    		weibo.setUserId(args[0]);//�û���/ID
    		weibo.setPassword(args[1]);//����
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
