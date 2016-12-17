package com.familledupuis91.gamingtools.components;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.SystemClock;
import android.util.Log;
import android.view.MotionEvent;

import com.familledupuis91.gamingtools.animations.AnimationManagerListener;

public class MySurfaceView extends GLSurfaceView {

    public float touchX = 0;
    public float touchY = 0;
    private Scene mScene;
    public float startX = 0;
    public float startY = 0;

    public float histoX = 0;
    public float histoY = 0;

    public boolean touched = false;
    public boolean animationManagerIsPlaying = false;
    public AnimationManagerListener animationManagerListener;
    public float lastTouch = 0.f;

    public static enum ScreenEvent {
        SCROLL_H_RIGHT, SCROLL_H_LEFT, SCROLL_V_UP, SCROLL_V_DOWN, SHORT_PRESS, LONG_PRESS, UNKNOWN
    }

    private ScreenEvent event;

    public MySurfaceView(Context context) {
        super(context);
        this.event = ScreenEvent.UNKNOWN;
        // TODO Auto-generated constructor stub


    }


    public void setRenderer(Scene scene) {
        super.setRenderer(scene);

        this.animationManagerListener = new AnimationManagerListener() {
            @Override
            public void onStartPlaying() {
                MySurfaceView.this.animationManagerIsPlaying = true;
                Log.e("toto", "animation start");

                checkRenderMode();
            }

            @Override
            public void onStopPlaying() {
                MySurfaceView.this.animationManagerIsPlaying = false;
                Log.e("toto", "animation stop");

                checkRenderMode();
            }

        };
        scene.getAnimationManager().addListener(this.animationManagerListener);


    }

    /**
     * si l'utilisateur ne touche plus l'écran et qu'il n'y pas plus d'animation à jouer
     * on repasse en mode de rendu "à la demande"
     * dans le cas contraire, on est en mode de rendu continue.
     */
    private void checkRenderMode() {
        if (this.touched == false && !this.animationManagerIsPlaying) {
            setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
            //Log.e("toto", "when dirty");
        } else {
            setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
            //Log.e("toto", "when continous");
        }


    }

    public float getLastTouchTime() {
        return this.lastTouch;
    }

    private void setLastTouchTime(float time) {
        this.lastTouch = time;
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {


        switch (e.getAction()) {
            case MotionEvent.ACTION_UP:
                //Log.e("action", "Action_up________________________________________________");
                touched = false;
                this.histoX = this.touchX;
                this.histoY = this.touchY;
                this.touchX = e.getX();
                this.touchY = e.getY();

                this.event = ScreenEvent.UNKNOWN;
                //setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);

                checkRenderMode(); //on regarde si on doit changer le mode de rendu

                //requestRender va planifier une mise à jour du thread de rendu
                //inutile de le faire plusieurs fois d'affilé
                requestRender(); // on fait une passe pour recalculer les colisions

                break;

            case MotionEvent.ACTION_DOWN:
                Log.e("action", "Action_down");
                if (this.event == ScreenEvent.UNKNOWN) {
                    //if faut rendre continuelement pour detecter les long click
                    //setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);

                    touched = true;
                    checkRenderMode();
                    //on vient de toucher l'écran en fonction du temps resté sur l'écran
                    // par défaut on considère que l'on est dans le cas d'un SHORT_PRESS

                    this.event = ScreenEvent.SHORT_PRESS;
                    this.setLastTouchTime(SystemClock.elapsedRealtime());

                    this.startX = e.getX();
                    this.startY = e.getY();

                    this.touchX = e.getX();
                    this.touchY = e.getY();

                    requestRender();
// je fais exprès de ma mettre de break pour aussi faire le move
                }
            case MotionEvent.ACTION_MOVE:
                Log.e("action", "Action_move");

                touched = true;
                this.touchX = e.getX();
                this.touchY = e.getY();

                float distanceX = this.startX - this.touchX;
                float distanceY = this.startY - this.touchY;

                Log.e("distanceX", String.valueOf(distanceX));
                Log.e("distanceY", String.valueOf(distanceY));

                float duree = SystemClock.elapsedRealtime()
                        - this.getLastTouchTime();

                // si cela fait plus de 500 ms que l'on a appuyé sur l'écran
                // on considère que l'on est dans le cas d'un LONG_PRESS
                if (duree > 500) {
                    this.event = ScreenEvent.LONG_PRESS;
                    Log.e("action", "LongPress");

                }



                    if (distanceX < 0 && distanceX < -200) {
                        this.event = ScreenEvent.SCROLL_H_LEFT;

                    } else {

                        if (distanceX > 0 && distanceX > 200) {
                            this.event = ScreenEvent.SCROLL_H_RIGHT;
                        }
                    }

                    if (distanceY < 0 && distanceY < -200) {
                        this.event = ScreenEvent.SCROLL_V_UP;

                    } else {

                        if (distanceY > 0 && distanceY > 200) {
                            this.event = ScreenEvent.SCROLL_V_DOWN;
                        }
                    }



                if (e.getHistorySize() > 0) {
                    this.histoX = e.getHistoricalX(e.getHistorySize() - 1);
                    this.histoY = e.getHistoricalY(e.getHistorySize() - 1);
                }
        }

        return true;
    }


    public ScreenEvent getScreenEvent() {
        return this.event;
    }

}
