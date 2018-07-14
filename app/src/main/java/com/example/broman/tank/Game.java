package com.example.broman.tank;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Game extends Activity implements View.OnTouchListener {
    private GLSurfaceView glSurfaceView;

    private final float _TOUCH_SENSITIVITY = 0.25f;
    private final float _ANGLE_SPAN = 90.0f;
    private float _dxFiltered = 0.0f;
    private float _zAngle = 0.0f;
    private float _filterSensitivity = 0.1f;
    private float _zAngleFiltered = 0.0f;
    private int _width;
    private float _touchedX;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!supportES2()){
            Toast.makeText(this, "OpenGl ES 2.0 is not supported", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        glSurfaceView = new GLSurfaceView(this);
        glSurfaceView.setEGLContextClientVersion(2);
        glSurfaceView.setRenderer(new OpenGLRenderer());
        glSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        setContentView(glSurfaceView);

        RelativeLayout rl = new RelativeLayout(this);
        rl.setOnTouchListener(this);
        RelativeLayout.LayoutParams rllp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
        addContentView(rl, rllp);
        getDeviceWidth();
    }

    public void getDeviceWidth() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        if (width > height) {
            _width = width;
        } else {
            _width = height;
        }
    }

    @Override
    protected void onPause(){
        super.onPause();
        glSurfaceView.onPause();
        if (isFinishing()) {
            // save high scores etc
            OpenGLRenderer.setZAngle(0);
            _dxFiltered = 0.0f;
            _zAngle = 0.0f;
            _zAngleFiltered = 0.0f;
            this.finish();
        }
    }

    @Override
    protected void onResume(){
        super.onResume();
        glSurfaceView.onResume();
    }

    private boolean supportES2() {
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();
        return (configurationInfo.reqGlEsVersion >= 0x20000);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            _touchedX = event.getX();
        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            float touchedX = event.getX();
            float dx = Math.abs(_touchedX - touchedX);
            _dxFiltered = _dxFiltered * (1.0f - _filterSensitivity) + dx
                    * _filterSensitivity;

            if (touchedX < _touchedX) {
                _zAngle = (2 * _dxFiltered / _width) * _TOUCH_SENSITIVITY
                        * _ANGLE_SPAN;
                _zAngleFiltered = _zAngleFiltered * (1.0f - _filterSensitivity)
                        + _zAngle * _filterSensitivity;
                OpenGLRenderer.setZAngle(OpenGLRenderer.getZAngle()
                        + _zAngleFiltered);
                glSurfaceView.requestRender();
            } else {
                _zAngle = (2 * _dxFiltered / _width) * _TOUCH_SENSITIVITY
                        * _ANGLE_SPAN;
                _zAngleFiltered = _zAngleFiltered * (1.0f - _filterSensitivity)
                        + _zAngle * _filterSensitivity;
                OpenGLRenderer.setZAngle(OpenGLRenderer.getZAngle()
                        - _zAngleFiltered);
            }
        }
        return true;
    }
}
