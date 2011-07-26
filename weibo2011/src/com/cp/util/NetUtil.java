package com.cp.util;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetUtil {
	public static boolean checkNet(Context context){
		ConnectivityManager connectivityManager=(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
		if(networkInfo!=null && networkInfo.isConnected()){
			if(networkInfo.getState()==NetworkInfo.State.CONNECTED){
				return true;
			}
		}
		return false;
	}
	
	public static BitmapDrawable getPicFromUrl(URL url){
		BitmapDrawable bd=null;
		try {
//			URL u=new URL(url);
			HttpURLConnection con=(HttpURLConnection) url.openConnection();
			InputStream in=con.getInputStream();
			bd=new BitmapDrawable(in);
			return bd;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static BitmapDrawable getPicFromUrl(String address){
		BitmapDrawable bd=null;
		try {
			URL url=new URL(address);
			HttpURLConnection con=(HttpURLConnection) url.openConnection();
			InputStream in=con.getInputStream();
			bd=new BitmapDrawable(in);
			return bd;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
