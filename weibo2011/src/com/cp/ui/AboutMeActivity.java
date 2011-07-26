package com.cp.ui;

import java.util.List;

import weibo4j.Status;
import weibo4j.Weibo;
import weibo4j.WeiboException;

import com.cp.logic.MainService;
import com.cp.util.DateHelper;
import com.cp.util.NetUtil;
import com.cp.util.SourceHelper;
import com.cp.util.WeiboHelper;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class AboutMeActivity extends Activity implements IWeibo{
	
//	Weibo weibo=WeiboHelper.getWeibo();
	Weibo weibo=WeiboHelper.getWeibo(MainService.Accesstoken,MainService.Access_token_secret);
	List<Status> status_list;
	ListView about_me_listView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about_me);
		about_me_listView=(ListView) findViewById(R.id.about_me_listView);
		init();
	}
	
	public class AboutMeAdapter extends BaseAdapter{
		
		Context context;
		List<Status> status_list;
		public AboutMeAdapter(Context context,List<Status> all){
			this.context=context;
			this.status_list=all;
		}
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return status_list.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return status_list.get(position);
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
			Status s=status_list.get(position);
			if(convertView==null){
				holder=new ViewHolder();
				convertView=LayoutInflater.from(context).inflate(R.layout.about_me_item, null);
				holder.about_me_cbox_btn=(CheckBox) convertView.findViewById(R.id.about_me_cbox_btn);
				holder.about_me_content=(TextView) convertView.findViewById(R.id.about_me_content);
				holder.about_me_userName=(TextView) convertView.findViewById(R.id.about_me_userName);
				holder.about_me_userImage=(ImageView) convertView.findViewById(R.id.about_me_userImage);
				holder.about_me_row_three=	(TextView) convertView.findViewById(R.id.about_me_row_three);
				holder.about_me_thumbnail_pic=(ImageView) convertView.findViewById(R.id.about_me_thumbnail_pic);
				holder.about_me_retweeted_content=(TextView) convertView.findViewById(R.id.about_me_retweeted_content);
				
				BitmapDrawable b=NetUtil.getPicFromUrl(s.getOriginal_pic());	
				BitmapDrawable b1=NetUtil.getPicFromUrl(s.getBmiddle_pic());
				BitmapDrawable b2=NetUtil.getPicFromUrl(s.getThumbnail_pic());
				holder.about_me_content.setText(s.getText());
				holder.about_me_userName.setText(s.getUser().getName());
				holder.about_me_userImage.setImageDrawable(NetUtil.getPicFromUrl(s.getUser().getProfileImageURL()));
				holder.about_me_row_three.setText(DateHelper.showCurrentTime(s.getCreatedAt())+"    À´×Ô£º   "+SourceHelper.getCurrentSource(s.getSource()));
				holder.about_me_thumbnail_pic.setImageDrawable(b);
				holder.about_me_retweeted_content.setText("");
			}else{
				holder=(ViewHolder) convertView.getTag();
			}
			
			convertView.setTag(holder);
			return convertView;
		}
		
	}
	
	
	static class ViewHolder{
		ImageView about_me_userImage,about_me_thumbnail_pic;
		TextView about_me_userName,about_me_content,about_me_row_three,about_me_retweeted_content;
		CheckBox about_me_cbox_btn;
	}
	
	@Override
	public void init() {
		// TODO Auto-generated method stub
		
		try {
			status_list=weibo.getMentions();

		} catch (WeiboException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		about_me_listView.setAdapter(new AboutMeAdapter(this,status_list));
	}

	@Override
	public void refresh(Object... param) {
		// TODO Auto-generated method stub
		
	}
}
