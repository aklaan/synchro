package com.familledupuis91.gamingtools.components;


import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ConfigurationInfo;
import android.graphics.Color;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class OpenGLActivity extends Activity {

    public static final byte UPDATE_FPS = 1;
    public static float DEFAULT_ZOOM_FACTOR = 2.f;
    // ! OpenGL SurfaceView
    public MySurfaceView mGLSurfaceView;
    private int fpsTv_Id;

    public Handler getFpsHandler() {
        return mFpsHandler;
    }

    private Handler mFpsHandler;

    public void setHandler(Handler mHandler) {
        this.mHandler = mHandler;
    }

    public Handler getHandler() {
        return mHandler;
    }

    private Handler mHandler;

    public MySurfaceView getSurfaceView() {
        return this.mGLSurfaceView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (!isOGLES20Compatible()) {
            // C++ Reflex sorry
            mGLSurfaceView = null;
            showOGLES20ErrorDialogBox();
            return;
        }

        // We don't use Layout. But you can.
        // create an OpenGLView
        mGLSurfaceView = new MySurfaceView(this);

        mGLSurfaceView.setDebugFlags(GLSurfaceView.DEBUG_CHECK_GL_ERROR);
        // Create an OpenGL ES 2.0 context.
        mGLSurfaceView.setEGLContextClientVersion(2);


        //	mGLSurfaceView.setRenderer(new GLES20RendererScene01(this));

        setContentView(mGLSurfaceView);

        //------------------
        TextView tv = new TextView(this);
        fpsTv_Id = View.generateViewId();
        tv.setId(fpsTv_Id);
        tv.setText("...");
        tv.setTextColor(Color.RED);
        addContentView(tv, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        initFpsHandler();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mGLSurfaceView != null) {
            mGLSurfaceView.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mGLSurfaceView != null) {
            mGLSurfaceView.onPause();
        }
    }

    /* This method verify that your Phone is compatible with OGLES 2.x */
    private boolean isOGLES20Compatible() {
        ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        ConfigurationInfo info = am.getDeviceConfigurationInfo();
        return (info.reqGlEsVersion >= 0x20000);
    }

    /* show an error message */
    private void showOGLES20ErrorDialogBox() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("No OpenGL ES 2.0 GPU Found!").setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        finish();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void initFpsHandler() {
        //je crée un Handler et je reféfini la méthose handleMessage
        //de cette manière, je peux capter des informations qui sont émises par d'autres
        //thread pour pouvoir effectuer des actions dans cette scène.
        //notamemnt, la mise à jour des View (textView...etc..)
        //car seul le Thread de la scène peu mettre à jour les vue de la scène
        this.mFpsHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message inputMessage) {
                // Gets the image task from the incoming Message object.
                //PhotoTask photoTask = (PhotoTask) inputMessage.obj;

                switch (inputMessage.what) {

                    case UPDATE_FPS:
                        TextView tv = (TextView) findViewById(fpsTv_Id);
                        int value = inputMessage.getData().getInt(Scene.BUNDLE_FPS_VALUE);

                        tv.setText("FPS: " + String.valueOf(value));
                        break;


                }
            }

            ;
        };

    }
}
