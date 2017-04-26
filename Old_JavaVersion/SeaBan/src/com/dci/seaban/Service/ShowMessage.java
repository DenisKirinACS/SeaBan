package com.dci.seaban.Service;

import java.io.IOException;

import com.dci.seaban.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.widget.Toast;
import android.content.Context;

@SuppressLint("NewApi")
public class ShowMessage {

	protected static String _text;
	

	// this is total crash alert message - "OK" button close this app
	@SuppressWarnings("deprecation")
	public static void ShowCrash(String text) {
		_text = text;
	
		GlobalVar.mainActivity.runOnUiThread(new Runnable() { 
			public void run() {

				for (int i = 0; i < 10; i++)
					Toast.makeText(GlobalVar.mainActivity, "Android version: " + Build.VERSION.RELEASE, Toast.LENGTH_LONG).show();

				String DebugInfo ="<br><br> <b>Debug-infos:</b>"
				 + "<br> OS Version: " + System.getProperty("os.version") + "(" + android.os.Build.VERSION.INCREMENTAL + ")"
				 + "<br> OS API Level: " + android.os.Build.VERSION.SDK_INT 
				 + "<br> Device: " + android.os.Build.DEVICE
				 + "<br> Model (Product): " + android.os.Build.MODEL + " ("+ android.os.Build.PRODUCT + ")";
				
				
				AlertDialog alertDialog = new AlertDialog.Builder(GlobalVar.mainActivity).create();

				alertDialog.setTitle("Houston, we have a problem");
				

				alertDialog.setMessage(Html.fromHtml("We are very sorry, <b>SeaBan</b> is stop running :( <br><br>"
						+ "<b>The reson: </b><br><i>" 				
						+ ShowMessage._text 
						+ "</i>" 
						+ DebugInfo));

				alertDialog.setIcon(R.drawable.ic_launcher);

				alertDialog.setButton("Close", new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						System.exit(1);
					}
				});

				alertDialog.show();
			}
		});

	}

	public static void ShowToast(String text) {

		_text = text;
		GlobalVar.mainActivity.runOnUiThread(new Runnable() {
			public void run() {
				for (int i = 0; i < 10; i++)
					Toast.makeText(GlobalVar.mainActivity, ShowMessage._text, Toast.LENGTH_LONG).show();
			}
		});
	}

}