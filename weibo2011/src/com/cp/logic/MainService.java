package com.cp.logic;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cp.ui.HomeActivity;
import com.cp.ui.IWeibo;
import com.cp.ui.LoginActivity;
import com.cp.ui.R;
import com.cp.util.NetUtil;
import com.cp.util.WeiboHelper;

import weibo4j.Comment;
import weibo4j.Paging;
import weibo4j.Status;
import weibo4j.User;
import weibo4j.Weibo;
import weibo4j.WeiboException;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.BounceInterpolator;

public class MainService extends Service implements Runnable{
	public Weibo weibo;
	public User user;
	public static String userId,Accesstoken,Access_token_secret;
	public boolean isRun=true;
	public static ArrayList<Activity> allActivity=new ArrayList<Activity>();//����activity
	public static ArrayList<Task> allTask=new ArrayList<Task>();//��������
	public static HashMap<Integer,BitmapDrawable> allUserIcon=new HashMap<Integer,BitmapDrawable>();//����ͷ��
	public Activity getActivity(String name){ //ͨ��activity���ֻ�ȡActivity
		for (Activity ac:allActivity) {
			if(ac.getClass().getName().indexOf(name)>=0){
				return ac;
			}	
		}
		return null;
	}
	
	
	public static void addTask(Task task){//�������
		allTask.add(task);
	}
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	public void doTask(Task task){//ִ������
		Message msg=handler.obtainMessage();
		msg.what=task.getTask_Id();//��ȡ������
		switch (task.getTask_Id()) {
		case Task.USER_LOGIN:	
			SharedPreferences sp=getSharedPreferences("myLogin", Context.MODE_PRIVATE);
			userId=sp.getString("userId", "");
			Accesstoken=sp.getString("Accesstoken", "");
			Access_token_secret=sp.getString("Access_token_secret", "");
			try {
		    	weibo=WeiboHelper.getWeibo(Accesstoken,Access_token_secret);
				user=weibo.verifyCredentials();
				msg.obj=user;
			} catch (WeiboException e) {
				e.printStackTrace();
				msg.what=LoginActivity.LOGIN_ERROR;
			}
			break;
		case Task.HOME_MESSAGE://��ȡ��ҳ��Ϣ
			List<Status> allStatus=null;
			Paging p=new Paging();
			p.setPage((Integer)task.getParam().get("page"));
			p.setCount((Integer)task.getParam().get("pageSize"));
			try {
				allStatus=weibo.getFriendsTimeline(p);
				for(Status st:allStatus){//����΢���б�
					BitmapDrawable bd=allUserIcon.get(st.getUser().getId());
					if(bd==null){
						HashMap map=new HashMap();
						map.put("userId", st.getUser().getId());
						map.put("imageUrl", st.getUser().getProfileImageURL());
						MainService.addTask(new Task(Task.USER_IMAGEURL,map));
					}
					
				}
				msg.obj=allStatus;
			} catch (WeiboException e) {
				e.printStackTrace();
			}
			break;
		case Task.USER_IMAGEURL://��ȡ�û�ͷ��url
			Integer uId=(Integer) task.getParam().get("userId");
			URL url=(URL) task.getParam().get("imageUrl");
			BitmapDrawable bd=NetUtil.getPicFromUrl(url);//ͨ��url��ȡ�û�ͷ��
			allUserIcon.put(uId, bd);//���û�ͷ��ͨ��userId����һ��HashMap��
			msg.obj=allUserIcon;
			break;
		case Task.COMMENT_LIST://��ȡ��ǰ�û��������յ��������б�
			try {
				Status sta=(Status) task.getParam().get("sta");
				String sid=new String().valueOf(sta.getId());
				List<Comment> comments= weibo.getComments(sid);
				int i=comments.size();
				HashMap  map=new HashMap();
				map.put("sta", sta);
				map.put("comments", comments);
				msg.obj=map;
			} catch (WeiboException e) {
				e.printStackTrace();
			}
			break;
		default:
			break;
		}
		allTask.remove(task);
		handler.sendMessage(msg);
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Thread thread=new Thread(this);
		thread.start();
	}

	@Override
	public void run() {
		Task tempTask=null;
		while(isRun){
			synchronized(allTask){
				if(allTask.size()>0){
					tempTask=allTask.get(0);
					doTask(tempTask);//ִ������
				}
			}
		}
		try {
			Thread.sleep(1000);
		} catch (Exception e) {
		}
	}
	
	Handler handler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case Task.USER_LOGIN://��¼
				IWeibo iw=(IWeibo) getActivity("LoginActivity");
				iw.refresh(Task.LOGIN_SUCCESS,msg.obj);
				break;
			case LoginActivity.LOGIN_ERROR://��¼ʧ��
				IWeibo i=(IWeibo) getActivity("LoginActivity");
				i.refresh(LoginActivity.LOGIN_ERROR,msg.obj);
				break;
			case Task.HOME_MESSAGE://��ȡ��ҳ��Ϣ
				IWeibo homeActivity=(IWeibo) getActivity("HomeActivity");
				homeActivity.refresh(HomeActivity.DATA_GETOK,msg.obj);
				break;
				
			case Task.USER_IMAGEURL:
				IWeibo iwe=(IWeibo) getActivity("HomeActivity");
				iwe.refresh(HomeActivity.REFRESH_ICON,msg.obj);
				break;
			case Task.COMMENT_LIST:
				IWeibo tt=(IWeibo) getActivity("HomeActivity");
				tt.refresh(HomeActivity.GET_COMMENTS,msg.obj);
				break;
			default:
				break;
			}
		}
		
	};
	//�˳�����
	public static void exitApp(Context con){
		for(Activity ac:allActivity){
			ac.finish();
		}
		Intent it=new Intent("com.cp.logic.MainService");
		con.stopService(it);
	}
	
	public static void promtExit(final Context context) {
		LayoutInflater layoutInflater=LayoutInflater.from(context);
		View view=layoutInflater.inflate(R.layout.exitdialog, null);
		AlertDialog.Builder builder=new AlertDialog.Builder(context);
		builder.setView(view).// �趨�Ի�����ʾ������
		setPositiveButton("�˳�", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				exitApp(context);
			}
		})
		.setNegativeButton("ȡ��", null).show();
		
	}
}
