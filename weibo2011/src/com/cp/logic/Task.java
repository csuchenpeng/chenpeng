package com.cp.logic;

import java.util.HashMap;

public class Task {
	private int Task_Id;
	private HashMap param;
	public static final int USER_LOGIN=1;//用户登录
	public static final int LOGIN_SUCCESS=2;//登录成功
	public static final int HOME_MESSAGE=3;//获取首页信息
	public  static final int USER_IMAGEURL=4;//用户头像url
	public  static final int COMMENT_LIST=5;//获取当前用户发出和收到的评论列表
	public Task(int taskId, HashMap param) {
		super();
		Task_Id = taskId;
		this.param = param;
	}
	public int getTask_Id() {
		return Task_Id;
	}
	public void setTask_Id(int taskId) {
		Task_Id = taskId;
	}
	public HashMap getParam() {
		return param;
	}
	public void setParam(HashMap param) {
		this.param = param;
	}
	
}
