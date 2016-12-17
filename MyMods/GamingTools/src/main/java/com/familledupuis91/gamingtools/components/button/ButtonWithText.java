package com.familledupuis91.gamingtools.components.button;

import android.os.Bundle;
import android.os.SystemClock;


import com.familledupuis91.gamingtools.components.GroupOfGameObject;
import com.familledupuis91.gamingtools.components.fonts.GlFont;
import com.familledupuis91.gamingtools.components.fonts.GlString;
import com.familledupuis91.gamingtools.components.shapes.Rectangle2D;
import com.familledupuis91.gamingtools.components.shapes.Shape;
import com.familledupuis91.gamingtools.components.texture.Texture;
import com.familledupuis91.gamingtools.enums.DrawingMode;
import com.familledupuis91.gamingtools.inputs.UserFinger;
import com.familledupuis91.gamingtools.interfaces.Clikable;
import com.familledupuis91.gamingtools.utils.Tools;

import java.util.ArrayList;

/**
 * un bouton avec texte
 */

public class ButtonWithText extends GroupOfGameObject implements Clikable {
    public enum ButtonStatus {
        UP, DOWN
    }

    private final String BTN_WTH_TXT_TAG = "BTN_WTH_TXT";
    private final String TEXT = "TEXT";
    private Rectangle2D rectangle2D;
    private GlString mText;
    public static String TEXT_VALUE = "TEXT_VALUE";


    public Texture textureUp;
    public Texture textureDown;

    public ButtonStatus status;
    private float lastTap;
    private float elapsedTime;
    private boolean listening;
    private boolean ON_CLICK_FIRE;
    private final float DELAY_BTWN_TAP = 200; //200ms
    private final float ON_LONG_CLICK_DELAY = 1000;
    private final ArrayList<GLButtonListener> eventListenerList = new ArrayList<GLButtonListener>();


    public GlString getText(){return this.mText;}


    public ButtonWithText(float x, float y, float witdth, float height, Texture textureUp, Texture textureDown, GlFont glFont) {

        this.textureUp = textureUp;
        this.textureDown = textureDown;

        //définition du premier rectangle : il s'agit du bouton sur lequel on appuie
        this.rectangle2D = new Rectangle2D(DrawingMode.FILL);
        this.rectangle2D.enableTexturing();

        //il s'agit de la taille initiale. au final, elle va être propotionelle au
        //GroupOfGameObject.
        this.rectangle2D.setWidth(1f);
        this.rectangle2D.setHeight(1f);

        //on active la gestion des collision
        this.rectangle2D.enableCollisions();
        this.rectangle2D.setTagName(BTN_WTH_TXT_TAG);


        //Définition du texte affiché sur le bouton.
        this.mText = new GlString(glFont);


        //on ajoute le rectangle et le texte dans la liste des composants.
        // il faut respecter l'odre d'affichage. d'abord le bouton et ensuite le texte
        this.add(rectangle2D);
        this.add(mText);

        //par défaut le bouton est en position haute
        this.status = ButtonStatus.UP;

        this.setX(x);
        this.setY(y);
        this.setHeight(height);
        this.setWidth(witdth);




        this.listening = false;

    }

    public void addGLButtonListener(GLButtonListener glButtonListener) {
        this.eventListenerList.add(glButtonListener);
    }

    public void setText(String string) {

        mText.setText(string);
        //on recalcule la position du texte pour le centrer
     //   this.mText.setX(this.getX() + (this.getWidth() / 2f) - (mText.getWidth()/2f));
     //   this.mText.setY(this.getY() + (this.getHeight() / 2f) - (mText.getHeight()/2f));

        Tools.allignCenter(this,mText);
    }


    @Override
    public void update() {
        rectangle2D.setTexture(this.textureUp);



        if (SystemClock.elapsedRealtime() - this.lastTap != DELAY_BTWN_TAP) {

            Shape uf = (Shape) this.getScene().getGOManager().getGameObjectByTag(UserFinger.USER_FINGER_TAG);

            if (this.getScene().getColliderManager().isCollide(uf, rectangle2D)) {
                //        Log.e("button", "set texture down");
                rectangle2D.setTexture(this.textureDown);
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
                rectangle2D.setTexture(textureUp);
                this.status = ButtonStatus.UP;

            }
        }
        //avec les nouvelle données, je check si on vient de faire un click
        this.checkClick();


    }

    private void checkClick() {
        //si on est en train d'écouter ce que fait l'utilisateur
        if (this.listening) {


            //si l'utilisateur a levé le doigt
            if (this.status == ButtonStatus.UP) {
                //on a levé le doigt avant de délai d'un long click
                //c'est donc un click
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


    }


    /**
     * pour tous les objets qui écoutent le onClick(), on leur passe
     * l'info
     */
    public void onClick() {
        for (GLButtonListener listener : eventListenerList) {

            //on utilise un Bundle pour pouvoir passer toute sorte de choses
            Bundle bundle = new Bundle();
            bundle.putString(ButtonWithText.TEXT_VALUE,this.getText().getText());
            listener.onClick(bundle);

        }
    }


    public void onLongClick() {
        for (GLButtonListener listener : eventListenerList) {
            listener.onLongClick();
        }
    }


}
