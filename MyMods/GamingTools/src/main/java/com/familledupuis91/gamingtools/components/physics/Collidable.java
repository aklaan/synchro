package com.familledupuis91.gamingtools.components.physics;

import com.familledupuis91.gamingtools.components.Vertex;

import java.util.ArrayList;

/**
 * Created by rodol on 05/02/2016.
 */
public interface Collidable {

    ArrayList<Vertex> getVertices();

    float[] getModelView();

    boolean isCollisionEnabled();

    boolean isCollisionCheckingEnabled();

    void disableCollisionChecking();

    void enableCollisionChecking();

}
