package org.adaptlab.chpir.android.survey;

import java.io.IOException;
import java.util.List;

import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class CameraFragment extends Fragment {
    private static final String TAG = "CameraFragment";
    private View mProgressIndicator;
	private Camera mCamera;
    private SurfaceView mSurfaceView;
    
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_camera, parent, false);
		mProgressIndicator = v.findViewById(R.id.camera_progressIndicator);
        mProgressIndicator.setVisibility(View.INVISIBLE);
        Button takePictureButton = (Button) v.findViewById(R.id.camera_takePictureButton);
        takePictureButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				getActivity().finish();
			}
		});
        
        mSurfaceView = (SurfaceView)v.findViewById(R.id.camera_surfaceView);
        SurfaceHolder holder = mSurfaceView.getHolder();
		holder.addCallback(new SurfaceHolder.Callback() {
			public void surfaceCreated(SurfaceHolder holder) {
				try {
					if (mCamera != null) {
						mCamera.setPreviewDisplay(holder);
					}
				} catch(IOException e) {
					Log.e(TAG, "Error setting up surface preview display", e);
				}
			}
			
			public void surfaceDestroyed(SurfaceHolder holder) {
				if (mCamera != null) {
					mCamera.stopPreview();
				}
			}
			
			public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
				if (mCamera == null) {
					return;
				}
				Camera.Parameters parameters = mCamera.getParameters();
                Size s = getBestSupportedSize(parameters.getSupportedPreviewSizes(), w, h);
                parameters.setPreviewSize(s.width, s.height);
                mCamera.setParameters(parameters);
                try {
                	mCamera.startPreview();
                } catch (Exception e) {
                	Log.e(TAG, "Unable to start preview",e);
                }
			}	
		});
        
		return v;
	}
	
	private Size getBestSupportedSize(List<Size> sizes, int width, int height) {
		final double ASPECT_TOLERANCE = 0.1;
        double targetRatio = (double) width / height;
        if (sizes == null) return null;
 
        Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;
        int targetHeight = height;
 
        for (Size size : sizes) {
            double ratio = (double) size.width / size.height;
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) continue;
            if (Math.abs(size.height - targetHeight) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - targetHeight);
            }
        }
 
        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Size size : sizes) {
                if (Math.abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }
        }
        return optimalSize;
    }
	
	@Override
	public void onResume() {
		super.onResume();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {    
            mCamera = Camera.open(0);
        } else {
            mCamera = Camera.open();
        } //TODO Check what's the case for a 2-camera device
	}
	
	@Override
    public void onPause() {
        super.onPause();
        if (mCamera != null) {
            mCamera.release();
            mCamera = null;
        }
    }
	
}
