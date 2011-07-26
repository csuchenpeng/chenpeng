package com.cp.ui;

import com.cp.logic.MainService;
import com.cp.util.NetUtil;
import com.cp.util.WeiboHelper;

import weibo4j.Status;
import weibo4j.Weibo;
import weibo4j.WeiboException;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class OneselfActivity extends Activity implements IWeibo{
	TextView oneself_row_one,oneself_row_two,oneself_row_three;
	ImageView oneself_userImage;
	Button onself_delete_btn,oneself_favorites_btn;
	Status sta;
	String userId;
	boolean is_focused;
	Weibo weibo=WeiboHelper.getWeibo(MainService.Accesstoken,MainService.Access_token_secret);
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.oneself);
		Intent intent=this.getIntent();
		sta=(Status) intent.getSerializableExtra("sta");
		oneself_row_one=(TextView) findViewById(R.id.oneself_row_one);
		oneself_row_two=(TextView) findViewById(R.id.oneself_row_two);
		oneself_row_three=(TextView) findViewById(R.id.oneself_row_three);
		oneself_userImage=(ImageView) findViewById(R.id.oneself_userImage);
		onself_delete_btn=(Button) findViewById(R.id.oneself_delete_btn);
		oneself_favorites_btn=(Button) findViewById(R.id.oneself_favorites_btn);
		init();
		if(is_focused==true){
			onself_delete_btn.setEnabled(false);
			oneself_favorites_btn.setEnabled(true);
		}else{
			onself_delete_btn.setEnabled(true);
			oneself_favorites_btn.setEnabled(false);
		}
		oneself_row_one.setText(sta.getUser().getName());
		oneself_row_two.setText(sta.getText());
		String sourceTxt=sta.getSource();
		int i=sourceTxt.indexOf(">");
		int j=sourceTxt.lastIndexOf("<");
		String source=sourceTxt.substring(i+1, j);
		oneself_row_three.setText("来自："+source);
		oneself_userImage.setImageDrawable(NetUtil.getPicFromUrl(sta.getUser().getProfileImageURL()));
		onself_delete_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					weibo.createFriendship(String.valueOf(sta.getUser().getId()));
				} catch (WeiboException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				Toast.makeText(OneselfActivity.this, "已关注", 1000).show();
				onself_delete_btn.setEnabled(false);
				oneself_favorites_btn.setEnabled(true);
			}
		});
		oneself_favorites_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					weibo.destroyFriendship(String.valueOf(sta.getUser().getId()));
					
				} catch (WeiboException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Toast.makeText(OneselfActivity.this, "取消成功!", 1000).show();
				onself_delete_btn.setEnabled(true);
				oneself_favorites_btn.setEnabled(false);
			}
		});
		
	}
	@Override
	public void init() {
		// TODO Auto-generated method stub
		SharedPreferences s = getSharedPreferences("myLogin",
				Context.MODE_PRIVATE);
		userId = s.getString("userId", userId);;
		try {
			is_focused=weibo.existsFriendship(userId, String.valueOf(sta.getUser().getId()));
		} catch (WeiboException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Override
	public void refresh(Object... param) {
		// TODO Auto-generated method stub
		
	}
}
