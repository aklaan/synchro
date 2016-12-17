package com.familledupuis91.gamingtools.components.keyboard;

import android.os.Bundle;
import android.util.Log;

import com.familledupuis91.gamingtools.components.GroupOfGameObject;
import com.familledupuis91.gamingtools.components.button.ButtonWithText;
import com.familledupuis91.gamingtools.components.button.GLButtonListener;
import com.familledupuis91.gamingtools.components.fonts.GlFont;
import com.familledupuis91.gamingtools.components.texture.Texture;

import java.util.ArrayList;

/**
 * Created by rodol on 04/02/2016.
 */
public class GLKeyboard extends GroupOfGameObject {
    private final ArrayList<GLKeyboardListener> eventListenerList = new ArrayList<GLKeyboardListener>();
    public static String KEYPRESSED = "KEYPRESSED";
    private Texture mTextureUP;
    private Texture mTextureDown;
    private GlFont mGlFont;
private static final char SPACE=' ';

    public GLKeyboard(float x, float y, float width, float height, Texture texUp, Texture texDown, GlFont glFont) {
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

        String text = "AZERTYUIOP";
        float button_size = width / text.length();

        for (int i = 0; i < text.length(); i++) {
            this.addCharKey(text.charAt(i), spaceX, spaceY, button_size);
            spaceX += button_size;
        }

        spaceX = this.getX();
        spaceY -= button_size;
        text = "QSDFGHJKLM";
        for (int i = 0; i < text.length(); i++) {
            this.addCharKey(text.charAt(i), spaceX, spaceY, button_size);
            spaceX += button_size;
        }


        spaceX = this.getX();
        spaceY -= button_size;
        text = "WXCVBN'";
        for (int i = 0; i < text.length(); i++) {
            this.addCharKey(text.charAt(i), spaceX, spaceY, button_size);
            spaceX += button_size;
        }


        spaceX = this.getX();
        spaceY -= button_size;
        this.addSpace(spaceX, spaceY, button_size);



    }


    public void addGLKeyboardListener(GLKeyboardListener glKeyboardListener) {
        this.eventListenerList.add(glKeyboardListener);
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
                GLKeyboard.this.onClick(button_Value.charAt(0));
            }

            public void onLongClick() {
                Log.e("debug", "Longclick");
            }

        });


        this.add(bt);


    }


    private void addSpace(float spaceX, float spaceY, float buttonSize) {

        ButtonWithText bt = new ButtonWithText(spaceX, spaceY, buttonSize*5, buttonSize, this.mTextureUP, this.mTextureDown, this.mGlFont);
        bt.setText("espace");

        //ajout d'un listener sur le bouton
        bt.addGLButtonListener(new GLButtonListener() {
            @Override
            public void onClick(Bundle bundle) {

                //je récupère le texte du bouton qui est présent dans le Bundle
                String button_Value = bundle.getString(ButtonWithText.TEXT_VALUE);

                //comme ce texte est censé être seulement une lettre, je ne garde que cette
                //lettre en char et j'appelle le onClick pour avertir
                //tous les listeners avec la lettre en paramètre.
                GLKeyboard.this.onClick(SPACE);
            }

            public void onLongClick() {
                Log.e("debug", "Longclick");
            }

        });


        this.add(bt);


    }



    /**
     * pour tous les objets qui écoutent le onClick(), on leur passe
     * l'info
     */
    public void onClick(char value) {
        for (GLKeyboardListener listener : eventListenerList) {
            Bundle bundle = new Bundle();
            bundle.putChar(GLKeyboard.KEYPRESSED, value);
            listener.onClick(bundle);

        }
    }

}
