package com.familledupuis91.gamingtools.animations;

import com.familledupuis91.gamingtools.components.GameObject;

import android.os.SystemClock;

public abstract class Animation implements Cloneable {

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    long startTime;
    long endTime;

    public float getElapsedTime() {
        return elapsedTime;
    }

    public void setElapsedTime(long elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

    private long elapsedTime;
    private float speed;

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    //durée de l'animation en ms
    private long duration;

    public static enum playingMode {
        NONE, ONCE, REPEAT_N, LOOP
    }

    public static enum AnimationStatus {
        PLAYING, STOPPED
    }

    private GameObject animatedGameObject;
    private AnimationStatus status;


    /****************************************************************
     * getters / setters
     *********************************************************/

    public GameObject getAnimatedGameObject() {
        return this.animatedGameObject;

    }

    public void setAnimatedGameObject(GameObject gameObject) {
        this.animatedGameObject = gameObject;

    }

    public float getSpeed() {

        return speed;
    }

    public void setSpeed(float speed) {

        this.speed = speed;
    }

    public AnimationStatus getStatus() {
        return this.status;

    }

    public void setStatus(AnimationStatus status) {
        this.status = status;

    }

    //constructeur
    public Animation(GameObject animatedGameObject) {
        this.animatedGameObject = animatedGameObject;

    }

    public void launch() {
        if (this.status != AnimationStatus.PLAYING) {
            startTime = -1;
            this.status = AnimationStatus.PLAYING;
        }
    }

    public void stop() {
        this.status = AnimationStatus.STOPPED;
        endTime = SystemClock.elapsedRealtime();
        startTime = -1;
    }

    public void play() {

    }


    public Animation clone() throws CloneNotSupportedException {
        return (Animation) super.clone();

    }


}
