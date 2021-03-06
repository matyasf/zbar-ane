/*
License:
No license. You do what you want with it. I'd be happy if you send me a message, if you found it useful.
This project uses the Zbar library from http://zbar.sourceforge.net/
The iOS part of the ANE is from http://code.google.com/p/qr-zbar-ane/
See the above mentioned sites for their own licenses.
*/
package com.blogspot.visualscripts.zbar{
	
import flash.events.StatusEvent;
import flash.external.ExtensionContext;

/** Barcode/QR code reader AIR native extension, that uses the ZBar library. 
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
 * Needs Android 2.3/iOS 5 minimum<br/>
 **/
public class Zbar {

	/** Scan completed without error */
	public static var SCANNED:String = "SCANNED";
	/** (Android only) Could not open camera. e.g. its used by some other app */
	public static var STATUS_CAMERA_ERROR:String = "CAMERA_ERROR";
	/** (Android only) User clicked the cancel button */
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
	 * The ANE calls the result function on successful scan, or if scanning was aborted/some error occurred.
	 * The first parameter will be one of the constant strings defined in this app.<br/>
	 * on iOS:<br/>
	 * The ANE calls the result function on successful scan. If the scan was aborted, the result function is <b>not</b> called.
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

    /**
     * Sets scanning configuration. Disable symbol types to increase speed and accuracy.
     * By default all types of symbols are enabled.
     * Does NOT work on iOS!
     * Example: Disable all checks except QR code:
     *  setConfig(ZbarSymbolType.ALL, ZbarSymbolSetting.ENABLE, 0); // disables everything
     *  setConfig(ZbarSymbolType.QRCODE, ZbarSymbolSetting.ENABLE, 1); // enable QR code
     * @Parameters:
     *  forSymbol: What symbol type should the config set (0 for all)
     *  setting: what to set
     *  value: the setting value (for boolean values 0=false,1=true)
     **/
    public function setConfig(forSymbol:ZbarSymbolType, setting:ZbarSymbolSetting, value:uint):void {
        try {
            extContext.call( "setConfig", forSymbol.symbolType, setting.setting, value );
        } catch (e:Error) {
            trace("Zbar error setting config ", e);
        }
    }
	
	private function onResult(e:StatusEvent):void {
		extContext.removeEventListener(StatusEvent.STATUS,onResult);
		returnFunc(e.code, e.level);
		extContext.dispose();
	}
	
}
}