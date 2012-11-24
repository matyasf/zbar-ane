/*
To compile this project with Flash builder:
- Import this project to Flash builder
- Edit the FLEX_HOME property in build.xml, so it points to your Flex SDK.
- Right click the build.xml file, then run As -> Ant build
Finally clean the project, that uses this .ANE, so it notices the changes.

The Java project can be edited in Ecplise with Android developer tools installed
(make sure that you compile it as a library project, so it generates a .jar file.).

License:
No license. You do what you want with it. I'd be happy if you send me a message, if you found it useful.
This project uses the Zbar library from http://zbar.sourceforge.net/
The iOS part of the ANE is from http://code.google.com/p/qr-zbar-ane/
See the above mentioned sites for their own licenses.
*/
package com.blogspot.visualscripts.zbar{
	
import flash.events.StatusEvent;
import flash.external.ExtensionContext;
import flash.system.Capabilities;
import flash.system.System;

/** Barcode/QR code reader AIR native extension, that uses the ZBar library. 
 *  It uses Zbar 0.2 for Android and Zbar from http://code.google.com/p/qr-zbar-ane/ for iOS. 
 *  Usage:<br/>
 *  <code>
 *  var zb:Zbar = new Zbar();<br/>
 *	zb.scanCode( onQRCode );<br/>
 *	.....<br/>
 *	private function onQRCode(statusCode:String,decodedTxt:String):void{<br/>
 * 		// do something with the result<br/>
 *	}<br/>
 * </code>
 * Your app.xml file needs to have added this to the Android manifestAdditions:<br/>
 * <code>
 *	&lt;uses-permission android:name="android.permission.CAMERA" /&gt; <br/>
 *	&lt;uses-feature android:name="android.hardware.camera" android:required="false"/&gt; <br/>
 *	&lt;uses-feature android:name="android.hardware.camera.autofocus" android:required="false"/&gt; <br/>
 *	&lt;application&gt;<br/>
 *	    &lt;activity android:name="com.blogspot.visualscripts.activity.LaunchActivity" /&gt;<br/>
 *	    &lt;activity android:name="com.blogspot.visualscripts.activity.CodeReaderActivity" /&gt;<br/>
 *	&lt/application&gt;<br/>
 * </code>
 * Limitations:<br/>
 * Does NOT support Android devices with only a front-facing camera e.g. Nexus 7.
 * (This would raise the minimum requiement for the app to Android 2.3)<br/>
 **/
public class Zbar {

	/** Scan completed without error */
	public static var SCANNED:String = "SCANNED";
	/** (Android only) Could not open camera. e.g. there is no back camera or its used by other app */
	public static var STATUS_CAMERA_ERROR:String = "CAMERA_ERROR";
	/** (Android only) User clicked the exit button */
	public static var STATUS_ABORTED:String = "ABORTED";
	/** Unknown error. */
	public static var STATUS_UNKNOWN_ERROR:String = "UNKNOWN_ERROR";

	private var returnFunc:Function;	
	private var extContext:ExtensionContext;

	public function Zbar() {
		try {
			extContext = ExtensionContext.createExtensionContext("com.blogspot.visualscripts.zbarane",null);
		} catch (e:Error) {
			trace("ZBAR::ERROR: this native extension is not supported on this platform.");
		}
	}
	/** 
	 * Start the scanning UI, that automatically scans for codes.
	 * The return function should look like:<br/>
	 * <code>function onResult(statusCode:String,decodedTxt:String)</code><br/>
	 * <code>statusCode</code> will be one of the constants defined in this class.<br/>
	 * on ANDROID it functions as:<br/>
	 * The ANE calls the result function on successfull scan, or if scanning was aborted/some error occured.
	 * The first parameter will be one of the constant strings defined in this app.<br/>
	 * on iOS:<br/>
	 * The ANE calls the result function on successfull scan. If the scan was aborted, the result function is <b>not</b> called.
	 **/
	public function scanCode(_returnFunc:Function):void {
		returnFunc = _returnFunc;
		try {
			extContext.addEventListener(StatusEvent.STATUS,onResult);
			extContext.call( "scan" );
		} catch (e:Error) {
			extContext.dispose();
			returnFunc(STATUS_UNKNOWN_ERROR,"ZBAR::ERROR:failed to call native library.");
		}
	}
	
	private function onResult(e:StatusEvent):void {
		extContext.removeEventListener(StatusEvent.STATUS,onResult);
		returnFunc(e.code, e.level);
		extContext.dispose();
	}
	
}
}