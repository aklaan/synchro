package com.familledupuis91.gamingtools.components.fonts;

import com.familledupuis91.gamingtools.components.texture.Texture;

import java.io.InputStream;

/**
 * Created by rodol on 18/02/2016.
 */
public abstract class GlFont {


    //getter & setter
    abstract public Texture getMap();

    abstract public void setMap(Texture texture);

    abstract public void setXmlData(InputStream inputStream);

    abstract public InputStream getXmlData();

    abstract public FrameCursor getFrameCursor();

    abstract public int getMapPathId();

    abstract public int getXmlDataPathId();

    abstract public float[] getCharTextCoord(int charValue);

    abstract public float getCharRatio(int charValue);

    abstract public float getXspace(int charValue);

    abstract public float getXoffset(int charValue);

    abstract public float getYoffset(int charValue);

    abstract public float getBase2HeightRatio(int charValue);

    abstract public float getBase2AdvanceRatio(int value);
}