package com.familledupuis91.gamingtools.components.button;

import android.os.Bundle;

import java.util.EventListener;

/**
 * Created by rodol on 12/10/2015.
 */
public interface GLButtonListener extends EventListener {


    public void onClick(Bundle bundle);

    public void onLongClick();
}
