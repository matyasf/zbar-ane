package com.blogspot.visualscripts.ane;

import com.adobe.fre.FREContext;
import com.adobe.fre.FREExtension;
/** Entry point for the ANE.
 * If you add external .jar files or layout .xml-s, ADT will faild to compile, since it cant 
 * handle nested .jar files. To solve this, modify the ant script to merge jar files. */
public class InitZbar implements FREExtension {

	ZbarContext myContext;
	
	public FREContext createContext(String arg0) {
		myContext = new ZbarContext();
		return myContext;
	}

	public void dispose() {
	}

	public void initialize() {
	}

}
