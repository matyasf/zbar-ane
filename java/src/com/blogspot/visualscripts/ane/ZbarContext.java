package com.blogspot.visualscripts.ane;

import java.util.HashMap;
import java.util.Map;

import net.sourceforge.zbar.Config;
import net.sourceforge.zbar.ImageScanner;

import com.adobe.fre.FREContext;
import com.adobe.fre.FREFunction;

public class ZbarContext extends FREContext {

	@Override
	public void dispose() {
		scanner.destroy();
		scanner = null;
	}

	@Override
	public Map<String, FREFunction> getFunctions() {
		 Map<String, FREFunction> functions = new HashMap<String, FREFunction>();
		 functions.put("scan", new ScanCodeFunction());
		 functions.put("setConfig", new SetConfigFunction());
		 return functions;
	}
	
	private static ImageScanner scanner;
	
	public static ImageScanner getScanner() {
		if (scanner == null)
		{
			scanner = new ImageScanner();
			scanner.setConfig(0, Config.X_DENSITY, 3);
			scanner.setConfig(0, Config.Y_DENSITY, 3);
			scanner.setConfig(0, Config.ENABLE, 1);
		}
		return scanner;
	}
	
	// just for testing
	public static void destroyScanner() {
		if (scanner != null)
		{
			scanner.destroy();
			scanner = null;
		}
	}

}
