package com.cp.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.widget.Toast;

public class MyToast extends Activity{
	public static void showToast(Context context){
		Toast.makeText(context, "�Բ���,�û��������������...", 1000).show();
	}
}
