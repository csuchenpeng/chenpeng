package com.cp.ui;

import com.cp.logic.MainService;
import com.cp.util.WeiboHelper;

import weibo4j.Status;
import weibo4j.Weibo;
import weibo4j.WeiboException;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class PublishWeibo extends Activity{
	
	Button btn;
	EditText weibo_content;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.publish_weibo);
		super.onCreate(savedInstanceState);
		weibo_content=(EditText) findViewById(R.id.publishweibo_content);
//		btn=(Button) findViewById(R.id.publishweibo_button);
		btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Status  st=null;
		    	try {	
//		    		Weibo weibo=WeiboHelper.getWeibo();
		    		Weibo weibo=WeiboHelper.getWeibo(MainService.Accesstoken,MainService.Access_token_secret);
		    		st=weibo.updateStatus(weibo_content.getText().toString());
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				Log.i("PublishWeibo", st.getText());
				Toast.makeText(PublishWeibo.this, "发布成功..", 1000).show();
			}
		});
	}
	
	@Override
	protected void onDestroy() {
		Intent intent=new Intent();
		intent.setAction("com.cp.logic.MainService");
		stopService(intent);
		Toast.makeText(PublishWeibo.this, "Exit progress,end Service..", 1000).show();
		super.onDestroy();
	}
}
