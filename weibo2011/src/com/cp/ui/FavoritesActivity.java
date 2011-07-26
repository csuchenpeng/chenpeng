package com.cp.ui;

import java.util.ArrayList;
import java.util.List;

import weibo4j.DirectMessage;
import weibo4j.Status;
import weibo4j.Weibo;
import weibo4j.WeiboException;

import com.cp.logic.MainService;
import com.cp.util.DateHelper;
import com.cp.util.NetUtil;
import com.cp.util.SourceHelper;
import com.cp.util.WeiboHelper;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class FavoritesActivity extends Activity implements IWeibo{
	
	ListView favorites_listView;
	Weibo weibo=WeiboHelper.getWeibo(MainService.Accesstoken,MainService.Access_token_secret);
	List<Status> list;
	List<Status> temp=new ArrayList<Status>();
	ImageButton favorites_cancel_btn;
	ProgressDialog pd;
	boolean isDelete=true;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.favorites);
		favorites_cancel_btn=(ImageButton) findViewById(R.id.favorites_cancel_btn);
		favorites_listView=(ListView) findViewById(R.id.favorites_listView);
		if(isDelete=false){
			pd.dismiss();
		}
		init();
		
		favorites_cancel_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(isDelete=true){
					if(pd==null){
						pd=new ProgressDialog(FavoritesActivity.this);
					}
					pd.setMessage("删除...");
					pd.setCancelable(true);
					pd.show();
					isDelete=false;
					
					for(Status s:temp){
						try {
							weibo.destroyFavorite(s.getId());
							temp.remove(s);
						} catch (WeiboException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				
					
					Toast.makeText(FavoritesActivity.this, "删除成功!", 1000).show();
					init();
				}
				
				
			}
			
		});
	}
	
	
	public class FavoritesAdapter extends BaseAdapter{
		Context context;
		List<Status> list;
		public FavoritesAdapter(Context context,List<Status> sts){
			this.context=context;
			this.list=sts;
		}
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final int i=position;
			ViewHolder holder;
			final Status sta=list.get(position);
			if(convertView==null){
				holder=new ViewHolder();
				
			}else {
				holder=(ViewHolder) convertView.getTag();
			}
			convertView=LayoutInflater.from(context).inflate(R.layout.favorites_item, null);
			holder.favorites_userImage=(ImageView) convertView.findViewById(R.id.favorites_userImage);
			holder.favorites_userName=(TextView) convertView.findViewById(R.id.favorites_userName);
			holder.favorites_row_two=(TextView) convertView.findViewById(R.id.favorites_row_two);
			holder.favorites_row_three=(TextView) convertView.findViewById(R.id.favorites_row_three);
			holder.cbox=(CheckBox) convertView.findViewById(R.id.favorites_cbox);
			holder.cbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					// TODO Auto-generated method stub
					if(isChecked){
						temp.add(sta);
						favorites_cancel_btn.setEnabled(true);
						Toast.makeText(FavoritesActivity.this, "共"+String.valueOf(temp.size())+"项", 1000).show();
					}else {
						temp.remove(sta);
						Toast.makeText(FavoritesActivity.this, "共"+String.valueOf(temp.size())+"项", 1000).show();
						favorites_cancel_btn.setEnabled(false);
					}
				}
			});
			holder.favorites_userImage.setImageDrawable(NetUtil.getPicFromUrl(sta.getUser().getProfileImageURL()));
			holder.favorites_userName.setText(sta.getUser().getName());
			holder.favorites_row_two.setText(sta.getText());
			holder.favorites_row_three.setText(DateHelper.showTimeDistance(sta.getCreatedAt())+"    来自  ： "+SourceHelper.getCurrentSource(sta.getSource())
						+"");
			convertView.setTag(holder);
			return convertView;
		}
		
	}
	static class ViewHolder{
		TextView favorites_userName,favorites_row_two,favorites_row_three;
		CheckBox cbox;
		ImageView favorites_userImage;
	}

	@Override
	public void init() {
		if(isDelete=false){
			pd.dismiss();
//			isDelete=true;
		}
		try {
//			List<DirectMessage> dm=(List<DirectMessage>) weibo.getDirectMessages();
//			for(DirectMessage d:dm){
//				d.g
//			}
			list=weibo.getFavorites();
		} catch (WeiboException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		favorites_listView.setAdapter(new FavoritesAdapter(this,list));
		if(temp.size()>0){
			favorites_cancel_btn.setEnabled(true);
		}else {
			favorites_cancel_btn.setEnabled(false);
		}
		
		
	}


	@Override
	public void refresh(Object... param) {
		// TODO Auto-generated method stub
		
	}
}
