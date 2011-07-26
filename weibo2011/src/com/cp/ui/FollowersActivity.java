package com.cp.ui;

import java.util.List;

import weibo4j.Paging;
import weibo4j.User;
import weibo4j.Weibo;
import weibo4j.WeiboException;

import com.cp.logic.MainService;
import com.cp.util.DateHelper;
import com.cp.util.NetUtil;
import com.cp.util.WeiboHelper;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class FollowersActivity extends Activity implements IWeibo{
		ListView followersListView;
		Button back,next;
		TextView ff_title;
		Weibo weibo=WeiboHelper.getWeibo(MainService.Accesstoken,MainService.Access_token_secret);
		List<User> users;
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.followers);
			followersListView=(ListView) findViewById(R.id.followersListView);
//			ff_title=(TextView) findViewById(R.id.ff_title);
//			ff_title.setText("我的粉丝");
			init();
			followersListView.setAdapter(new MyAdapter(this));
			followersListView.addFooterView(back);
		}
		
		@Override
		public void init() {
			Paging paging=new Paging(1,5);
			try {
				users=weibo.getFollowersStatuses(paging);
			} catch (WeiboException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		@Override
		public void refresh(Object... param) {
			// TODO Auto-generated method stub
			
		}
		
		public class MyAdapter extends BaseAdapter{
			Context context;
			public MyAdapter(Context context){
				this.context=context;
			}
			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return users.size();
			}

			@Override
			public Object getItem(int position) {
				// TODO Auto-generated method stub
				return users.get(position);
			}

			@Override
			public long getItemId(int position) {
				// TODO Auto-generated method stub
				return position;
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				// TODO Auto-generated method stub
				
				
				ViewHolder holder;
				if(convertView!=null){
					holder=(ViewHolder) convertView.getTag();
				}else {
					holder=new ViewHolder();
					convertView=LayoutInflater.from(context).inflate(R.layout.followers_list, null);
					holder.f_userIcon=(ImageView) convertView.findViewById(R.id.f_userIcon);
					holder.f_userName=(TextView) convertView.findViewById(R.id.f_userName);
					holder.f_oneRow=(TextView)convertView.findViewById(R.id.f_oneRow);
					holder.f_twoRow=(TextView) convertView.findViewById(R.id.f_twoRow);
					holder.f_threeRow=(TextView)convertView.findViewById(R.id.f_threeRow);
					convertView.setTag(holder);
				}
				
			
				User user=users.get(position);
				BitmapDrawable bd=NetUtil.getPicFromUrl(user.getProfileImageURL());
				holder.f_userIcon.setImageDrawable(bd);
				holder.f_userName.setText(user.getName().toString());
				holder.f_oneRow.setText("关注： "+user.getFriendsCount()+"   粉丝： "+user.getFollowersCount()
						+"    微博："+user.getStatusesCount());
				holder.f_twoRow.setText("来自： "+user.getLocation()+"      "+DateHelper.showCurrentTime(user.getCreatedAt()));
				holder.f_threeRow.setText(user.getDescription());
				return convertView;
			}
			
		}

		static class ViewHolder{
			ImageView f_userIcon;
			TextView f_userName,f_oneRow,f_twoRow,f_threeRow;
		}
}
