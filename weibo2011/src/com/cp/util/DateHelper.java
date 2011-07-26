package com.cp.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateHelper {
	public static Date today_date=new Date();
	String time_distance="";
	public static String showTimeDistance(Date date){	
		if(today_date.getYear()-date.getYear()==0){
			if (today_date.getMonth()-date.getMonth()==0){
				if (today_date.getDay()-date.getDay()==0){
					if (today_date.getHours()-date.getHours()<=24 && today_date.getHours()-date.getHours()>=1){			
						SimpleDateFormat sdf=new SimpleDateFormat("HH:mm");
						String time_distance="����  "+sdf.format(date);
						return time_distance;
					}else {
						if(today_date.getSeconds()-date.getSeconds()>0 && today_date.getSeconds()-date.getSeconds()<60){
							String time_distance=new Integer(today_date.getSeconds()-date.getSeconds()).toString()+"��ǰ";
							return time_distance;
						}
						String time_distance=new Integer(today_date.getMinutes()-date.getMinutes()).toString()+"����ǰ";
						return time_distance;
					}
				}else {
					String time_distance=new Integer(today_date.getDay()-date.getDay()).toString()+"��ǰ";
					return time_distance;
				}
			}else{
				SimpleDateFormat sdf=new SimpleDateFormat("yyyy��MM��dd��");
				String time_distance=sdf.format(date);
				return time_distance;
			} 
		}else{
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy��MM��dd��");
			String time_distance=sdf.format(date);
			return time_distance;
		}
	}
	
	public static String showCurrentTime(Date date){
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy.MM.dd   HH:mm");
		String currentTime=sdf.format(date);
		return currentTime;
	}
}
