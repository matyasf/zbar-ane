package com.blogspot.visualscripts.ane;

import java.util.HashMap;
import java.util.Map;

import com.adobe.fre.FREContext;
import com.adobe.fre.FREFunction;

public class ZbarContext extends FREContext {

	@Override
	public void dispose() {
	}

	@Override
	public Map<String, FREFunction> getFunctions() {
		 Map<String, FREFunction> functions = new HashMap<String, FREFunction>();
		 functions.put("scan", new ScanCodeFunction());
		 return functions;
	}

}
