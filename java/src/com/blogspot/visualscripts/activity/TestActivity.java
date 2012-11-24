package com.blogspot.visualscripts.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
/** Testing Activity only for running it outside of AIR. */
public class TestActivity extends Activity {
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	
    	LinearLayout rootView = new LinearLayout(this.getApplicationContext());
 	    rootView.setOrientation(LinearLayout.VERTICAL);
    	setContentView(rootView);
    	
    	Log.i("zbar", "starting test app");
        Intent intent = new Intent(this, LaunchActivity.class);
        startActivity(intent);
    }
    
}