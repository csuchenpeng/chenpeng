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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class SendPrivateMessageActivity extends Activity{
	TextView receiver;
	EditText editMessage;
	Button btn_sendPrivateMessage;
	Status sta;
	ImageView sendImageView;
//	Weibo weibo=WeiboHelper.getWeibo();
	Weibo weibo=WeiboHelper.getWeibo(MainService.Accesstoken,MainService.Access_token_secret);
	boolean isVisible=true;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.send_private_message);
		editMessage=(EditText) findViewById(R.id.editMessage);
		receiver=(TextView) findViewById(R.id.receiver_message);
		btn_sendPrivateMessage=(Button) findViewById(R.id.btn_sendPrivateMessage);
		sendImageView=(ImageView) findViewById(R.id.send_private_message_sendImageView);
		Intent intent=this.getIntent();
		sta=(Status) intent.getSerializableExtra("sta");
		receiver.setText("发私信给@ "+sta.getUser().getName());
		btn_sendPrivateMessage.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(isVisible){
					sendImageView.setVisibility(View.VISIBLE);
					isVisible=false;
				}
				String id=String.valueOf(sta.getUser().getId());
				try {
					weibo.sendDirectMessage(id, editMessage.getText().toString());
				} catch (WeiboException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				editMessage.setText("");
//				sendImageView.setVisibility(View.GONE);
//				isVisible=true;
				Toast.makeText(SendPrivateMessageActivity.this, "发送成功!", 2000).show();
			}
		});
	}
}
