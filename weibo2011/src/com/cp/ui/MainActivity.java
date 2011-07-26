package com.cp.ui;

import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Window;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class MainActivity extends TabActivity{
	RadioGroup group;
	TabHost th;
	public static final String TAB_HOME="首页";
	public static final String TAB_FRIENDS="关注";
	public static final String TAB_FOLLOWERS="粉丝";
	public static final String TAB_SEARCH="搜索";
	public static final String TAB_MORE="更多";
	public static final String TAB_FAVORITRES="收藏";
	public static final String TAB_PRIVATEMESSAGE="私信";
	public static final String TAB_ABOUT_ME="提到我的";
	public static final String TAB_MY_WEIBO="我的微博";
	@Override
	protected void onCreate(Bundle savedInstanceState) {	
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);
		th=getTabHost();
		th.addTab(th.newTabSpec(TAB_HOME).setIndicator(TAB_HOME).setContent(new Intent(this,HomeActivity.class)));
		th.addTab(th.newTabSpec(TAB_FRIENDS).setIndicator(TAB_FRIENDS).setContent(new Intent(this,FriendsActivity.class)));
		th.addTab(th.newTabSpec(TAB_FOLLOWERS).setIndicator(TAB_FOLLOWERS).setContent(new Intent(this,FollowersActivity.class)));
		th.addTab(th.newTabSpec(TAB_FAVORITRES).setIndicator(TAB_FAVORITRES).setContent(new Intent(this,FavoritesActivity.class)));
		th.addTab(th.newTabSpec(TAB_PRIVATEMESSAGE).setIndicator(TAB_PRIVATEMESSAGE).setContent(new Intent(this,PrivateMessageActivity.class)));
		th.addTab(th.newTabSpec(TAB_ABOUT_ME).setIndicator(TAB_ABOUT_ME).setContent(new Intent(this,AboutMeActivity.class)));
		th.addTab(th.newTabSpec(TAB_MY_WEIBO).setIndicator(TAB_MY_WEIBO).setContent(new Intent(this,WeiboActivity.class)));
		group=(RadioGroup) findViewById(R.id.main_group);
		group.setOnCheckedChangeListener(new OnCheckedChangeListener(){

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.radio_btn1:
					th.setCurrentTabByTag(TAB_HOME);
					break;
				case R.id.radio_btn2:
					th.setCurrentTabByTag(TAB_FRIENDS);
					break;
				case R.id.radio_btn3:
					th.setCurrentTabByTag(TAB_FOLLOWERS);
					break;
				case R.id.radio_btn4:
					th.setCurrentTabByTag(TAB_PRIVATEMESSAGE);
					break;
				case R.id.radio_btn5:
					th.setCurrentTabByTag(TAB_FAVORITRES);
					break;
				case R.id.radio_btn6:
					th.setCurrentTabByTag(TAB_ABOUT_ME);
				default:
					break;
				}
			}
			
		});
		
	}
}
