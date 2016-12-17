package com.familledupuis91.gamingtools.components;

import com.familledupuis91.gamingtools.utils.CONST;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Created by rodol on 29/01/2016.
 */
public class ColorRGBA {

    private float red = 1.f;
    private float green = 1.f;
    private float blue = 1.f;
    private float alpha = 1.f;


    public void setRed(float colorValue) {
        this.red = checkValue(colorValue);
    }

    public float getRed() {
        return this.red;
    }


    public void setGreen(float colorValue) {
        this.green = checkValue(colorValue);
    }

    public float getGreen() {
        return this.green;
    }


    public void setBlue(float colorValue) {
        this.blue = checkValue(colorValue);
    }

    public float getBlue() {
        return this.blue;
    }


    public void setAlpha(float alphaValue) {
        this.alpha = checkValue(alphaValue);
    }

    public float getAlpha() {
        return this.alpha;
    }

    //Par défaut on retourne blanc.
    public ColorRGBA(){
        this.setRed(1f);
        this.setGreen(1f);

        this.setBlue(1f);
        this.setAlpha(1f);
    }

    public ColorRGBA(float r, float g, float b, float a) {
        this.setRed(r);
        this.setGreen(g);

        this.setBlue(b);
        this.setAlpha(a);
    }

    private float checkValue(float colorValue) {
        colorValue = (colorValue > 1) ? 1f : colorValue;
        //je force l'alpha à une valeur très petite pour éviter d'avoir zéro.
        //sinon ça risque de poser problème de division par zéro dans les ratio de
        //group og gameobject
        colorValue = (colorValue <= 0) ? 0.0001f : colorValue;

        return colorValue;
    }


    public FloatBuffer getColorBuffer() {

        FloatBuffer result = ByteBuffer.allocateDirect(4 * CONST.FLOAT_SIZE)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();

        result.rewind();
        //on écrit les coordonées de texture
        result.put(getRed()).put(getGreen()).put(getBlue()).put(getAlpha());
        // on se repositionne en 0 , prêt pour la lecture
        result.rewind();


        return result;
    }


}
