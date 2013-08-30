package com.yugy.videorecordtest;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.Size;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.yugy.videorecordtest.widget.RecordButton;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class VideoRecordActivity extends Activity implements Callback, OnClickListener, OnTouchListener{

    private File path = new File(Environment.getExternalStorageDirectory() + "/video.mp4");

    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private MediaRecorder mediaRecorder;
    private Camera camera;
    private RecordButton record;
    private ImageButton back;
    private ImageButton ok;
    private ImageView swap;
    private TextView time;

    private CountDownTimer countDownTimer;

    private boolean recording = false;
    private int currentCameraId = 0;
    private int orientation = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_record);

        initViews();

    }

    @SuppressLint("NewApi")
    @SuppressWarnings("deprecation")
    private void initViews(){
        surfaceView = (SurfaceView)findViewById(R.id.videorecord_surfaceview);
        surfaceHolder= surfaceView.getHolder();
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        surfaceHolder.addCallback(this);

        checkCamera();
        camera = Camera.open();
        camera.setDisplayOrientation(90);

        CameraInfo cameraInfo = new CameraInfo();
        Camera.getCameraInfo(currentCameraId, cameraInfo);
        orientation = cameraInfo.orientation - 90;

        mediaRecorder = new MediaRecorder();

        record = (RecordButton)findViewById(R.id.videorecord_record);
        swap = (ImageView)findViewById(R.id.videorecord_swap);
        back = (ImageButton)findViewById(R.id.videorecord_back);
        ok = (ImageButton)findViewById(R.id.videorecord_ok);
        record.setOnClickListener(this);
        swap.setOnClickListener(this);
        back.setOnClickListener(this);
        ok.setOnClickListener(this);
        swap.setOnTouchListener(this);
        time = (TextView)findViewById(R.id.videorecord_time);

        countDownTimer = new CountDownTimer(15000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                // TODO Auto-generated method stub
                time.setText(15 - millisUntilFinished / 1000 + "″/15″");
                record.twinkle();
            }

            @Override
            public void onFinish() {
                // TODO Auto-generated method stub
                time.setText("0″/15″");
                mediaRecorder.stop();
                record.setBright();
                recording = false;
            }
        };
    }

    private void initMediaRecorder(){
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.DEFAULT);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
        mediaRecorder.setOutputFile(path.getAbsolutePath());
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
        mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
        mediaRecorder.setOrientationHint(orientation);
//		if(currentCameraId == 1){	//前置摄像头
//			mediaRecorder.setOrientationHint(180);
//		}
        mediaRecorder.setVideoEncodingBitRate(2500000);
        mediaRecorder.setVideoSize(640, 480);
        mediaRecorder.setPreviewDisplay(surfaceHolder.getSurface());
    }

    private void checkCamera(){
        if(Camera.getNumberOfCameras() < 1){
            finish();
        }
    }

    @Override
    public void onClick(View arg0) {
        // TODO Auto-generated method stub
        switch(arg0.getId()){
            case R.id.videorecord_record:
                if(recording){
                    countDownTimer.onFinish();
                    countDownTimer.cancel();
                }else{
                    camera.unlock();
                    mediaRecorder.setCamera(camera);
                    initMediaRecorder();
                    try {
                        mediaRecorder.prepare();
                        mediaRecorder.start();
                        countDownTimer.start();
                    } catch (IllegalStateException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    recording = true;
                }
                break;
            case R.id.videorecord_swap:
                if(!recording){
                    if(Camera.getNumberOfCameras() > 1){
                        camera.release();
                        currentCameraId = currentCameraId == 0 ? 1 : 0;
                        camera = Camera.open(currentCameraId);
                        camera.setDisplayOrientation(90);
                        CameraInfo cameraInfo = new CameraInfo();
                        Camera.getCameraInfo(currentCameraId, cameraInfo);
                        orientation = cameraInfo.orientation - 90;
                        try {
                            camera.setPreviewDisplay(surfaceHolder);
                            camera.startPreview();
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }
                break;
            case R.id.videorecord_back:

                break;
            case R.id.videorecord_ok:

                break;
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
        // TODO Auto-generated method stub

    }

    @Override
    public void surfaceCreated(SurfaceHolder arg0) {
        // TODO Auto-generated method stub
        try {
            camera.setPreviewDisplay(surfaceHolder);
            camera.startPreview();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        camera.release();
        super.onDestroy();
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean onTouch(View arg0, MotionEvent arg1) {
        // TODO Auto-generated method stub
        switch(arg0.getId()){
            case R.id.videorecord_swap:
                switch(arg1.getActionMasked()){
                    case MotionEvent.ACTION_DOWN:
                        swap.setAlpha(128);
                        break;
                    case MotionEvent.ACTION_UP:
                        swap.setAlpha(255);
                        break;
                }
                break;
        }
        return false;
    }

}

