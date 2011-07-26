package com.cp.ui;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cp.logic.MainService;
import com.cp.logic.Task;
import com.cp.util.DateHelper;
import com.cp.util.WeiboHelper;

import weibo4j.Comment;
import weibo4j.Status;
import weibo4j.Weibo;
import weibo4j.WeiboException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
//对评论（n条）进行跟帖
public class CommentListActivity extends Activity implements IWeibo{
	
	ListView  commnets_listView;
	List<Comment> comments;
	String userName,weibo_content;
	TextView c_weibo_userName,c_weibo_content;
	Button btn_ok;
	EditText input_content;
	Status sta;
	CommentsListAdapter myAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.comment_list);
		c_weibo_userName=(TextView) findViewById(R.id.c_weibo_userName);
		c_weibo_content=(TextView) findViewById(R.id.c_weibo_content);
		input_content=(EditText) findViewById(R.id.c_weibo_edit);
		commnets_listView=(ListView) findViewById(R.id.c_listview_comments);
		btn_ok=(Button) findViewById(R.id.c_submit_btn);
		Intent intent=this.getIntent();
		Map map=(Map) intent.getSerializableExtra("map");
		
		comments=(List<Comment>) map.get("comments");
		sta=(Status) map.get("sta");
		String userName=sta.getUser().getName().toString();
		String weibo_content=sta.getText().toString();
		c_weibo_userName.setText("评论 "+Html.fromHtml("<b>"+"@"+"</b>")+userName+" 的微博:");
		c_weibo_content.setText(weibo_content);
		myAdapter=new CommentsListAdapter(this);
		commnets_listView.setAdapter(myAdapter);
		btn_ok.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String input=input_content.getText().toString();
				String sid=String.valueOf(sta.getId());
//				Weibo weibo=WeiboHelper.getWeibo();
				Weibo weibo=WeiboHelper.getWeibo(MainService.Accesstoken,MainService.Access_token_secret);
				try {
					weibo.updateComment(input, sid, null);
				} catch (WeiboException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				input_content.setText("");
				myAdapter.notifyDataSetChanged();
				init();
				Toast.makeText(CommentListActivity.this, "评论成功！", 2000).show();
			}
		});
		
	}
	
	@Override
	public void init() {
	
		HashMap param=new HashMap();
		param.put("sta", sta);
		MainService.allTask.add(new Task(Task.COMMENT_LIST,param));
	}

	@Override
	public void refresh(Object... param) {
		// TODO Auto-generated method stub
		
	}
	
	public class CommentsListAdapter extends BaseAdapter{
		
		Context context;
		LayoutInflater layoutInflater;
		public CommentsListAdapter(Context context){
			this.context=context;
			layoutInflater=LayoutInflater.from(context);
		}
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return comments.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return comments.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view=null;
			if(convertView!=null){
				view=convertView;
			}else {
			    view=layoutInflater.inflate(R.layout.comment_items, null);
			}
			
			TextView c_comment_txt=(TextView) view.findViewById(R.id.c_comment_txt);
			Comment comment=comments.get(position);
			c_comment_txt.setText(comment.getUser().getName().toString()+": "
					+comment.getText().toString()+"       "+DateHelper.showTimeDistance(comment.getCreatedAt()));
			return view;
		}
		
	}

}
