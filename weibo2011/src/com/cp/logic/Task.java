package com.cp.logic;

import java.util.HashMap;

public class Task {
	private int Task_Id;
	private HashMap param;
	public static final int USER_LOGIN=1;//�û���¼
	public static final int LOGIN_SUCCESS=2;//��¼�ɹ�
	public static final int HOME_MESSAGE=3;//��ȡ��ҳ��Ϣ
	public  static final int USER_IMAGEURL=4;//�û�ͷ��url
	public  static final int COMMENT_LIST=5;//��ȡ��ǰ�û��������յ��������б�
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
