package com.cp.ui;

import java.util.List;

import com.cp.logic.MainService;
import com.cp.util.WeiboHelper;

import weibo4j.Comment;
import weibo4j.Weibo;
import weibo4j.WeiboException;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ReplyCommentActivity extends Activity{
	Button replycomment_btn;
	EditText replycomment_content;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.reply);
		replycomment_btn=(Button) findViewById(R.id.reply_button);
		replycomment_content=(EditText) findViewById(R.id.reply_content);
		replycomment_btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String sid=getIntent().getStringExtra("sid");
//		    	Weibo weibo=WeiboHelper.getWeibo();
				Weibo weibo=WeiboHelper.getWeibo(MainService.Accesstoken,MainService.Access_token_secret);
		    	try {
//					
					List<Comment> comments=weibo.getComments(sid);
					int s=comments.size();
					for(Comment c:comments){
						String cid=String.valueOf(c.getId());
						String id=String.valueOf(c.getInReplyToStatusId());
						weibo.reply(sid, "s001", replycomment_content.getText().toString());
						Log.i("ReplyCommentActivity", "cid:"+cid+"text:"+c.getText()+"id:"+id);
					}
		    	} catch (WeiboException e) {	
					e.printStackTrace();
				}
				Toast.makeText(ReplyCommentActivity.this, "已经回复评论", 2000).show();
			}
		});
	}
}
