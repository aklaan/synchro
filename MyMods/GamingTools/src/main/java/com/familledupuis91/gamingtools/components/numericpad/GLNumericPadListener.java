package com.familledupuis91.gamingtools.components.numericpad;

import android.os.Bundle;

import java.util.EventListener;

/**
 * Created by rodol on 12/10/2015.
 */
public interface GLNumericPadListener extends EventListener {


    void onClick(Bundle bundle);
    void onClickOk();
    void onClickClear();

}
