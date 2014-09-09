package com.blogspot.visualscripts.activity;

/*
 License:
 No license. You do what you want with it. I'd be happy if you send me a message, 
 if you found it useful.
 This project uses the Zbar library, which has some different license. 
 */
import net.sourceforge.zbar.Config;
import net.sourceforge.zbar.Image;
import net.sourceforge.zbar.ImageScanner;
import net.sourceforge.zbar.Symbol;
import net.sourceforge.zbar.SymbolSet;
import net.sourceforge.zbar.android.CameraTest.CameraPreview;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

/** Main Activity that displays the camera and scans for codes. */
public class CodeReaderActivity extends Activity {
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
		
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
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
				Intent dat = new Intent();
				dat.putExtra("ACTIVITY_RESULT", "ABORTED");
				dat.putExtra("SCAN_RESULT", "");
				setResult(RESULT_OK, dat);
				finish();
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
			Intent dat = new Intent();
			dat.putExtra("ACTIVITY_RESULT", "CAMERA_ERROR");
			dat.putExtra("SCAN_RESULT", "");
			setResult(RESULT_OK, dat);
			finish();
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
				Intent dat = new Intent();
				dat.putExtra("ACTIVITY_RESULT", "OK");
				dat.putExtra("SCAN_RESULT", scanResult);
				setResult(RESULT_OK, dat);
				finish();
			}
		}
	};
}
