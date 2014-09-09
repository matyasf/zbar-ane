package com.blogspot.visualscripts.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.blogspot.visualscripts.ane.ScanCodeFunction;
/** Launches the main Camera activity. This is needed, so it can catch the result of the Activity. */
public class LaunchActivity extends Activity {
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	
    	View view = new View(this);
    	setContentView(view);
         
        if (savedInstanceState == null){
        	Log.i("zbar", "Launching camera");
        	Intent intent = new Intent(this, CodeReaderActivity.class);
            startActivityForResult(intent, 6666);
        }
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
    	String status = "UNKNOWN_ERROR";
    	String res = "";
    	if (data != null) {
    		String actRes = data.getStringExtra("ACTIVITY_RESULT");
    		if (actRes.equals("OK") == true) {
				res = data.getStringExtra("SCAN_RESULT");
    			status = "SCANNED";
    		} else if (actRes.equals("CAMERA_ERROR") == true) {
    			status = "CAMERA_ERROR";
    		} else if (actRes.equals("ABORTED") == true){
    			status = "ABORTED";
    		}
    	}
    	Log.i("zbar","finiseh "+ res + " status: " + status );
    	if (ScanCodeFunction.scanCodeContext != null) {
    		ScanCodeFunction.scanCodeContext.dispatchStatusEventAsync(status, res);
    	} else {
    		Intent dat = new Intent();
            dat.putExtra("ACTIVITY_RESULT", status);
            dat.putExtra("SCAN_RESULT", res);
            setResult(RESULT_OK,dat);
    		Log.e("zbar", "No FREContext. This is normal in standalone mode.");
    	}
        super.onActivityResult(requestCode, resultCode, data);
        finish();
    }
}
