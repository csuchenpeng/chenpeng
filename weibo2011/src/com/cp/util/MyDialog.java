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
		builder.setTitle("����");
		builder.setMessage("��������쳣,�����Ƿ�����...")
		.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent intent=new Intent();
				intent.setAction(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
				context.startActivity(intent);
			}
		})
		.setNegativeButton("ȡ��", null).create().show();
	}
}
