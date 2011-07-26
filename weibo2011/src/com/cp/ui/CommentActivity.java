package com.cp.ui;

import com.cp.logic.MainService;
import com.cp.util.WeiboHelper;

import weibo4j.Comment;
import weibo4j.Status;
import weibo4j.Weibo;
import weibo4j.WeiboException;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CommentActivity extends Activity{
	Button comment_btn;
	EditText comment_content;
	Status status=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.comment);
		comment_btn=(Button) findViewById(R.id.comment_button);
		comment_content=(EditText) findViewById(R.id.comment_content);
		comment_btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String sid=getIntent().getStringExtra("sid");
//				Weibo weibo=WeiboHelper.getWeibo();
				Weibo weibo=WeiboHelper.getWeibo(MainService.Accesstoken,MainService.Access_token_secret);
				try {
					
				Comment c=	weibo.updateComment(comment_content.getText().toString(), sid, null);
//				List<Comment> l=weibo.getCommentsByMe();
				} catch (WeiboException e) {	
					e.printStackTrace();
				}
				Toast.makeText(CommentActivity.this, "评论已提交", 2000).show();
			}
		});
	}
}
