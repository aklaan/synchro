package com.familledupuis91.gamingtools.components.button;

import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;


import com.familledupuis91.gamingtools.components.ColorRGBA;
import com.familledupuis91.gamingtools.components.shapes.Shape;
import com.familledupuis91.gamingtools.components.shapes.Rectangle2D;
import com.familledupuis91.gamingtools.components.texture.Texture;
import com.familledupuis91.gamingtools.enums.DrawingMode;
import com.familledupuis91.gamingtools.inputs.UserFinger;
import com.familledupuis91.gamingtools.interfaces.Clikable;

import java.util.ArrayList;

public class Button extends Rectangle2D implements Clikable {
    public enum ButtonStatus {
        UP, DOWN
    }

    public Texture textureUp;
    public Texture textureDown;
    public ColorRGBA colorDown = new ColorRGBA(0f, 1f, 1f, 1f);
    public ColorRGBA colorUp = new ColorRGBA(0f, 1f, 0f, 1f);

    public ButtonStatus status;
    private float lastTap;
    private float elapsedTime;
    private boolean listening;
    private boolean ON_CLICK_FIRE;
    private final float DELAY_BTWN_TAP = 200; //200ms
    private final float ON_LONG_CLICK_DELAY = 1000;
    private float originalWidth, originalHeight;

    private final ArrayList<GLButtonListener> eventListenerList = new ArrayList<GLButtonListener>();


    public Button(float x, float y, float width, float height) {
        super(DrawingMode.FILL);
        this.originalHeight = height;
        this.originalWidth = width;
        this.status = ButtonStatus.UP;
        this.setCoord(x, y);
        this.setHeight(height);
        this.setWidth(width);


        this.listening = false;

        this.enableCollisions();

        this.textureEnabled = false;
    }


    public Button(float x, float y, float witdth, float height, Texture textureUp, Texture textureDown) {
        super(DrawingMode.FILL);
        this.originalHeight = height;
        this.originalWidth = witdth;
        this.status = ButtonStatus.UP;
        this.setCoord(x, y);
        this.setHeight(height);
        this.setWidth(witdth);
        this.listening = false;
        this.textureUp = textureUp;
        this.textureDown = textureDown;
        this.enableCollisions();

        this.textureEnabled = true;
    }

    public void addGLButtonListener(GLButtonListener glButtonListener) {
        this.eventListenerList.add(glButtonListener);
    }

    @Override
    public void update() {
        this.setTexture(this.textureUp);
        this.setAmbiantColor(colorUp);
        //  Log.e("button", "on update");
        if (SystemClock.elapsedRealtime() - this.lastTap != DELAY_BTWN_TAP) {

            Shape uf = (Shape) this.getScene().getGOManager().getGameObjectByTag(UserFinger.USER_FINGER_TAG);

            if (this.getScene().getColliderManager().isCollide(uf, this)) {
                Log.e("button", this.getTagName() + ": down");
                this.setTexture(this.textureDown);
                this.setAmbiantColor(colorDown);
                this.status = ButtonStatus.DOWN;


                // si je n'étais en train d'écouler, j'initialise le compteur delai
                if (!this.listening) {
                    lastTap = SystemClock.elapsedRealtime();
                    this.elapsedTime = 0f;
                    this.ON_CLICK_FIRE = false;
                    //on commence à écouter ce que fait l'utilisateur
                    this.listening = true;
                } else {

                    // si je suis en train d'écouter, l'incrémente le compteur de temps
                    // pour avoir une idée du temp laissé appuyé sur le bouton
                    //ceci pour detecter les longClick
                    this.elapsedTime = SystemClock.elapsedRealtime() - this.lastTap;
                }


            } else {
                this.setTexture(textureUp);
                this.setAmbiantColor(colorUp);
                this.status = ButtonStatus.UP;

            }
        }
        //avec les nouvelle données, je check si on vient de faire un click
        this.checkClick();
    }

    private void checkClick() {

        //si on est en train d'écouter ce que fait l'utilisateur
        if (this.listening) {

            //  this.setWidth(this.getWidth() + 2.f);
            //  this.setHeight(this.getHeight() + 2.f);
            //si l'utilisateur a levé le doigt
            if (this.status == ButtonStatus.UP) {
                //on a levé le doigt avant de délai d'un long click
                if (elapsedTime < ON_LONG_CLICK_DELAY) {
                    onClick();
                    this.stopListening();
                }
                //sinon, on arrête d'écouter
                this.stopListening();
            }

            //si l'utilisateur a toujours le doigt appuyé sur l'écran
            // et qu'il a le doit appuyé depuis le temps necessaire pour un longClick
            if ((this.status == ButtonStatus.DOWN) && elapsedTime > ON_LONG_CLICK_DELAY && !ON_CLICK_FIRE) {

                ON_CLICK_FIRE = true;
                //on appel la méthode onclick
                onLongClick();

                this.elapsedTime = 0f;
            }
        }
    }

    private void stopListening() {
        this.listening = false;
        this.setWidth(this.originalWidth);
        this.setHeight(this.originalHeight);
    }

    /**
     * pour tous les objets qui écoutent le onClick(), on leur passe
     * l'info
     */
    public void onClick() {
        for (GLButtonListener listener : eventListenerList) {
            listener.onClick(new Bundle());

        }
    }


    public void onLongClick() {
        for (GLButtonListener listener : eventListenerList) {
            listener.onLongClick();
        }
    }


}
