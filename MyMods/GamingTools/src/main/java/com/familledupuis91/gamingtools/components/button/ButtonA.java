package com.familledupuis91.gamingtools.components.button;

import android.os.Bundle;
import android.os.SystemClock;

import com.familledupuis91.gamingtools.components.GroupOfGameObject;
import com.familledupuis91.gamingtools.components.shapes.Shape;
import com.familledupuis91.gamingtools.components.shapes.Rectangle2D;
import com.familledupuis91.gamingtools.components.texture.Texture;
import com.familledupuis91.gamingtools.enums.DrawingMode;
import com.familledupuis91.gamingtools.inputs.UserFinger;
import com.familledupuis91.gamingtools.interfaces.Clikable;
import com.familledupuis91.gamingtools.utils.Tools;

import java.util.ArrayList;

public class ButtonA extends GroupOfGameObject implements Clikable {
    public enum ButtonStatus {
        UP, DOWN
    }

    private final String BTN__A_TAG = "BTN_A";
    private Rectangle2D rectangle2D_A;
    private Rectangle2D rectangle2D_B;

    public Texture textureUp;
    public Texture textureDown;
    public Texture textureBack;
    public ButtonStatus status;
    private float lastTap;
    private float elapsedTime;
    private boolean listening;
    private boolean ON_CLICK_FIRE;
    private final float DELAY_BTWN_TAP = 200; //200ms
    private final float ON_LONG_CLICK_DELAY = 1000;

    private final ArrayList<GLButtonListener> eventListenerList = new ArrayList<GLButtonListener>();

    public ButtonA(float x, float y, float witdth, float height, Texture textureUp, Texture textureDown, Texture textureBack) {

        this.textureUp = textureUp;
        this.textureDown = textureDown;
        this.textureBack = textureBack;

        //définition du premier rectangle : il s'agit du bouton sur lequel on appuie
        this.rectangle2D_A = new Rectangle2D(DrawingMode.FILL);
        this.rectangle2D_A.textureEnabled = true;
        //   rectangle2D_A.setTexture(this.textureUp);

        //il s'agit de la taille initiale. au final, elle va être propotionelle au
        //GroupOfGameObject.
        this.rectangle2D_A.setWidth(.5f);
        this.rectangle2D_A.setHeight(.5f);
        this.rectangle2D_A.enableCollisions();
        this.rectangle2D_A.setTagName(BTN__A_TAG + ":A");

        //Définition du second rectangle : dans ce rectangle on va afficher un cercle
        //qui va venir rétrécir au fur et à mesure que l'on laisse le doigt appuyé
        //sur le bouton.
        this.rectangle2D_B = new Rectangle2D(DrawingMode.FILL);
        this.rectangle2D_B.textureEnabled = true;
        this.rectangle2D_B.setTexture(this.textureBack);
        this.rectangle2D_B.setVisibility(false);
        this.rectangle2D_B.setWidth(1);
        this.rectangle2D_B.setHeight(1);
        this.rectangle2D_B.setAlpha(0);
        this.rectangle2D_B.disableCollisions();
        this.rectangle2D_B.setTagName(BTN__A_TAG + ":B" + rectangle2D_B.toString());

        //on ajoute les 2 boutons dans la liste des composants.
        this.add(rectangle2D_A);
        this.add(rectangle2D_B);

        this.status = ButtonStatus.UP;
        this.setX(x);
        this.setY(y);
        this.setHeight(height);
        this.setWidth(witdth);

        this.listening = false;

        //this.isStatic = false;

    }

    public void addGLButtonListener(GLButtonListener glButtonListener) {
        this.eventListenerList.add(glButtonListener);
    }

    @Override
    public void update() {


//        rectangle2D_A.setTexture(this.textureUp);

        //on se garde un délai technique entre 2 détection de touché pour ne pas
        //recalculer sans cesse
        if (SystemClock.elapsedRealtime() - this.lastTap != DELAY_BTWN_TAP) {

            Shape uf = (Shape) this.getScene().getGOManager().getGameObjectByTag(UserFinger.USER_FINGER_TAG);

            //si l'utilisateur est en train d'appuyer sur le bouton A
            if (this.getScene().getColliderManager().isCollide(uf, rectangle2D_A)) {
                //on change la texture pour celle du bouton appuyé;
                rectangle2D_A.setTexture(this.textureDown);
                //on indique que le statut du bouton est "appuyé - DOWN"
                this.status = ButtonStatus.DOWN;

                // si je n'étais en train d'écouler, j'initialise le compteur delai
                //qui va servir à connaitre le temps d'appui sur le bouton
                if (!this.listening) {
                    onStartListening();


                } else {
                    // si je suis en train d'écouter, l'incrémente le compteur de temps
                    // pour avoir une idée du temp laissé appuyé sur le bouton
                    //ceci pour detecter les longClick
                    this.elapsedTime = SystemClock.elapsedRealtime() - this.lastTap;
                }


            } else {

                //par défaut la texture du bouton correspond à celle
                //d'un bonton non appuyé
                rectangle2D_A.setTexture(textureUp);
                this.status = ButtonStatus.UP;

            }
        }
        //avec les nouvelle données, je check si on vient de faire un click
        if (this.listening) {
            updateButtonB();
            this.checkClick();
        }

    }

    //on teste le type de click réalisé par l'utilisateur
    private void checkClick() {
        //si l'utilisateur a levé le doigt
        if (this.status == ButtonStatus.UP) {
            //on a levé le doigt avant de délai d'un long click
            //c'est donc un click
            if (elapsedTime < ON_LONG_CLICK_DELAY) {
                onClick();
                this.onStopListening();
            }
            //sinon, on arrête d'écouter
            this.onStopListening();
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


    private void updateButtonB() {
        //on rend le second rectangle visible
        rectangle2D_B.setVisibility(true);
        //on augmente son alpha
        rectangle2D_B.setAlpha(rectangle2D_B.getAlpha() + 0.1f);

        //on réduit progressivement sa taille
        rectangle2D_B.setHeight((rectangle2D_B.getHeight() < 0) ? 0 : rectangle2D_B.getHeight() - 8.0f);
        rectangle2D_B.setWidth((rectangle2D_B.getWidth() < 0) ? 0 : rectangle2D_B.getWidth() - 8.0f);

        computeRectangleBCoord();
    }


    //Actions à effectuer à la fin d'une écoute
    private void onStopListening() {
        this.listening = false;
        reinitializeButtonB();
    }

    //Actions à efféctuer au démarrage d'une écoute
    private void onStartListening() {
        lastTap = SystemClock.elapsedRealtime();
        this.elapsedTime = 0f;
        this.ON_CLICK_FIRE = false;
        //on commence à écouter ce que fait l'utilisateur
        this.listening = true;
        reinitializeButtonB();
    }

    //initialiser le bouton B aux valeurs d'origine
    private void reinitializeButtonB() {
        rectangle2D_B.setVisibility(false);
        rectangle2D_B.setAlpha(0);

        //Retour à la taille initiale
        rectangle2D_B.setHeight(this.getHeight());
        rectangle2D_B.setWidth(this.getWidth());

        computeRectangleBCoord();

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

    private void computeRectangleBCoord(){
        //center le rectangle B sur le A
    //    rectangle2D_B.setX(rectangle2D_A.getX() - (rectangle2D_B.getWidth() - rectangle2D_A.getWidth())/2);
    //    rectangle2D_B.setY(rectangle2D_A.getY() - (rectangle2D_B.getHeight() - rectangle2D_A.getHeight())/2);

        Tools.allignCenter(rectangle2D_A,rectangle2D_B);

    }

    /**
     * pour tous les objets qui écoutent le onLongClick(), on leur passe
     * l'info
     */
    public void onLongClick() {
        for (GLButtonListener listener : eventListenerList) {
            listener.onLongClick();
        }
    }


}
