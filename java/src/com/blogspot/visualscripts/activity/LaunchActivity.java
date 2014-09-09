package com.blogspot.visualscripts.activity;

import net.sourceforge.zbar.Config;
import net.sourceforge.zbar.Image;
import net.sourceforge.zbar.ImageScanner;
import net.sourceforge.zbar.Symbol;
import net.sourceforge.zbar.SymbolSet;
import net.sourceforge.zbar.android.CameraTest.CameraPreview;
import android.app.Activity;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blogspot.visualscripts.ane.ScanCodeFunction;
/** Launches the main Camera activity. This is needed, so it can catch the result of the Activity. */
public class LaunchActivity extends Activity {
	
	private Camera mCamera;
	private CameraPreview mPreview;
	private ImageScanner scanner;
	private String scanResult = "";
	private FrameLayout cameraPreview;
	private Button exitBtn;
	
	static {
		System.loadLibrary("iconv");
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		LinearLayout rootView = new LinearLayout(this);
		rootView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
		rootView.setOrientation(LinearLayout.VERTICAL);
		
		cameraPreview = new FrameLayout(this);
		cameraPreview.setLayoutParams(new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT,
				(float) 1.0));
		rootView.addView(cameraPreview);

		TextView txt = new TextView(this);
		txt.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT));
		txt.setText("Scanning");
		rootView.addView(txt);

		exitBtn = new Button(this);
		exitBtn.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT));
		exitBtn.setGravity(Gravity.CENTER);
		exitBtn.setText("Cancel");
		rootView.addView(exitBtn);

		setContentView(rootView);

		scanner = new ImageScanner();
		scanner.setConfig(0, Config.X_DENSITY, 3);
		scanner.setConfig(0, Config.Y_DENSITY, 3);

		exitBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				endTask("ABORTED", "");
			}
		});

		mPreview = new CameraPreview(this);
		cameraPreview.addView(mPreview);
	}

	public void onPause() {
		super.onPause();
		if (mCamera != null) {
			mCamera = null;
		}
		mPreview.stopCamera();
	}

	public void onResume() {
		super.onResume();
		// try to open the back camera, of not possible then anything else.
		int backupCamera = 0;
		Camera.CameraInfo info = new Camera.CameraInfo();
		for (int i = 0; i < Camera.getNumberOfCameras(); i++) {
			Camera.getCameraInfo(i, info);
			if (info.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
				mCamera = Camera.open(i);
			} else {
				backupCamera = i;
			}
		}
		if (mCamera == null && Camera.getNumberOfCameras() > 0) {
			mCamera = Camera.open(backupCamera);
		}

		if (mCamera == null) {
			Log.e("zbar", "Camera.open() returned null. The device has likely no camera.");
			endTask("CAMERA_ERROR", "");
			return;
		}
		mPreview.setCamera(mCamera, previewCb);
	}

	PreviewCallback previewCb = new PreviewCallback() {
		public void onPreviewFrame(byte[] data, Camera camera) {
			Camera.Parameters parameters = camera.getParameters();
			Size size = parameters.getPreviewSize();

			Image barcode = new Image(size.width, size.height, "Y800");
			barcode.setData(data);
			int result = scanner.scanImage(barcode);

			scanResult = "";
			if (result != 0) {
				SymbolSet syms = scanner.getResults();
				for (Symbol sym : syms) {
					scanResult = scanResult + sym.getData();
				}
				endTask("OK", scanResult);
			}
		}
	};
	
	private void endTask(String status, String result) 
	{
		Log.i("zbar","finished "+ result + " status: " + status );
    	if (ScanCodeFunction.scanCodeContext != null) {
    		ScanCodeFunction.scanCodeContext.dispatchStatusEventAsync(status, result);
    	} else {
    		Intent dat = new Intent();
            dat.putExtra("ACTIVITY_RESULT", status);
            dat.putExtra("SCAN_RESULT", result);
            setResult(RESULT_OK,dat);
    		Log.e("zbar", "No FREContext. This is normal in standalone mode.");
    	}
		finish();
	}
	
}
