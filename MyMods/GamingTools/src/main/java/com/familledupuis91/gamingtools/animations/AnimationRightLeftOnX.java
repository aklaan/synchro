package com.familledupuis91.gamingtools.animations;

import com.familledupuis91.gamingtools.components.shapes.Shape;

import android.os.SystemClock;

public class AnimationRightLeftOnX extends Animation {

	float offsetX;

	public AnimationRightLeftOnX(Shape parent) {
		super(parent);

	}

	@Override
	public void launch() {
		super.launch();
		offsetX = 3.0f;
	}

	@Override
	public void play() {
		Shape go  = (Shape) this.getAnimatedGameObject();
		// se déplacer vers la droite pendant 3 seconde
		float elapsedTime = SystemClock.elapsedRealtime() - startTime;

		if (elapsedTime < 2000) {
			// offsetX += 0.5f;
			go.setX(go.getX()+ offsetX);
		} else {

			if (elapsedTime >= 2000 && elapsedTime <= 4000) {
				// offsetX -= 0.5f;
				go.setX(go.getX()- offsetX);
			} else {
				this.setStatus(AnimationStatus.STOPPED);
			}
		}

	}

}
