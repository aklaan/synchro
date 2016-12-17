package com.familledupuis91.gamingtools.components.numericpad;

import android.os.Bundle;

import com.familledupuis91.gamingtools.components.GroupOfGameObject;
import com.familledupuis91.gamingtools.components.button.ButtonWithText;
import com.familledupuis91.gamingtools.components.button.GLButtonListener;
import com.familledupuis91.gamingtools.components.fonts.GlFont;
import com.familledupuis91.gamingtools.components.texture.Texture;

import java.util.ArrayList;

/**
 * Created by rodol on 04/02/2016.
 */
public class GLNumericPad extends GroupOfGameObject {
    private final ArrayList<GLNumericPadListener> eventListenerList = new ArrayList<GLNumericPadListener>();
    public static String KEY_PRESSED = "KEY";
    private Texture mTextureUP;
    private Texture mTextureDown;
    private GlFont mGlFont;


    public GLNumericPad(float x, float y, float width, float height, Texture texUp, Texture texDown, GlFont glFont) {
        super();
        //initialisation de base pour le clavier
        this.setX(x);
        this.setY(y);
        this.setWidth(width);
        this.setHeight(height);
        this.mGlFont = glFont;
        this.mTextureDown = texDown;
        this.mTextureUP = texUp;

        //initialisation de la position de la première touche
        float spaceX = this.getX();
        float spaceY = this.getY();

        float button_size = width/3;

        //première ligne du bas
        addOkButton(spaceX,spaceY,button_size);

        //seconde ligne
        spaceY += button_size;
        String text = "0.";
        for (int i = 0; i < text.length(); i++) {
            this.addCharKey(text.charAt(i), spaceX, spaceY, button_size);
            spaceX += button_size;
        }

        addClearButton(spaceX,spaceY,button_size);

        //seconde ligne
        spaceX = this.getX();
        spaceY += button_size;
        text = "123";
        for (int i = 0; i < text.length(); i++) {
            this.addCharKey(text.charAt(i), spaceX, spaceY, button_size);
            spaceX += button_size;
        }

        //Troisième  ligne
        spaceX = this.getX();
        spaceY += button_size;
        text = "456";
        for (int i = 0; i < text.length(); i++) {
            this.addCharKey(text.charAt(i), spaceX, spaceY, button_size);
            spaceX += button_size;
        }

        //Quatrième ligne
        spaceX = this.getX();
        spaceY += button_size;
        text = "789";
        for (int i = 0; i < text.length(); i++) {
            this.addCharKey(text.charAt(i), spaceX, spaceY, button_size);
            spaceX += button_size;
        }


    }


    public void addGLNumericPadListener(GLNumericPadListener glNumericPadListener) {
        this.eventListenerList.add(glNumericPadListener);
    }


    private void addCharKey(char value, float spaceX, float spaceY, float buttonSize) {

        ButtonWithText bt = new ButtonWithText(spaceX, spaceY, buttonSize, buttonSize, this.mTextureUP, this.mTextureDown, this.mGlFont);
        bt.setText(String.valueOf(value));

        //ajout d'un listener sur le bouton
        bt.addGLButtonListener(new GLButtonListener() {
            @Override
            public void onClick(Bundle bundle) {

                //je récupère le texte du bouton qui est présent dans le Bundle
                String button_Value = bundle.getString(ButtonWithText.TEXT_VALUE);

                //comme ce texte est censé être seulement une lettre, je ne garde que cette
                //lettre en char et j'appelle le onClick pour avertir
                //tous les listeners avec la lettre en paramètre.
                GLNumericPad.this.onClick(button_Value.charAt(0));
            }

            public void onLongClick() {
                //Nothing to do
            }

        });


        this.add(bt);


    }


    private void addOkButton(float spaceX, float spaceY, float buttonSize) {

        ButtonWithText bt = new ButtonWithText(spaceX, spaceY, buttonSize*3, buttonSize, this.mTextureUP, this.mTextureDown, this.mGlFont);
        bt.setText("OK");

        //ajout d'un listener sur le bouton
        bt.addGLButtonListener(new GLButtonListener() {
            @Override
            public void onClick(Bundle bundle) {

                GLNumericPad.this.onClickOk();
            }

            public void onLongClick() {
                //Nothing to do
            }

        });


        this.add(bt);


    }



    private void addClearButton(float spaceX, float spaceY, float buttonSize) {

        ButtonWithText bt = new ButtonWithText(spaceX, spaceY, buttonSize , buttonSize, this.mTextureUP, this.mTextureDown, this.mGlFont);
        bt.setText("C");

        //ajout d'un listener sur le bouton
        bt.addGLButtonListener(new GLButtonListener() {
            @Override
            public void onClick(Bundle bundle) {

                GLNumericPad.this.onClickClear();;
            }

            public void onLongClick() {
                //Nothing to do
            }

        });


        this.add(bt);


    }

    /**
     * pour tous les objets qui écoutent le onClick(), on leur passe
     * l'info
     */
    public void onClick(char value) {
        for (GLNumericPadListener listener : eventListenerList) {
            Bundle bundle = new Bundle();
            bundle.putChar(GLNumericPad.KEY_PRESSED, value);
            listener.onClick(bundle);

        }
    }

    public void onClickOk() {
        for (GLNumericPadListener listener : eventListenerList) {
            listener.onClickOk();

        }
    }

    public void onClickClear() {
        for (GLNumericPadListener listener : eventListenerList) {
            listener.onClickClear();

        }
    }

}
