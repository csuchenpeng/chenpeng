package com.cp.ui;

import com.cp.logic.MainService;
import com.cp.util.WeiboHelper;

import weibo4j.Status;
import weibo4j.Weibo;
import weibo4j.WeiboException;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RepostStatusActivity extends Activity{
	Button repost_status_btn;
	EditText repost_status_input;
	TextView repost_status_title,repost_status_content;
	Status sta;
	@Override
	protected void onCreate(Bundle savedInstanceState) {	
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.repost_status);
		Intent intent=this.getIntent();
		sta=(Status) intent.getExtras().get("sta");
		repost_status_btn=(Button) findViewById(R.id.repost_status_button);
		repost_status_content= (TextView) findViewById(R.id.repsot_status_content);
		repost_status_input=(EditText) findViewById(R.id.repost_status_input);
		repost_status_title=(TextView) findViewById(R.id.repost_status_title);
		repost_status_title.setText("转发  @"+sta.getUser().getName()+"的微博：");
		repost_status_content.setText(sta.getText());
		repost_status_btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String sid=String.valueOf(sta.getId());
//		    	Weibo weibo=WeiboHelper.getWeibo();
				Weibo weibo=WeiboHelper.getWeibo(MainService.Accesstoken,MainService.Access_token_secret);
		    	try {
					weibo.repost(sid, repost_status_input.getText().toString());
				} catch (WeiboException e) {
					e.printStackTrace();
				}
				Toast.makeText(RepostStatusActivity.this, "微博转发成功！", 2000).show();
			}
		});
	}
}
