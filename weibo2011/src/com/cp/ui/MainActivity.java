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
	public static final String TAB_HOME="��ҳ";
	public static final String TAB_FRIENDS="��ע";
	public static final String TAB_FOLLOWERS="��˿";
	public static final String TAB_SEARCH="����";
	public static final String TAB_MORE="����";
	public static final String TAB_FAVORITRES="�ղ�";
	public static final String TAB_PRIVATEMESSAGE="˽��";
	public static final String TAB_ABOUT_ME="�ᵽ�ҵ�";
	public static final String TAB_MY_WEIBO="�ҵ�΢��";
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
