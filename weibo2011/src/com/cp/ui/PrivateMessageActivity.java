package com.cp.ui;

import java.util.ArrayList;
import java.util.List;

import weibo4j.DirectMessage;
import weibo4j.Weibo;
import weibo4j.WeiboException;

import com.cp.logic.MainService;
import com.cp.util.DateHelper;
import com.cp.util.NetUtil;
import com.cp.util.WeiboHelper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class PrivateMessageActivity extends Activity implements IWeibo{

//	Weibo weibo=WeiboHelper.getWeibo();
	Weibo weibo=WeiboHelper.getWeibo(MainService.Accesstoken,MainService.Access_token_secret);
	List<DirectMessage> receiveList,sendList;
	List<DirectMessage> tempList=new ArrayList<DirectMessage>();
	String[] str={"收件箱","发件箱"};
	ListView private_message_listView;
	TextView private_message_title;
	CheckBox private_message_receive,private_message_send;
	Spinner sp;
	ImageButton private_message_delete_btn;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.private_message);
		sp=(Spinner) findViewById(R.id.private_message_spinner);
		private_message_delete_btn=(ImageButton) findViewById(R.id.private_message_delete_btn);
		private_message_title=(TextView) findViewById(R.id.private_message_title);
		private_message_title.setText(str[0]);
		private_message_delete_btn.setEnabled(false);
		
		ArrayAdapter adapter=new ArrayAdapter(this,android.R.layout.simple_spinner_item , str);
		sp.setAdapter(adapter);

		private_message_listView=(ListView) findViewById(R.id.private_message_listView);
		init();
		//为spinner添加事件
		sp.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				switch (position) {
				case 0:
					
					init();
					break;
				case 1:
					try {
						sendList=weibo.getSentDirectMessages();
					} catch (WeiboException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					private_message_title.setText(str[1]);
					private_message_listView.setAdapter(new PrivateMessageAdapter(PrivateMessageActivity.this,sendList,str[1]));
					
					break;	
				default:
					break;
				}
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
		});
		
		//为删除按钮添加事件
		private_message_delete_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				List<DirectMessage> l=tempList;
				for(DirectMessage dm:tempList){
					try {
						
//						weibo.deleteDirectMessage(dm.getId());
						weibo.destroyDirectMessage(dm.getId());
						Toast.makeText(PrivateMessageActivity.this, "删除成功...", 1000).show();
					} catch (WeiboException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					tempList.remove(dm);
				}
				
				
				init();
			}
		});
	}
	
	
	public class PrivateMessageAdapter extends BaseAdapter{
		
		Context context;
		List<DirectMessage> dm;
		String flag;
		public PrivateMessageAdapter(Context context,List<DirectMessage> dmList,String sFlag){
			this.context=context;
			this.dm=dmList;
			this.flag=sFlag;
		}
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return dm.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return dm.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			final ViewaHolder holder;
			final DirectMessage d=dm.get(position);
			if(convertView==null){
				holder=new ViewaHolder();
				if(flag.equals(str[0])){//当传进来的标志是收件箱时,则初始化收件箱对应的xml文件
					convertView=LayoutInflater.from(context).inflate(R.layout.private_message_item, null);
					holder.private_message_userImage=(ImageView) convertView.findViewById(R.id.private_message_userImage);
					holder.private_message_userName=(TextView) convertView.findViewById(R.id.private_message_userName);
					holder.private_message_content=(TextView) convertView.findViewById(R.id.private_message_content);
					holder.private_message_time=(TextView) convertView.findViewById(R.id.private_message_time);	
					holder.private_message_more_btn=(Button) convertView.findViewById(R.id.private_message_more);
					holder.private_message_cbox_btn=(CheckBox) convertView.findViewById(R.id.private_message_cbox_btn);
					
					//为发私信按钮添加点击事件
					holder.private_message_more_btn.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							//将微博的xml文件共享
							View view=LayoutInflater.from(context).inflate(R.layout.publish_weibo, null);
							TextView private_send_message_title=(TextView) view.findViewById(R.id.publishweibo_title);
							final EditText private_message_input=(EditText) view.findViewById(R.id.publishweibo_content);
							private_send_message_title.setText("对"+d.getSenderScreenName()+"说:");
							Toast.makeText(PrivateMessageActivity.this, "点击了"+position+"项", 1000).show();
							AlertDialog.Builder builder=new AlertDialog.Builder(context);
							builder.setTitle("发送私信").setView(view)
									.setPositiveButton("OK", new DialogInterface.OnClickListener() {
										
										@Override
										public void onClick(DialogInterface dialog, int which) {
											// TODO Auto-generated method stub
											try {
												weibo.sendDirectMessage(String.valueOf(d.getSenderId()), private_message_input.getText().toString());
											} catch (WeiboException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											}
											Toast.makeText(PrivateMessageActivity.this, "发送成功..", 1000).show();
										}
									});
							builder.create();
							builder.show();
						}
					});
					
					//为CheckBox 添加OnCheckedChangeListener 事件
					holder.private_message_cbox_btn.setOnCheckedChangeListener(new OnCheckedChangeListener() {
						
						@Override
						public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
							// TODO Auto-generated method stub
							if(isChecked){
								tempList.add(d);
								private_message_delete_btn.setEnabled(true);
								Toast.makeText(PrivateMessageActivity.this,"共"+tempList.size()+"项", 1000).show();
							}else {
								tempList.remove(d);
								private_message_delete_btn.setEnabled(false);
								Toast.makeText(PrivateMessageActivity.this, "共"+tempList.size()+"项", 1000).show();
							}
							
						}
					});
					
					holder.private_message_userImage.setImageDrawable(NetUtil.getPicFromUrl(d.getSender().getProfileImageURL()));
					holder.private_message_userName.setText(d.getSender().getName());
					holder.private_message_content.setText(d.getText());
					holder.private_message_time.setText(DateHelper.showCurrentTime(d.getCreatedAt()));
					convertView.setTag(holder);
				}else { 		//当传进来的标志是发件箱时,则初始化发件箱对应的xml文件
					
					convertView=LayoutInflater.from(context).inflate(R.layout.private_message_item_send, null);
					holder.private_message_userImage_send=(ImageView) convertView.findViewById(R.id.private_message_userImage_send);
					holder.private_message_userName_send=(TextView) convertView.findViewById(R.id.private_message_userName_send);
					holder.private_message_content_send=(TextView) convertView.findViewById(R.id.private_message_content_send);
					holder.private_message_time_send=(TextView) convertView.findViewById(R.id.private_message_time_send);	
					holder.private_message_cbox_btn_send=(CheckBox) convertView.findViewById(R.id.private_message_cbox_btn_send);
				
					//为CheckBox 添加OnCheckedChangeListener 事件
					holder.private_message_cbox_btn_send.setOnCheckedChangeListener(new OnCheckedChangeListener() {
						
						@Override
						public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
							// TODO Auto-generated method stub
							if(isChecked){
								tempList.add(d);
								private_message_delete_btn.setEnabled(true);
								Toast.makeText(PrivateMessageActivity.this, "共"+tempList.size()+"项", 1000).show();
							}else {
								tempList.remove(d);
								private_message_delete_btn.setEnabled(false);
								Toast.makeText(PrivateMessageActivity.this, "共"+tempList.size()+"项", 1000).show();
							}
							
						}
					});
					
					//为变量赋值
					holder.private_message_userImage_send.setImageDrawable(NetUtil.getPicFromUrl(d.getSender().getProfileImageURL()));
					holder.private_message_userName_send.setText(d.getSender().getName());
					holder.private_message_content_send.setText(d.getText());
					holder.private_message_time_send.setText(DateHelper.showCurrentTime(d.getCreatedAt()));
					convertView.setTag(holder);
				}
				
			}else {
				holder=(ViewaHolder) convertView.getTag();
			}

			return convertView;
		}
		
	}
	
	static  class ViewaHolder{
		ImageView private_message_userImage,private_message_userImage_send;
		TextView private_message_userName,private_message_content,private_message_time,
				 private_message_userName_send,private_message_content_send,private_message_time_send;
		Button private_message_more_btn;
		CheckBox private_message_cbox_btn,private_message_cbox_btn_send;
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		try {
			
			receiveList=weibo.getDirectMessages();
		} catch (WeiboException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		private_message_listView.setAdapter(new PrivateMessageAdapter(this,receiveList,str[0]));
	}

	@Override
	public void refresh(Object... param) {
		// TODO Auto-generated method stub
		
	}
}
