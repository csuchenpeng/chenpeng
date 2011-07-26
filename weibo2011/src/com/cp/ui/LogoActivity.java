package com.cp.ui;


import com.cp.db.DatabaseHelper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.Button;
import android.widget.ImageView;

public class LogoActivity extends Activity{
	

	ImageView logoImage;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //取消标题
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //取消状态栏,并设置为全屏
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
        		WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.logo);  
        logoImage=(ImageView) findViewById(R.id.logoImage);
        Animation alpha=new AlphaAnimation(0.1f,10.0f);
        alpha.setDuration(3000);
        logoImage.startAnimation(alpha);
        alpha.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(LogoActivity.this,LoginActivity.class);
				startActivity(intent);
				finish();
			}
		});
    }
}
