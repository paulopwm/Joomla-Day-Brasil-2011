package com.JoomlaDay;

import android.app.ProgressDialog;
import android.content.Context;

public class Dialogs {

	public Dialogs() {
		
	}
	
	public ProgressDialog showDialog(String msg, Context context){
		
		ProgressDialog dialog = new ProgressDialog(context);
		dialog.setTitle(context.getString(R.string.wait));
		dialog.setMessage(msg);
		dialog.setCancelable(false);
		dialog.setIndeterminate(true);
		
		return dialog;
	}
}
