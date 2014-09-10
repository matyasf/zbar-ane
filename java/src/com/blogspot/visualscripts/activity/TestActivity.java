package com.blogspot.visualscripts.activity;

import net.sourceforge.zbar.Config;
import net.sourceforge.zbar.Symbol;

import com.blogspot.visualscripts.ane.ZbarContext;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/** Testing Activity only for running it outside of AIR. */
public class TestActivity extends Activity {

	TextView txt;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Log.i("zbar", "starting test app");
		final Activity self = this;
		
		LinearLayout rootView = new LinearLayout(this.getApplicationContext());
		rootView.setOrientation(LinearLayout.VERTICAL);
		setContentView(rootView);

		txt = new TextView(this);
		txt.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT));
		txt.setText("Zbar test app");
		rootView.addView(txt);
		
		Button b1 = new Button(this);
		rootView.addView(b1);
		b1.setGravity(Gravity.CENTER);
		b1.setText("Scan for CODE 128");
		b1.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				ZbarContext.getScanner().setConfig(Symbol.NONE, Config.ENABLE, 0);
				ZbarContext.getScanner().setConfig(Symbol.CODE128, Config.ENABLE, 1);
				Intent intent = new Intent(self, LaunchActivity.class);
				startActivityForResult(intent, 123);
			}
		});
		
		Button b2 = new Button(this);
		rootView.addView(b2);
		b2.setGravity(Gravity.CENTER);
		b2.setText("Scan for QR code");
		b2.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				ZbarContext.getScanner().setConfig(Symbol.NONE, Config.ENABLE, 0);
				ZbarContext.getScanner().setConfig(Symbol.QRCODE, Config.ENABLE, 1);
				Intent intent = new Intent(self, LaunchActivity.class);
				startActivityForResult(intent, 123);
			}
		});
		
		Button b3 = new Button(this);
		rootView.addView(b3);
		b3.setGravity(Gravity.CENTER);
		b3.setText("Scan for everything");
		b3.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				ZbarContext.getScanner().setConfig(Symbol.NONE, Config.ENABLE, 1);
				Intent intent = new Intent(self, LaunchActivity.class);
				startActivityForResult(intent, 123);
			}
		});
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.i("zbar", "app finised, all done.");
		txt.setText("Zbar test app results: Status: "
				+ data.getStringExtra("ACTIVITY_RESULT") + " data: "
				+ data.getStringExtra("SCAN_RESULT"));
		
		ZbarContext.destroyScanner();
		
		super.onActivityResult(requestCode, resultCode, data);
	}

}