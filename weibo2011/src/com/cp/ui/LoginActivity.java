package com.cp.ui;

import java.util.HashMap;
import java.util.List;
import java.util.SortedSet;

import oauth.signpost.OAuthProvider;
import oauth.signpost.basic.DefaultOAuthProvider;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.exception.OAuthNotAuthorizedException;


import weibo4j.Status;
import weibo4j.User;
import weibo4j.Weibo;
import weibo4j.WeiboException;
import weibo4j.http.AccessToken;
import weibo4j.http.RequestToken;

import com.cp.db.DatabaseHelper;
import com.cp.logic.MainService;
import com.cp.logic.Task;
import com.cp.oauth.OAuthConstant;
import com.cp.util.MyDialog;
import com.cp.util.MyToast;
import com.cp.util.NetUtil;
import com.cp.util.WeiboHelper;
import com.cp.vo.UserInfo;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity implements IWeibo{
	TextView userName,userPass;
	Button submit,cancel;
	ProgressDialog pd;
	boolean is_Authorized,isFirst;
	public static final int LOGIN_ERROR=-100;//登录失败
	CommonsHttpOAuthConsumer httpOauthConsumer;
	OAuthProvider httpOauthprovider;
	String userNameTxt,userPwdTxt;
	DatabaseHelper dbHelper=new DatabaseHelper(LoginActivity.this);
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("uri", "onCreate-------");
        setContentView(R.layout.login);
        userName=(TextView) findViewById(R.id.userName_text);
        userPass=(TextView) findViewById(R.id.userPass_text);
        submit=(Button) findViewById(R.id.login_submit);
        cancel=(Button) findViewById(R.id.login_cancel);
        final SharedPreferences sp=getSharedPreferences("myLogin", Context.MODE_PRIVATE);
        is_Authorized=sp.getBoolean("is_Authorized", false);
        isFirst=sp.getBoolean("isFirst", false);
        if(is_Authorized==true && isFirst==true){
    	   AlertDialog.Builder builder=new AlertDialog.Builder(this);
    	   builder.setTitle("保存认证码之后下次登录无需授权,是否保存？").
    	   		setPositiveButton("保存", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						Uri uri=LoginActivity.this.getIntent().getData();
						try {
							
							RequestToken requestToken= OAuthConstant.getInstance().getRequestToken();
							AccessToken accessToken=requestToken.getAccessToken(uri.getQueryParameter("oauth_verifier"));
							String userName_Input=sp.getString("userName", "");
							String userPwd_Input=sp.getString("userPwd", "");
							String Accesstoken=accessToken.getToken();
							String userId=String.valueOf(accessToken.getUserId());
							String Access_token_secret=accessToken.getTokenSecret();

							Editor editor=sp.edit();
							editor.putString("userId", userId);
							editor.putString("Accesstoken", Accesstoken);
							editor.putString("Access_token_secret", Access_token_secret);
							editor.putBoolean("isFirst", false);
							editor.commit();
							
							Log.i("uri", "userId"+userId+"    Accesstoken:"+Accesstoken+ "    Access_token_secret"+Access_token_secret);
							OAuthConstant.getInstance().setAccessToken(accessToken);
							dbHelper.saveUserBasicMessage(userName_Input, userPwd_Input, userId, Accesstoken, Access_token_secret);
							Toast.makeText(LoginActivity.this, "保存成功!", 3000).show();
							System.exit(0);
						} catch (WeiboException e) {
							e.printStackTrace();
						}
					}
				}).setNegativeButton("暂不保存", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						finish();
					}
				}).create().show();
       } 
        
        //登录按钮
		submit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// 到数据库中 查询 key.. 如果查不到,就去认证
				final String userNameTxt = userName.getText().toString();
				final String userPwdTxt = userPass.getText().toString();
				if (userNameTxt.equals("") || userPwdTxt.equals("")) {
					Toast.makeText(LoginActivity.this, "用户名或者密码不能为空!", 1000)
							.show();
					return;
				} else {
					// Sqllite 数据库操作
					boolean b = dbHelper.checkUserIsExist(userNameTxt,
							userPwdTxt);
					if (b) {
						SharedPreferences s = getSharedPreferences("myLogin",
								Context.MODE_PRIVATE);
						String m = s.getString("userName", "");
						String n = s.getString("userPwd", "");
						Toast.makeText(LoginActivity.this, "已授权登陆...", 1000)
								.show();

						if (pd == null) {
							pd = new ProgressDialog(LoginActivity.this);
						}
						pd.setTitle("新浪微博");
						pd.setMessage("正在登录...");
						pd.setCancelable(true);
						pd.show();
						HashMap param = new HashMap();
						MainService.allTask.add(new Task(Task.USER_LOGIN, param));
						
					} else {

						SharedPreferences sp = getSharedPreferences("myLogin",
								Context.MODE_PRIVATE);
						Editor editor = sp.edit();
						editor.putString("userName", userNameTxt);
						editor.putString("userPwd", userPwdTxt);
						editor.putBoolean("is_Authorized", true);
						editor.putBoolean("isFirst", true);
						editor.commit();

						Toast.makeText(LoginActivity.this, "准备进入新浪官方授权页面...",
								1000).show();
						System.setProperty("weibo4j.oauth.consumerKey",
								Weibo.CONSUMER_KEY);
						System.setProperty("weibo4j.oauth.consumerSecret",
								Weibo.CONSUMER_SECRET);
						Weibo weibo = OAuthConstant.getInstance().getWeibo();
						RequestToken requestToken;
						try {
							requestToken = weibo
									.getOAuthRequestToken("weibo2011://LoginActivity");
							Uri uri = Uri.parse(requestToken
									.getAuthenticationURL()
									+ "&from=xweibo");
							Log.i("uri", String.valueOf(uri));
							OAuthConstant.getInstance().setRequestToken(
									requestToken);
							startActivity(new Intent(Intent.ACTION_VIEW, uri));
						} catch (WeiboException e) {
							e.printStackTrace();
						}
					}
				}
			}
		});
        cancel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
        MainService.allActivity.add(this);//将本身添加到Activity集合,便于集中管理
    }
    
	@Override
	protected void onResume() {
		super.onResume();
		init();
		
	}

	@Override
	public void init() {
		if(NetUtil.checkNet(this)){
			Intent intent=new Intent();
			intent.setAction("com.cp.logic.MainService");
			startService(intent);
		}else{
			MyDialog.showDialog(this);
		}
	}

	@Override
	public void refresh(Object... param) {
		switch (((Integer)param[0]).intValue()) {
		case Task.LOGIN_SUCCESS:
			Intent intent=new Intent();
			intent.setAction("com.cp.MainActivity");
			startActivity(intent);
			break;
		case LoginActivity.LOGIN_ERROR:
			pd.dismiss();	
			MyToast.showToast(this);
			break;
		default:
			break;
		}
	}
	
}