package com.familledupuis91.gamingtools.components.fonts;


import com.familledupuis91.gamingtools.R;
import com.familledupuis91.gamingtools.components.texture.*;

import java.io.InputStream;

/**
 * Created by rodol on 18/02/2016.
 */
public class FontConsolas extends GlFont {


    //toutes les instances de FontConsolas vont partager les mêmes données de texture
    private static Texture mTexture;
    private static InputStream xmlData;
    private static FrameCursor frameCursor;

    //getter & setter
    public Texture getMap() {
        return mTexture;
    }

    public void setMap(Texture texture) {
        mTexture = texture;
    }

    public FrameCursor getFrameCursor() {
        return frameCursor;
    }

    public InputStream getXmlData() {
        return FontConsolas.xmlData;
    }

    public void setXmlData(InputStream xmlData) {
        FontConsolas.xmlData = xmlData;
    }

    public int getMapPathId() {
        return R.string.consolas;
    }

    public int getXmlDataPathId() {
        return R.string.consolas_xml_data;
    }

    public float getXspace(int charValue) {
        return this.getFrameCursor().getXspace();
    }

    public float getXoffset(int charValue) {
        return this.getFrameCursor().getXoffset();
    }

    public float getYoffset(int charValue) {
        return this.getFrameCursor().getYoffset();
    }


    //Constructor
    public FontConsolas() {
        //je crée un frameCursor spécifique pour cette font
        //s'il n'en existe deja pas un
        if (this.getFrameCursor() == null) {
            this.frameCursor = new FrameCursor(0);
        }
    }

    public float[] getCharTextCoord(int value) {
        return this.getFrameCursor().getCharTextCoord(value, this);
    }

    public float getCharRatio(int value) {
        return this.getFrameCursor().getCharRatio(value, this);
    }

    public float getBase2HeightRatio(int value){return this.getFrameCursor().getBase2HeightRatio();}

    public float getBase2AdvanceRatio(int value){return this.getFrameCursor().getBase2AdvanceRatio();}
}