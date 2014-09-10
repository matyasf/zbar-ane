package com.blogspot.visualscripts.ane;

import android.util.Log;

import com.adobe.fre.FREContext;
import com.adobe.fre.FREFunction;
import com.adobe.fre.FREObject;

public class SetConfigFunction implements FREFunction {

	public FREObject call(FREContext context, FREObject[] args) {
		if (args == null || args.length != 3) {
			return null;
		}
		try {	
			ZbarContext.getScanner().setConfig(args[0].getAsInt(),args[1].getAsInt(),args[2].getAsInt());
		} catch (Exception e) {
			Log.e("zbar", "setConfig Error: " + e.getMessage() + " : " + e.getClass().getCanonicalName()); 
		} 
		return null;
	}

}