package com.familledupuis91.gamingtools.interfaces;

import com.familledupuis91.gamingtools.components.ColorRGBA;
import com.familledupuis91.gamingtools.components.Vertex;
import com.familledupuis91.gamingtools.components.texture.Texture;

import java.nio.ShortBuffer;
import java.util.ArrayList;

public interface Drawable {

    public void draw();

    void setGlVBoId(int index);

    void setGlVBiId(int index);

    int getGlVBiId();

    ShortBuffer getIndices();

    int getNbvertex();

    ArrayList<Vertex> getVertices();

    int getGlVBoId();

    boolean getVisibility();

    boolean isTextureEnabled();

    Texture getTexture();

    float[] getModelView();

    ColorRGBA getAmbiantColor();

    int getGlDrawMode();

    int getNbIndex();
}
