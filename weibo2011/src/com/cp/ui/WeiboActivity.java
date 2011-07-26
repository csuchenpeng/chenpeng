package com.cp.ui;

import java.util.List;

import weibo4j.Status;
import weibo4j.User;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class WeiboActivity extends Activity implements IWeibo{
	
//	Weibo weibo=WeiboHelper.getWeibo();
	Weibo weibo=WeiboHelper.getWeibo(MainService.Accesstoken,MainService.Access_token_secret);
	List<Status> weibo_list;
	ListView weibo_listView;
	ImageButton delete_btn;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.weibo);
		weibo_listView=(ListView) findViewById(R.id.weibo_listView);
		delete_btn=(ImageButton) findViewById(R.id.weibo_delete_img_btn);
		init();
	}
	
	public class AboutMeAdapter extends BaseAdapter{
		
		Context context;
		List<Status> weibo_list;
		public AboutMeAdapter(Context context,List<Status> all){
			this.context=context;
			this.weibo_list=all;
		}
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return weibo_list.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return weibo_list.get(position);
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
			Status s=weibo_list.get(position);
			if(convertView==null){
				holder=new ViewHolder();
				convertView=LayoutInflater.from(context).inflate(R.layout.weibo_item, null);
				holder.weibo_cbox_btn=(CheckBox) convertView.findViewById(R.id.weibo_cbox_btn);
				holder.weibo_content=(TextView) convertView.findViewById(R.id.weibo_content);
				holder.weibo_userName=(TextView) convertView.findViewById(R.id.weibo_userName);
				holder.weibo_userImage=(ImageView) convertView.findViewById(R.id.weibo_userImage);
				holder.weibo_row_three=	(TextView) convertView.findViewById(R.id.weibo_row_three);
				holder.weibo_thumbnail_pic=(ImageView) convertView.findViewById(R.id.weibo_thumbnail_pic);
				holder.weibo_retweeted_content=(TextView) convertView.findViewById(R.id.weibo_retweeted_content);
				
				BitmapDrawable b=NetUtil.getPicFromUrl(s.getOriginal_pic());	
				BitmapDrawable b1=NetUtil.getPicFromUrl(s.getBmiddle_pic());
				BitmapDrawable b2=NetUtil.getPicFromUrl(s.getThumbnail_pic());
				holder.weibo_content.setText(s.getText());
				holder.weibo_userName.setText(s.getUser().getName());
				holder.weibo_userImage.setImageDrawable(NetUtil.getPicFromUrl(s.getUser().getProfileImageURL()));
				holder.weibo_row_three.setText(DateHelper.showCurrentTime(s.getCreatedAt())+"    À´×Ô£º   "+SourceHelper.getCurrentSource(s.getSource()));
				holder.weibo_thumbnail_pic.setImageDrawable(b);
				holder.weibo_retweeted_content.setText("");
			}else{
				holder=(ViewHolder) convertView.getTag();
			}
			
			convertView.setTag(holder);
			return convertView;
		}
		
	}
	
	
	static class ViewHolder{
		ImageView weibo_userImage,weibo_thumbnail_pic;
		TextView weibo_userName,weibo_content,weibo_row_three,weibo_retweeted_content;
		CheckBox weibo_cbox_btn;
	}
	
	@Override
	public void init() {
		// TODO Auto-generated method stub
		
		try {
			
			weibo_list=weibo.getUserTimeline();
			
		} catch (WeiboException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		weibo_listView.setAdapter(new AboutMeAdapter(this,weibo_list));
	}

	@Override
	public void refresh(Object... param) {
		// TODO Auto-generated method stub
		
	}
}
