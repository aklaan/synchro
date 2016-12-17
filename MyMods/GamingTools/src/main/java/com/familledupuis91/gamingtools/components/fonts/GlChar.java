package com.familledupuis91.gamingtools.components.fonts;

import com.familledupuis91.gamingtools.components.ColorRGBA;
import com.familledupuis91.gamingtools.components.shapes.Rectangle2D;
import com.familledupuis91.gamingtools.enums.DrawingMode;

/**
 * Created by rodol on 18/02/2016.
 */
public class GlChar extends Rectangle2D {

    private char value;
    private float xOffset = 0;
    private float yOffset = 0;
    private float xSpace = 0;
    private float mFontSize;
    private float ratioWidthHeight;
    private float base2HeightRatio;

    public float getBase2AdvanceRatio() {
        return base2AdvanceRatio;
    }

    public void setBase2AdvanceRatio(float base2AdvanceRatio) {
        this.base2AdvanceRatio = base2AdvanceRatio;
    }

    private float base2AdvanceRatio;

    public float getBase2HeightRatio() {
        return this.base2HeightRatio;
    }

    public void setBase2HeightRatio(float ratio) {
        this.base2HeightRatio = ratio;
    }

    public float getXoffset() {
        return xOffset;
    }

    public void setXoffset(float xOffset) {
        this.xOffset = xOffset;
    }

    public float getYoffset() {
        return yOffset;
    }

    public void setYoffset(float yOffset) {
        this.yOffset = yOffset;
    }


    public float getXspace() {
        return this.getFontSize() * (140f / 235f);

    }


    public void setXspace(float xSpace) {
        this.xSpace = xSpace;
    }


    public float getFontSize() {
        return mFontSize;
    }

    public void setFontSize(float mFontSize) {
        this.mFontSize = mFontSize;
    }


    public float getRatioWidthHeight() {
        return ratioWidthHeight;
    }

    public void setRatioWidthHeight(float ratioWidthHeight) {
        this.ratioWidthHeight = ratioWidthHeight;
    }


    /**
     * getter & setter
     *
     * @return
     */


    public void setGlFont(GlFont mGlFont) {
        update();
    }

    public char getValue() {
        return value;
    }

    public void setValue(char value) {
        this.value = value;
        update();
    }

    /**
     * Constructor
     */

    public GlChar(char value, GlFont font) {
        super(DrawingMode.FILL);
        this.value = value;
        this.mFontSize = 50; // taille d'un caractère par défaut
        this.enableTexturing();
        this.setTexture(font.getMap());
        this.setTexCoord(font.getCharTextCoord(value));
        this.setRatioWidthHeight(font.getCharRatio(value));
        this.setAmbiantColor(new ColorRGBA(1f, 1f, 1f, 1f));
        this.setX(0);
        this.setY(0);
        this.setXspace(font.getXspace(value));
        this.setXoffset(font.getXoffset(value));
        this.setYoffset(font.getYoffset(value));
        this.setBase2HeightRatio(font.getBase2HeightRatio(value));
        this.setBase2AdvanceRatio(font.getBase2AdvanceRatio(value));
        //temporaire : c'est juste pour voir comment sont tracé les caractères
        // this.enableCollisions();
    }

    public void setColor(ColorRGBA colorRGBA) {
        this.setAmbiantColor(colorRGBA);
    }

    @Override
    public float getHeight() {
        return this.mFontSize * this.getBase2HeightRatio();
    }

    @Override
    public float getWidth() {
        return this.getHeight() * getRatioWidthHeight();
    }


}

