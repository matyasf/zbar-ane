package net.sourceforge.zbar.android.CameraTest;

import java.util.List;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PreviewCallback;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

public class CameraPreview extends SurfaceView implements
		SurfaceHolder.Callback {

	private SurfaceHolder mHolder;
	private Camera mCamera;
	private boolean previewing = true;
	private Handler autoFocusHandler;
	private PreviewCallback previewCb;

	public CameraPreview(Context context) {
		super(context);

		autoFocusHandler = new Handler();

		mHolder = getHolder();
		mHolder.addCallback(this);
		// deprecated setting, but required on Android versions prior to 3.0
		mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}

	public void setCamera(Camera camera, PreviewCallback _previewCb) {
		mCamera = camera;
		previewCb = _previewCb;
	}

	private void focusCamera() {
		if (previewing) {
			Camera.Parameters parameters = mCamera.getParameters();
			List<String> focusModes = parameters.getSupportedFocusModes();
			if (focusModes.contains("continuous-picture")) {
				mCamera.getParameters().setFocusMode("continuous-picture");
				mCamera.autoFocus(autoFocusCB);
			} else if (focusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
				mCamera.getParameters().setFocusMode(
						Camera.Parameters.FOCUS_MODE_AUTO);
				mCamera.autoFocus(autoFocusCB);
			}
		}
	}

	// Mimic continuous auto-focusing
	AutoFocusCallback autoFocusCB = new AutoFocusCallback() {
		public void onAutoFocus(boolean success, Camera camera) {
			autoFocusHandler.postDelayed(doAutoFocus, 1000);
		}
	};

	private Runnable doAutoFocus = new Runnable() {
		public void run() {
			focusCamera();
		}
	};

	public void surfaceCreated(SurfaceHolder holder) {
		// do nothing here, surfaceChanged will be called soon
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		// Camera preview released in activity
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		if (mHolder.getSurface() == null) {
			// preview surface does not exist
			return;
		}

		try {
			mCamera.stopPreview();
		} catch (Exception e) {
			// ignore: tried to stop a non-existent preview
		}
		
		Display display = ((WindowManager)getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		if (display.getRotation() == Surface.ROTATION_0) {
			mCamera.setDisplayOrientation(90);
		}
		if (display.getRotation() == Surface.ROTATION_270) {
			mCamera.setDisplayOrientation(180);
		}
		
		try {
			mCamera.setPreviewDisplay(mHolder);
			mCamera.startPreview();
			mCamera.setPreviewCallback(previewCb);
			previewing = true;
			focusCamera();
		} catch (Exception e) {
			Log.e("zbar", "Error starting camera preview: " + e.getMessage());
		}
	}

	public void stopCamera() {
		previewing = false;
		autoFocusHandler.removeCallbacksAndMessages(null);
		if (mCamera != null) {
			mCamera.setPreviewCallback(null);
			mCamera.stopPreview();
			mCamera.release();
			mCamera = null;
		}
	}
}
