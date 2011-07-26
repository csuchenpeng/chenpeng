package com.cp.util;

public class SourceHelper {
	public static String getCurrentSource(String str){
		int i=str.indexOf(">");
		int j=str.lastIndexOf("<");
		String result = str.substring(i+1, j);
		return result;
	}
}
