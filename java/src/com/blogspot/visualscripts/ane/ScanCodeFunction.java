package com.blogspot.visualscripts.ane;

import android.content.Intent;
import android.util.Log;

import com.adobe.fre.FREContext;
import com.adobe.fre.FREFunction;
import com.adobe.fre.FREObject;
import com.blogspot.visualscripts.activity.LaunchActivity;

public class ScanCodeFunction implements FREFunction {

	public static FREContext scanCodeContext;
	
	public FREObject call(FREContext context, FREObject[] arg1) {
		try {
			scanCodeContext = context;
			Intent in = new Intent(context.getActivity(), LaunchActivity.class);
			context.getActivity().startActivity(in);
		} catch (Exception e) {
			 Log.e("zbar", "ERROR"+e.toString());
			for (StackTraceElement ste : e.getStackTrace()) {
				 Log.e("zbar", ste.toString());
			}
		}
		return null;
	}
}
