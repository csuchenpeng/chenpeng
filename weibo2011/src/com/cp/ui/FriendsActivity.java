package com.cp.ui;

import java.util.List;

import weibo4j.Paging;
import weibo4j.Status;
import weibo4j.User;
import weibo4j.Weibo;
import weibo4j.WeiboException;

import com.cp.logic.MainService;
import com.cp.ui.FollowersActivity.MyAdapter;
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
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class FriendsActivity extends Activity implements IWeibo{
	
	ListView followersListView;
	Button back,next;
	TextView ff_title;
	Weibo weibo=WeiboHelper.getWeibo(MainService.Accesstoken,MainService.Access_token_secret);
	List<User> users;
	List<User> moreUser;
	private int pageSize=5;//每页条数
	private int page=1;//页码
	Paging paging;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.followers);//此处关注的xml 和粉丝的xml可以一样
		followersListView=(ListView) findViewById(R.id.followersListView);
//		back=(Button) findViewById(R.id.f_back);
//		next=(Button) findViewById(R.id.f_next);
//		back.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				
//			}
//		});
//		next.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
////				if()
//				page++;
//				paging=new Paging(page,pageSize);
//				
//				try {
//					moreUser=weibo.getFriendsStatuses(paging);
//					
//				} catch (WeiboException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				followersListView.setAdapter(new MyAdapter(FriendsActivity.this,moreUser));
////				followersListView.addFooterView(back);
////				((MyAdapter)(FriendsActivity.this.followersListView.getAdapter())).addMoreData(moreUser);
//				
//			}
//		});
//		ff_title=(TextView) findViewById(R.id.ff_title);
//		ff_title.setText("我的关注");
		paging=new Paging(page,pageSize);
		init();
		followersListView.setAdapter(new MyAdapter(this,users));
//		followersListView.addFooterView(back);
	}
	@Override
	public void init() {
		
		try {
			users=weibo.getFriendsStatuses(paging);
			
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
		List<User> alls;
		public MyAdapter(Context context,List<User> users){
			this.context=context;
			this.alls=users;
		}
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return alls.size();
		}
		
		public void addMoreData(List<User> moreUser){
			
			this.alls.addAll(moreUser);
			this.notifyDataSetChanged();
		}
		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return alls.get(position);
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
			
			
			User user=alls.get(position);
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
	//ViewHolder 模式, 效率提高 50%
	static class ViewHolder{
		ImageView f_userIcon;
		TextView f_userName,f_oneRow,f_twoRow,f_threeRow;
	}
}
