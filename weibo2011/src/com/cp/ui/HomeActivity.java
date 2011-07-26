package com.cp.ui;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import weibo4j.Comment;
import weibo4j.Count;
import weibo4j.Paging;
import weibo4j.Status;
import weibo4j.Weibo;
import weibo4j.WeiboException;

import com.cp.logic.MainService;
import com.cp.logic.Task;
import com.cp.util.DateHelper;
import com.cp.util.NetUtil;
import com.cp.util.WeiboHelper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;

public class HomeActivity extends Activity implements IWeibo,Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	ListView lv;
	View progressView,titleBtnView;
	ImageButton weibo_btn,refresh_btn;
	ProgressBar pb;
	TextView more_tv;
	EditText weiboInput,commentInput;
	AlertDialog weibo_ad;
	Status sta;
	List<Status> all;
	List<Count> counts;
	List<Comment> comments;
//	Weibo weibo=WeiboHelper.getWeibo();
	Weibo weibo=WeiboHelper.getWeibo(MainService.Accesstoken,MainService.Access_token_secret);
	long l;
	private int lastItem=0;
	String sid,userName,weibo_content;
	private int pageSize=5;//每页条数
	private int page=1;//页码
	public static final int DATA_GETOK=2;//首页信息获取成功
	public static final int DATA_GETERROR=3;//首页信息获取失败
	public static final int REFRESH_ICON=4;//用户头像获取成功
	public static final int GET_COMMENTS=5;//获取评论列表
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);
		init();
		lv =(ListView) findViewById(R.id.home_list);
		progressView =findViewById(R.id.include_progress);
		titleBtnView=findViewById(R.id.include_btn_title);
		weibo_btn =(ImageButton) titleBtnView.findViewById(R.id.weibo_btn);
		refresh_btn	=(ImageButton) titleBtnView.findViewById(R.id.refresh_btn);
		MainService.allActivity.add(this);
		registerForContextMenu(lv);
		refresh_btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				progressView.setVisibility(View.VISIBLE);	
				init();
			}
		});
		weibo_btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				View view=LayoutInflater.from(HomeActivity.this).inflate(R.layout.publish_weibo, null);
				weiboInput=(EditText) view.findViewById(R.id.publishweibo_content);
				AlertDialog.Builder builder=new AlertDialog.Builder(HomeActivity.this);
				builder.setTitle("发布微博").setView(view).setPositiveButton("OK", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
//						Weibo weibo=WeiboHelper.getWeibo();
			    		try {
							weibo.updateStatus(weiboInput.getText().toString());
						} catch (WeiboException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						init();
						Toast.makeText(HomeActivity.this, "发布成功..", 1000).show();
					}
				}).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						weibo_ad.dismiss();
					}
				});
				weibo_ad=builder.create();
				weibo_ad.show();
				
			}
		});
		lv.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				sta=(Status)lv.getAdapter().getItem(position);
				return false;
			}
		});
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode==event.KEYCODE_BACK){
			MainService.promtExit(this);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		menu.add(1, 1, 0, "设置").setIcon(R.drawable.setting);
		menu.add(1, 2, 1, "我的资料").setIcon(R.drawable.switchuser);
		menu.add(1, 3, 2, "我的微博").setIcon(R.drawable.officialweibo);
		menu.add(2, 4, 3, "新浪官网").setIcon(R.drawable.comment);
		menu.add(2, 5, 4, "关于").setIcon(R.drawable.aboutweibo);
		menu.add(2, 6, 5, "退出").setIcon(R.drawable.menu_exit);
		return super.onCreateOptionsMenu(menu);
		
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case 1:
			
			break;
		case 2:
			Intent i=new Intent(this,BaisicMessageActivity.class);
			startActivity(i);
			break;
		case 3:
			Intent intent=new Intent(this,WeiboActivity.class);
			startActivity(intent);
			break;
		case 4:
			Intent in=new Intent(this,SinaWebViewActivity.class);
			startActivity(in);
			break;
		case 5:
	
			break;
		case 6:
			MainService.promtExit(this);
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		menu.add(0, 1, 1, "单条信息页面");
		menu.add(0, 2, 2, "收藏");
		menu.add(0, 3, 3, "转发");
		menu.add(0, 4, 4, "评论");
		menu.add(0, 5, 5, "发私信");
		super.onCreateContextMenu(menu, v, menuInfo);
	}
	
	@Override
	protected void onDestroy() {
		Toast.makeText(HomeActivity.this, "Exit progress,end Service..", 1000).show();
		super.onDestroy();
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info=(AdapterContextMenuInfo) item.getMenuInfo();
		 final int c_pageSize=5;//每页条数
		 int c_page=1;//页码
		switch (item.getItemId()) {
		case 1:
			Intent intent=new Intent(this,OneselfActivity.class);
			Bundle b3=new Bundle();
			b3.putSerializable("sta", sta);
			intent.putExtras(b3);
			startActivity(intent);
			break;
		case 2:			
			Log.i("HomeActivity", "onContextItemSelected....");
			
			Intent i=new Intent(this,CommentActivity.class);
			
			i.putExtra("sid", sid);
			startActivity(i);
	
			break;
		case 3:
			Intent ii=new Intent(this,RepostStatusActivity.class);
			Bundle b1=new Bundle();
			b1.putSerializable("sta", sta);
			ii.putExtras(b1);
			startActivity(ii);
			break;
		case 4:
			HashMap param=new HashMap();
			param.put("sta", sta);
			MainService.allTask.add(new Task(Task.COMMENT_LIST,param));
			break;
			
		case 5://发私信
			Intent in=new Intent(this,SendPrivateMessageActivity.class);
			Bundle b2=new Bundle();
			b2.putSerializable("sta", sta);
			in.putExtras(b2);
			startActivity(in);
			break;
		default:
			break;
		}
		return super.onContextItemSelected(item);
	}
	@Override
	public void init() {
		HashMap map =new HashMap();
		map.put("page", page);
		map.put("pageSize", pageSize);
		MainService.allTask.add(new Task(Task.HOME_MESSAGE,map));
	}

	@Override
	public void refresh(Object... param) {
		switch (((Integer)(param[0])).intValue()) {
		case DATA_GETOK:
			if(page==1){
				progressView.setVisibility(View.GONE);
				titleBtnView.setVisibility(View.VISIBLE);	
				Toast.makeText(this, "信息获取成功", 1000).show();
				all=(List<Status>) param[1];
				
				lv.setAdapter(new ListViewAdapter(this,all));
				
			}else {
				ListViewAdapter adapter=new ListViewAdapter(this,(List<Status>)param[1]);
				adapter.notifyDataSetChanged();
			}
			
			break;
		case DATA_GETERROR://信息获取失败
			Toast.makeText(this, "信息获取失败", 1000).show();
			break;
		case REFRESH_ICON:
			((ListViewAdapter)lv.getAdapter()).notifyDataSetChanged();
			break;
			
		case GET_COMMENTS:
			Map map=(Map) param[1];
			Intent it=new Intent(HomeActivity.this,CommentListActivity.class);
			Bundle b=new Bundle();
			b.putSerializable("map", (Serializable) map);
			it.putExtras(b);
			startActivity(it);
			break;
		default:
			break;
		}
	}
	
	public class  ListViewAdapter extends BaseAdapter{
		Context context;
		List<Status> allStatus;
		LayoutInflater layoutInflater;
		public ListViewAdapter(Context context,List<Status> allStatus){
			this.context=context;
			this.allStatus=allStatus;
			layoutInflater=LayoutInflater.from(context);
		}
		@Override
		public int getCount() {
			return allStatus.size();
		}

		@Override
		public Object getItem(int position) {
	
			return allStatus.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}
		
		public void addMore(List<Status> moreStatus){
			allStatus.addAll(moreStatus);
			this.notifyDataSetChanged();
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
				ViewHolder holder;
				if(convertView==null){
					holder=new ViewHolder();
					convertView=layoutInflater.inflate(R.layout.home_items, null);
					holder.userName=(TextView) convertView.findViewById(R.id.home_userName);
					holder.weiboOne=(TextView) convertView.findViewById(R.id.home_contentOne);
					holder.weiboTwo=(TextView) convertView.findViewById(R.id.home_contentTwo);
					holder.image=(ImageView) convertView.findViewById(R.id.home_pic);
					holder.row_three=(TextView) convertView.findViewById(R.id.home_row_three);
					holder.home_row_four=(ImageView) convertView.findViewById(R.id.home_row_four);
					convertView.setTag(holder);
				}else{
					holder=(ViewHolder) convertView.getTag();
				}
				Status s=allStatus.get(position);
				try {
					counts=weibo.getCounts(String.valueOf(s.getId()));
					
				} catch (WeiboException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
				
				BitmapDrawable b=NetUtil.getPicFromUrl(s.getThumbnail_pic());
				for(Count c:counts){
					String h=String.valueOf(c.getComments());
					String m=String.valueOf(c.getRt());
					Date status_date=s.getCreatedAt();
					String time_distance=DateHelper.showTimeDistance(status_date);
					String sourceTxt=s.getSource();
					int i=sourceTxt.indexOf(">");
					int j=sourceTxt.lastIndexOf("<");
					String source=sourceTxt.substring(i+1, j);
					holder.userName.setText(s.getUser().getName());
					holder.weiboOne.setText(s.getText());
					holder.weiboTwo.setText("");
					holder.home_row_four.setImageDrawable(b);
					holder.image.setImageDrawable(MainService.allUserIcon.get(s.getUser().getId()));	
					holder.row_three.setText(time_distance+"    来源： "+source+"    评论："+h+"    转发:"+m);
				}
				return convertView;
			}
	}
	
	static class ViewHolder{
		TextView userName,weiboOne,weiboTwo,row_three;
		ImageView image,home_row_four;
	}
	
}