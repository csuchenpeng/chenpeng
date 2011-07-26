package com.cp.util;

import android.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

public class MyDialog extends Activity{
	public static void showDialog(final Context context){
		AlertDialog.Builder builder=new AlertDialog.Builder(context);
		builder.setTitle("警告");
		builder.setMessage("网络出现异常,请检查是否连网...")
		.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent intent=new Intent();
				intent.setAction(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
				context.startActivity(intent);
			}
		})
		.setNegativeButton("取消", null).create().show();
	}
}
