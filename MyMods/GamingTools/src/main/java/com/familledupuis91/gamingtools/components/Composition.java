package com.familledupuis91.gamingtools.components;

import java.util.ArrayList;

/**
 * Created by Rodolphe on 06/02/2016.
 */
public interface Composition  {

        ArrayList<Composition> getComponent();
        long getIdOnScene();

        }
