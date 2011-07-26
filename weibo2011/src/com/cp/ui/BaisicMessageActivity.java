package com.cp.ui;

import weibo4j.User;
import weibo4j.Weibo;
import weibo4j.WeiboException;

import com.cp.logic.MainService;
import com.cp.util.DateHelper;
import com.cp.util.NetUtil;
import com.cp.util.WeiboHelper;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class BaisicMessageActivity extends Activity implements IWeibo{
	
	Weibo weibo=WeiboHelper.getWeibo(MainService.Accesstoken,MainService.Access_token_secret);
	User user;
	ImageView basic_message_userImage;
	TextView basic_message_rowOne,basic_message_rowTwo,basic_message_rowThree,
	basic_message_rowFour,basic_message_rowFive,basic_message_rowSix,basic_message_rowSeven,basic_message_Eight;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.basic_message);
		basic_message_userImage=(ImageView) findViewById(R.id.basic_message_userImage);
		basic_message_rowOne=(TextView) findViewById(R.id.basic_message_rowOne);
		basic_message_rowTwo=(TextView) findViewById(R.id.basic_message_rowTwo);
		basic_message_rowThree=(TextView) findViewById(R.id.basic_message_rowThree);
		basic_message_rowFour=(TextView) findViewById(R.id.basic_message_rowFour);
		basic_message_rowFive=(TextView) findViewById(R.id.basic_message_rowFive);
		basic_message_rowSix=(TextView) findViewById(R.id.basic_message_rowSix);
		basic_message_rowSeven=(TextView) findViewById(R.id.basic_message_rowSeven);
		basic_message_Eight=(TextView) findViewById(R.id.basic_message_rowEight);	
		init();
		basic_message_userImage.setImageDrawable(NetUtil.getPicFromUrl(user.getProfileImageURL()));
		basic_message_rowOne.setText(user.getName());
		basic_message_rowTwo.setText("基本资料 ");
		basic_message_rowThree.setText("关注 ："+String.valueOf(user.getFriendsCount()));
		basic_message_rowFour.setText("粉丝 ：  "+String.valueOf(user.getFollowersCount()));
		basic_message_rowFive.setText("收藏 ：  "+String.valueOf(user.getFavouritesCount()));
		basic_message_rowSix.setText("城市："+user.getLocation());
		basic_message_rowSeven.setText("创建时间 ：  "+DateHelper.showCurrentTime(user.getCreatedAt()));
		basic_message_Eight.setText("个人简介:"+user.getDescription());
	}

	@SuppressWarnings("deprecation")
	@Override
	public void init() {
		// TODO Auto-generated method stub
		try {
		
			 user=weibo.getUserDetail("2081133651");
		} catch (WeiboException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void refresh(Object... param) {
		// TODO Auto-generated method stub
		
	}
}
