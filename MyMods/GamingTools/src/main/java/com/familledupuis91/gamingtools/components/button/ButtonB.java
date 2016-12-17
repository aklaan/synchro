package com.familledupuis91.gamingtools.components.button;

import android.os.Bundle;
import android.os.SystemClock;

import com.familledupuis91.gamingtools.components.shapes.Shape;
import com.familledupuis91.gamingtools.components.shapes.Rectangle2D;
import com.familledupuis91.gamingtools.components.texture.Texture;
import com.familledupuis91.gamingtools.enums.DrawingMode;
import com.familledupuis91.gamingtools.inputs.UserFinger;
import com.familledupuis91.gamingtools.interfaces.Clikable;

import java.util.ArrayList;

public class ButtonB extends Rectangle2D implements Clikable {
    public enum ButtonStatus {
        UP, DOWN
    }

    public Texture textureUp;
    public Texture textureDown;
    public ButtonStatus status;
    private float lastTap;
    private float elapsedTime;
    private boolean listening;
    private boolean ON_CLICK_FIRE;
    private final float DELAY_BTWN_TAP = 200; //200ms
    private final float ON_LONG_CLICK_DELAY = 1000;
    private float originalWidth, originalHeight;

    private final ArrayList<GLButtonListener> eventListenerList = new ArrayList<GLButtonListener>();

    public ButtonB(float x, float y, float witdth, float height, Texture textureUp, Texture textureDown) {
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

        this.enableTexturing();
        this.setVisibility(false);
        this.setAlpha(0.f);
    }

    public void addGLButtonListener(GLButtonListener glButtonListener) {
        this.eventListenerList.add(glButtonListener);
    }

    @Override
    public void update() {
        this.setTexture(this.textureUp);
        //  Log.e("button", "on update");
        if (SystemClock.elapsedRealtime() - this.lastTap != DELAY_BTWN_TAP) {

            Shape uf = (Shape) this.getScene().getGOManager().getGameObjectByTag(UserFinger.USER_FINGER_TAG);

            if (this.getScene().getColliderManager().isCollide(uf,this)) {
                //        Log.e("button", "set texture down");
                this.setTexture(this.textureDown);
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
                this.status = ButtonStatus.UP;

            }
        }
        //avec les nouvelle données, je check si on vient de faire un click
        this.checkClick();
    }

    private void checkClick() {

        //si on est en train d'écouter ce que fait l'utilisateur
        if (this.listening) {
            this.setVisibility(true);

            this.setAlpha(this.getAlpha() + 0.1f);

            this.setHeight((this.getHeight() < 0) ? 0 : this.getHeight() - 8.0f);
            this.setWidth((this.getWidth()<0)? 0: this.getWidth()-8.0f);
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
        this.setAlpha(0);
        this.setVisibility(false);
        this.setHeight(this.originalHeight);
        this.setWidth(this.originalWidth);
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
