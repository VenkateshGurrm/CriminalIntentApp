package com.example.vl.criminalintentapp;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.style.BulletSpan;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;

import java.io.FileOutputStream;
import java.io.IOError;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * Created by vl on 11/17/2015.
 */
public class CrimeCameraFragment extends Fragment {
    private static final  String TAG = "CrimeCameraFragment";
    public static final String FILENAME =  "IMAGE_NAME";

    private Camera mCamera;
    private SurfaceView mSurfaceView;
    private View mProgressContainer;

    @Nullable
    @Override
    @SuppressWarnings("deprecation")
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_crime_camera, container, false);

        mProgressContainer = view.findViewById(R.id.crime_camera_progressContainer);
        mProgressContainer.setVisibility(View.INVISIBLE);

        Button takePicButton = (Button) view.findViewById(R.id.cameraTakeButton);
        takePicButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if (mCamera != null) {
                    mCamera.takePicture(mShutterCallback, null, mJpegCallback);
                }

                /*Display display = ((WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
                int rotation = display.getRotation();

                Log.d("Venky",  rotation + " a ");
                Log.d("Venky", getResources().getConfiguration().orientation +" b ");*/
            }
        });

        mSurfaceView = (SurfaceView) view.findViewById(R.id.crime_camera_surfaceview);
        SurfaceHolder surfaceHolder = mSurfaceView.getHolder();
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        surfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if(mCamera!=null){
                        mCamera.setPreviewDisplay(holder);
                    }
                }catch (IOException e){

                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

                if(mCamera!=null){
                    Camera.Parameters parameters = mCamera.getParameters();
                   // Camera.Size s =null;
                    Camera.Size s = getBestSupportedSize(parameters.getSupportedPreviewSizes());
                    parameters.setPreviewSize(s.width, s.height);

                    s = getBestSupportedSize(parameters.getSupportedPictureSizes());
                    parameters.setPictureSize(s.width, s.height);

                    try {
                        mCamera.startPreview();
                    } catch (Exception e) {
                        Log.e(TAG, "Could not start preview", e);
                        mCamera.release();
                        mCamera = null;
                    }
                }
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                if (mCamera != null) {
                    mCamera.stopPreview();
                }
            }
        });

        return view;
    }

    private Camera.ShutterCallback mShutterCallback = new Camera.ShutterCallback() {
        @Override
        public void onShutter() {
            mProgressContainer.setVisibility(View.VISIBLE);
        }
    };

    private Camera.PictureCallback mJpegCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {

            Log.i(TAG, " mJpegCallBack");

            String fileName = UUID.randomUUID().toString() +".jpg";

            FileOutputStream fileOutputStream = null;
            boolean success = true;
            try{
                fileOutputStream = getActivity().openFileOutput(fileName, Context.MODE_PRIVATE);
                fileOutputStream.write(data);

            }catch (Exception e){
                success = false;
            } finally {
                try{
                  if(fileOutputStream != null)
                      fileOutputStream.close();
                }catch (Exception e){
                    success = false;
                }
            }

            if (success) {
                Log.i(TAG, "JPEG saved at " + fileName);
            }

            if (success) {
                Intent i = new Intent();
                i.putExtra(FILENAME, fileName);
                getActivity().setResult(Activity.RESULT_OK, i);
            } else {
                getActivity().setResult(Activity.RESULT_CANCELED);
            }

            getActivity().finish();

        }
    };


    @TargetApi(9)
    @Override
    public void onResume() {
        super.onResume();

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD){
            mCamera = Camera.open(0);
        }else{
            mCamera = Camera.open();
        }

    }

    @Override
    public void onPause() {
        super.onPause();

        if(mCamera!=null){
            mCamera.release();
            mCamera = null;
        }
    }

    private Camera.Size getBestSupportedSize(List<Camera.Size> sizes ){
        Camera.Size bestSize = sizes.get(0);
        int largestArea = bestSize.width * bestSize.height;
        for (Camera.Size s : sizes) {
        int area = s.width * s.height;
        if (area > largestArea) {
            bestSize = s;
            largestArea = area;
        }
        }
    return bestSize;
   }
}
