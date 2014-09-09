package com.blogspot.visualscripts.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

/** Testing Activity only for running it outside of AIR. */
public class TestActivity extends Activity {

	TextView txt;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Log.i("zbar", "starting test app");
		
		LinearLayout rootView = new LinearLayout(this.getApplicationContext());
		rootView.setOrientation(LinearLayout.VERTICAL);
		setContentView(rootView);

		txt = new TextView(this);
		txt.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT));
		txt.setText("Zbar test app");
		rootView.addView(txt);
		
        if (savedInstanceState == null){
			Intent intent = new Intent(this, LaunchActivity.class);
			startActivityForResult(intent, 123);
        }
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.i("zbar", "app finised, all done.");
		txt.setText("Zbar test app results: Status: "
				+ data.getStringExtra("ACTIVITY_RESULT") + " data: "
				+ data.getStringExtra("SCAN_RESULT"));
		super.onActivityResult(requestCode, resultCode, data);
	}

}