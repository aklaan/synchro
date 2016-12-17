package com.familledupuis91.gamingtools.components.fonts;


import android.util.Log;

import com.familledupuis91.gamingtools.components.ColorRGBA;
import com.familledupuis91.gamingtools.components.Composition;
import com.familledupuis91.gamingtools.components.GroupOfGameObject;

import java.util.ArrayList;

/**
 * Created by rodol on 18/02/2016.
 */
public class GlString extends GroupOfGameObject implements Composition {

    private String mText;
    private GlFont mGlFont;
    private float mFontSize;
    private float maxCharHeight;

    /**
     * getter & setter
     */

    public GlFont getGlFont() {
        return mGlFont;
    }

    public void setGlFont(GlFont mGlFont) {
        this.mGlFont = mGlFont;
        //on a modifié la font. il faut que l'update répercute
        // la modification sur les char
        this.changeFontofGlchar();
    }

    public float getFontSize() {
        return mFontSize;
    }

    public void setFontSize(float mFontSize) {
        this.mFontSize = mFontSize;
        updateGlchar();
    }

    //on redéfini l'obtention de la hauteur de la chaine
    @Override
    public float getHeight(){
        return this.maxCharHeight;
    }


    public ArrayList<GlChar> getGlCharList() {
        ArrayList<GlChar> result = new ArrayList<GlChar>();
        for (Composition composition : this.getComponent()) {
            if (composition instanceof GlChar) {
                result.add((GlChar) composition);
            }


        }
        return result;
    }


    public String getText() {
        return this.mText;
    }

    public void setText(String string) {
        this.mText = string;
        this.getList().clear();
        //la première lettre et à l'emplacement du début de la chaine
        float xPosition = this.getX();
        for (int i = 0; i < string.length(); i++) {
            GlChar mChar = new GlChar(string.charAt(i), this.getGlFont());
            mChar.setX(xPosition);
            mChar.setY(this.getY());
            //si la taille du caratère est plus haute que la dernière connue, on redéfinie la hauteur de la chaine
            this.maxCharHeight = (this.maxCharHeight < mChar.getHeight()) ? mChar.getHeight() : this.maxCharHeight;
            this.getList().add(mChar);
            //le prochain caratère serra après
            xPosition += mChar.getWidth();

        }
        //on calcule la taille de la chaine de caractère en faisant point d'arrivé - point de départ
        this.setWidth(xPosition - this.getX());
    }

    //pour Utiliser une GlString, on doit obligatoirement définir la font
    public GlString(GlFont glFont) {
        this.setGlFont(glFont);
        //par defaut les caratères font 10 pixel de haut.
        this.setFontSize(10);
        this.maxCharHeight = 0f;
    }




    // mise à jour des caractères
    public void updateGlchar() {

        float xPosition = this.getX();

        for (GlChar glChar : this.getGlCharList()) {
            glChar.setFontSize(this.getFontSize());
            glChar.setX(xPosition);
            glChar.setY(this.getY());
            xPosition += this.getFontSize() * glChar.getBase2AdvanceRatio();

            Log.e("debug", String.valueOf(glChar.getValue())
                    + "/size:" + String.valueOf(glChar.getFontSize())
                    + "/xpos:" + String.valueOf(xPosition)
                    + "/height:" + String.valueOf(glChar.getHeight())
                    + "/width:" + String.valueOf(glChar.getWidth()));
        }

        //on calcule la taille de la chaine de caractère en faisant point d'arrivé - point de départ
        this.setWidth(xPosition - this.getX());
    }


    public void changeFontofGlchar() {

        for (GlChar glChar : this.getGlCharList()) {
            glChar.setGlFont(this.getGlFont());
        }
    }

    @Override
    public void update() {
     //   setFontSize(getFontSize() + 0.1f);
     //   updateGlchar();
    }


    private void add(GlChar glChar) {
        glChar.setGlFont(this.getGlFont());
        this.getList().add(glChar);
    }


}
