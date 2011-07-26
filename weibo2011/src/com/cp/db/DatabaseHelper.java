package com.cp.db;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper{
	

	public DatabaseHelper(Context context) {
		super(context, "myToken.db", null, 1);
		
	}

	//数据库创建时  调用
	@Override
	public void onCreate(SQLiteDatabase db) {
		//创建表
		//主键一般必须命名为_id
		db.execSQL("create table user(_id integer primary key autoincrement,userName text,userPwd text,userId text ,accessToken text,accessTokeSecret text)");
		Log.i("uri", "---------------");
	}
	
	//数据库版本更新时 调用
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//		//从第一版 升级到第二版
//		if(oldVersion==1){
//			db.execSQL("drop table favs if exists");
//			db.execSQL("create table favs(_id int primary key autoincrement,name text,uri text,description text,aa text)");
//		}//从第二版 升级到第三版
//		else if(oldVersion==2){
//			db.execSQL("create table xx(_id int primary key autoincrement,name text,uri)");
//		}
	}
	
	public void saveUserBasicMessage(String userName,String userPwd,String userId,String accessToken,String accessTokeSecret){
		SQLiteDatabase db = getWritableDatabase();
//		ContentValues values = new ContentValues();
//		values.put("name", name);
//		values.put("uri", uri);
//		values.put("description", desc);
//		db.insert("favs", null, values);
		db.execSQL("insert into user(userName,userPwd,userId,accessToken,accessTokeSecret) values(?,?,?,?,?)",new String[]{userName,userPwd,userId,accessToken,accessTokeSecret});
	}
	
	//通过用户的用户名和密码找到该用户相对应的访问令牌
	public HashMap findAccessTokenByUserBasicMessage(String userName,String userPwd){

		SQLiteDatabase db = getWritableDatabase();		
		HashMap param=new HashMap();
		String userId="";
		String accessToken="";
		String accessTokeSecret="";
		//Cursor代表结果集
		Cursor c = db.rawQuery("select * from user where userName=? and userPwd=?", new String[]{userName,userPwd});
		if(c.moveToFirst()){
			for(int i=0;i<c.getCount();i++){
				userId=c.getString(3);
				accessToken=c.getString(4);
				accessTokeSecret=c.getString(5);
			}
			
			param.put("userId",userId);
			param.put("accessToken",accessToken);
			param.put("accessTokeSecret",accessTokeSecret);
	
		}
		return param;
	}
	
	//判断用户是否存在
	public boolean checkUserIsExist(String userName,String userPwd){
		SQLiteDatabase db=this.getReadableDatabase();
		Cursor c=db.rawQuery("select * from user where userName=? and userPwd=?", new String[]{userName,userPwd});
		int i=c.getCount();
		if(i>0){
			c.close();
			return true;
		}else{
			c.close();
			return false;
		}
	}
	
}
